/* NFCard is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

NFCard is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wget.  If not, see <http://www.gnu.org/licenses/>.

Additional permission under GNU GPL version 3 section 7 */

package com.android.tool.nfc;

import java.nio.ByteBuffer;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.nfc.tech.IsoDep;

public class CpuCard {

	public final static byte[] DFN_PSE = { (byte) 0x3f, (byte) 0x00 };
	public final static byte[] file15 = { (byte) 0x00, (byte) 0x15 };
	public final static byte[] file16 = { (byte) 0x00, (byte) 0x16 };
	public final static byte[] file17 = { (byte) 0x00, (byte) 0x17 };

	public final static byte[] jy1 = { (byte) 0x00 };
	public final static byte[] jy2 = { (byte) 0x08 };
	public final static byte[] jy3 = { (byte) 0x09 };
	public final static byte[] jy4 = { (byte) 0x00 };
	public final static byte[] jy5 = { (byte) 0x14 };
	public final static byte[] jy6 = { (byte) 0x24 };
	public final static byte[] jy7 = { (byte) 0x31 };
	public final static byte[] jy8 = { (byte) 0x4F };
	public final static byte[] jy9 = { (byte) 0x77 };
	public final static byte[] jy10 = { (byte) 0x80 };
	public final static byte[] jy11 = { (byte) 0x9E };
	public final static byte[] jy12 = { (byte) 0xA4 };
	public final static byte[] jy13 = { (byte) 0xAA };
	public final static byte[] jy14 = { (byte) 0xAE };
	public final static byte[] jy15 = { (byte) 0xB2 };
	public final static byte[] jy16 = { (byte) 0xB6 };
	public final static String key = "390139023903390439053906390739FF";

	// 1pay.sys.ddf01
	public static final byte[] EMPTY = { 0 };
	protected byte[] data;

	protected CpuCard() {
		data = CpuCard.EMPTY;
	}

	protected CpuCard(byte[] bytes) {
		data = (bytes == null) ? CpuCard.EMPTY : bytes;
	}

	public boolean match(byte[] bytes) {
		return match(bytes, 0);
	}

	public boolean match(byte[] bytes, int start) {
		final byte[] data = this.data;
		if (data.length <= bytes.length - start) {
			for (final byte v : data) {
				if (v != bytes[start++])
					return false;
			}
		}
		return true;
	}

	public boolean match(byte tag) {
		return (data.length == 1 && data[0] == tag);
	}

	public boolean match(short tag) {
		final byte[] data = this.data;
		if (data.length == 2) {
			final byte d0 = (byte) (0x000000FF & tag);
			final byte d1 = (byte) (0x000000FF & (tag >> 8));
			return (data[0] == d0 && data[1] == d1);
		}
		return false;
	}

	public int size() {
		return data.length;
	}

	public byte[] getBytes() {
		return data;
	}

	// @Override
	// public String toString() {
	// return Util.toHexString(data, 0, data.length);
	//
	// }

	public final static class ID extends CpuCard {
		public ID(byte[] bytes) {
			super(bytes);
		}
	}

	public final static class Response extends CpuCard {
		public static final byte[] EMPTY = {};
		public static final byte[] ERROR = { 0x6F, 0x00 }; // SW_UNKNOWN

		public Response(byte[] bytes) {
			super((bytes == null || bytes.length < 2) ? Response.ERROR : bytes);
		}

		public byte getSw1() {
			return data[data.length - 2];
		}

		public byte getSw2() {
			return data[data.length - 1];
		}

		public short getSw12() {
			final byte[] d = this.data;
			int n = d.length;
			return (short) ((d[n - 2] << 8) | (0xFF & d[n - 1]));
		}

		public boolean isOkey() {
			return equalsSw12(SW_NO_ERROR);
		}

		public boolean equalsSw12(short val) {
			return getSw12() == val;
		}

		public int size() {
			return data.length - 2;
		}

		@SuppressLint("NewApi")
		public byte[] getBytes() {
			return isOkey() ? Arrays.copyOfRange(data, 0, size())
					: Response.EMPTY;
		}
	}

	@SuppressLint("NewApi")
	public final static class Tag {
		private final IsoDep nfcTag;
		private ID id;

		public Tag(IsoDep tag) {
			nfcTag = tag;
			id = new ID(tag.getTag().getId());
		}

		public ID getID() {
			return id;
		}

		/**
		 * 验证
		 * 
		 * @return
		 */
		public Response verify(String key) {
			byte[] bytes = key.getBytes();
			ByteBuffer buff = ByteBuffer.allocate(bytes.length + 6);
			buff.put((byte) 0x00).put((byte) 0x20).put((byte) 0x00)
					.put((byte) 0x00).put((byte) (bytes.length)).put(bytes)
					.put((byte) (0x04));
			return new Response(transceive(buff.array()));
		}

		/**
		 * 重置
		 * 
		 * @param isEP
		 * @return
		 */
		public Response initPurchase(boolean isEP) {
			final byte[] cmd = {
					(byte) 0x80, // CLA Class
					(byte) 0x50, // INS Instruction
					(byte) 0x01, // P1 Parameter 1
					(byte) (isEP ? 2 : 1), // P2 Parameter 2
					(byte) 0x0B, // Lc
					(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x11, (byte) 0x22, (byte) 0x33,
					(byte) 0x44, (byte) 0x55, (byte) 0x66, (byte) 0x0F, // Le
			};

			return new Response(transceive(cmd));
		}

		/**
		 * 
		 * @param isEP
		 * @return
		 */
		public Response getBalance(boolean isEP) {
			final byte[] cmd = { (byte) 0x80, // CLA Class
					(byte) 0x5C, // INS Instruction
					(byte) 0x00, // P1 Parameter 1
					(byte) (isEP ? 2 : 1), // P2 Parameter 2
					(byte) 0x04, // Le
			};
			return new Response(transceive(cmd));
		}

		/**
		 * 读记录
		 * 
		 * @param sfi
		 * @param index
		 * @return
		 */
		public Response readRecord(int sfi, int index) {
			final byte[] cmd = { (byte) 0x00, // CLA Class
					(byte) 0xB2, // INS Instruction
					(byte) index, // P1 Parameter 1
					(byte) ((sfi << 3) | 0x04), // P2 Parameter 2
					(byte) 0x00, // Le
			};

			return new Response(transceive(cmd));
		}

		/**
		 * 读记录
		 * 
		 * @param sfi
		 * @param index
		 * @return
		 */
		public Response readRecord(int sfi) {
			final byte[] cmd = { (byte) 0x00, // CLA Class
					(byte) 0xB2, // INS Instruction
					(byte) 0x01, // P1 Parameter 1
					(byte) ((sfi << 3) | 0x05), // P2 Parameter 2
					(byte) 0x00, // Le
			};

			return new Response(transceive(cmd));
		}

		/**
		 * 读文件
		 * 
		 * @param sfi
		 * @return
		 */
		public Response readBinary(int sfi) {
			final byte[] cmd = { (byte) 0x00, // CLA Class
					(byte) 0xB0, // INS Instruction
					(byte) (0x00000080 | (sfi & 0x1F)), // P1 Parameter 1
					(byte) 0x00, // P2 Parameter 2
					(byte) 0x00, // Le
			};

			return new Response(transceive(cmd));
		}

		// /**
		// * 通过id查询
		// * @param name
		// * @return00A40000023F0000
		// */
		public Response selectByID(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xA4) // INS Instruction
					.put((byte) 0x00) // P1 Parameter 1
					.put((byte) 0x00) // P2 Parameter 2
					.put((byte) name.length) // Lc
					.put(name).put((byte) 0x00); // Le

			return new Response(transceive(buff.array()));
		}

		/**
		 * 读数据
		 * 
		 * @param sfi
		 * @return
		 */
		public Response readData(int sfi) {
			final byte[] cmd = { (byte) 0x80, // CLA Class
					(byte) 0xCA, // INS Instruction
					(byte) 0x00, // P1 Parameter 1
					(byte) (sfi & 0x1F), // P2 Parameter 2
					(byte) 0x00, // Le
			};

			return new Response(transceive(cmd));
		}

		/**
		 * 验证
		 */
		public Response external(byte[] keys) {

			ByteBuffer buff = ByteBuffer.allocate(keys.length + 6);
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0x82) // INS Instruction
					.put((byte) 0x00) // P1 Parameter 1
					.put((byte) 0x00) // P2 Parameter 2
					.put((byte) keys.length) // Lc
					.put(keys).put((byte) 0x04);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 查询名字查询
		 * 
		 * @param name
		 * @return
		 */
		public Response selectByName(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xA4) // INS Instruction
					.put((byte) 0x04) // P1 Parameter 1
					.put((byte) 0x00) // P2 Parameter 2
					.put((byte) name.length) // Lc
					.put(name).put((byte) 0x00); // Le
			return new Response(transceive(buff.array()));
		}

		/**
		 * GET CHALLENGE
		 */
		public Response getchallenge() {
			ByteBuffer buff = ByteBuffer.allocate(5);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0x84).put((byte) 0x00) // INS Instruction
					.put((byte) 0x00) // P1 Parameter 1
					.put((byte) 0x08); // Le
			return new Response(transceive(buff.array()));
		}

		// /**
		// * get response
		// */
		// public Response getResponse()
		// {
		// byte [] cmd=
		// {
		// (byte)0x00,
		// (byte)0xc0,
		// (byte)
		//
		// };
		// }

		/**
		 * 写交易文件16
		 */
		public Response writeJY16Binary1(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x96).put((byte) 0x00) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件16
		 */
		public Response writeJY16Binary2(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x96).put((byte) 0x08) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件16
		 */
		public Response writeJY16Binary3(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x96).put((byte) 0x09) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写卡类型
		 */
		public Response writeBinary(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x95).put((byte) 0x00) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写卡号
		 */
		public Response writeBinary2(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x95).put((byte) 0x02) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写主体编码
		 */
		public Response writeBinary3(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x95).put((byte) 0x0C) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写客户名称
		 */
		public Response writeBinary4(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x95).put((byte) 0x19) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写客户身份证
		 */
		public Response writeBinary5(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x95).put((byte) 0x37) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写客户身联系电话
		 */
		public Response writeBinary6(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x95).put((byte) 0x49) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写客户类型
		 */
		public Response writeBinary7(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x95).put((byte) 0x56) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		;

		/**
		 * 写交易文件17
		 */
		public Response writeJY17Binary7(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x97).put((byte) 0x80) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件17
		 */
		public Response writeJY17Binary8(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x97).put((byte) 0x9E) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件17
		 */
		public Response writeJY17Binary9(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x97).put((byte) 0xA4) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件17
		 */
		public Response writeJY17Binary10(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x97).put((byte) 0xAA) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件17
		 */
		public Response writeJY17Binary11(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x97).put((byte) 0xAE) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件17
		 */
		public Response writeJY17Binary12(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x97).put((byte) 0xB2) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件17
		 */
		public Response writeJY17Binary13(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x97).put((byte) 0xB6) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件17
		 */
		public Response writeJY17Binary1(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x97).put((byte) 0x00) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件17
		 */
		public Response writeJY17Binary2(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x97).put((byte) 0x14) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件17
		 */
		public Response writeJY17Binary3(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x97).put((byte) 0x24) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件17
		 */
		public Response writeJY17Binary4(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x97).put((byte) 0x31) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件17
		 */
		public Response writeJY17Binary5(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x97).put((byte) 0x4F) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 写交易文件17
		 */
		public Response writeJY17Binary6(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			// 命令标头
			buff.put((byte) 0x00) // CLA Class
					.put((byte) 0xD6) // INS Instruction
					.put((byte) 0x97).put((byte) 0x77) // P1 Parameter 1
					.put((byte) name.length).put(name).put((byte) 0x00);
			return new Response(transceive(buff.array()));
		}

		/**
		 * 连接
		 */
		public void connect() {
			try {
				nfcTag.connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * 关闭
		 */
		public void close() {
			try {
				nfcTag.close();
			} catch (Exception e) {
			}
		}

		public byte[] transceive(final byte[] cmd) {
			try {
				return nfcTag.transceive(cmd);
			} catch (Exception e) {
				return Response.ERROR;
			}
		}
	}

	public static final short SW_NO_ERROR = (short) 0x9000;
	public static final short SW_BYTES_REMAINING_00 = 0x6100;
	public static final short SW_WRONG_LENGTH = 0x6700;
	public static final short SW_SECURITY_STATUS_NOT_SATISFIED = 0x6982;
	public static final short SW_FILE_INVALID = 0x6983;
	public static final short SW_DATA_INVALID = 0x6984;
	public static final short SW_CONDITIONS_NOT_SATISFIED = 0x6985;
	public static final short SW_COMMAND_NOT_ALLOWED = 0x6986;
	public static final short SW_APPLET_SELECT_FAILED = 0x6999;
	public static final short SW_WRONG_DATA = 0x6A80;
	public static final short SW_FUNC_NOT_SUPPORTED = 0x6A81;
	public static final short SW_FILE_NOT_FOUND = 0x6A82;
	public static final short SW_RECORD_NOT_FOUND = 0x6A83;
	public static final short SW_INCORRECT_P1P2 = 0x6A86;
	public static final short SW_WRONG_P1P2 = 0x6B00;
	public static final short SW_CORRECT_LENGTH_00 = 0x6C00;
	public static final short SW_INS_NOT_SUPPORTED = 0x6D00;
	public static final short SW_CLA_NOT_SUPPORTED = 0x6E00;
	public static final short SW_UNKNOWN = 0x6F00;
	public static final short SW_FILE_FULL = 0x6A84;

}
