package com.android.tool.nfc;

import java.io.IOException;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.nfc.tech.MifareClassic;

public class MifareClassicCard {
	MifareClassic mClassic = null;

	public MifareClassicCard(MifareClassic mClassic) {
		this.mClassic = mClassic;
	}

	public void connect() {

		try {
			mClassic.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	public void colse() throws Exception {

		mClassic.close();

	}

	/**
	 * Authenticate a sector with key A. 写入秘钥
	 * 
	 * @param sectorIndex
	 * @param key
	 * @return
	 */
	public boolean authenticateSectorWithKeyA(int sectorIndex, byte[] key)
			throws Exception {

		return mClassic.authenticateSectorWithKeyA(sectorIndex, key);

	}

	/**
	 * Authenticate a sector with key B. 写入秘钥b
	 * 
	 * @param sectorIndex
	 * @param key
	 * @return
	 */
	public boolean authenticateSectorWithKeyB(int sectorIndex, byte[] key)
			throws Exception {
		return mClassic.authenticateSectorWithKeyB(sectorIndex, key);

	}

	/**
	 * Return the sector that contains a given block.
	 * 
	 * @param blockIndex
	 * @return
	 */
	public int blockToSector(int blockIndex) {
		return mClassic.blockToSector(blockIndex);
	}

	/**
	 * 
	 * @param blockIndex
	 * @param value
	 */
	public void decrement(int blockIndex, int value) {
		try {
			mClassic.decrement(blockIndex, value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 返回快总数
	 * 
	 * @return
	 */
	public int getBlockCount() {
		return mClassic.getBlockCount();
	}

	/**
	 * 返回指定目录块数、 Return the number of blocks in the given sector.
	 */
	public int getBlockCountInSector(int sectorIndex) {
		return mClassic.getBlockCount();
	}

	/**
	 * 返回可以发送的最大最大字节数
	 * 
	 * @return
	 */
	public int getMaxTransceiveLength() {
		return mClassic.getMaxTransceiveLength();
	}

	/**
	 * Return the number of MIFARE Classic sectors.
	 * 
	 * @return
	 */
	public int getSectorCount() {
		return mClassic.getMaxTransceiveLength();
	}

	/**
	 * 返回标签字节的大小 Return the size of the tag in bytes.
	 * 
	 * @return
	 */
	public int getSize() {
		return mClassic.getSize();
	}

	/**
	 * 返回兼容的标记类型
	 * 
	 * @return
	 */
	public int getType() {
		return mClassic.getType();
	}

	/**
	 * Increment a value block, storing the result in the temporary block on the
	 * tag.
	 * 
	 * @param blockIndex
	 * @param value
	 */
	public void increment(int blockIndex, int value) {
		try {
			mClassic.increment(blockIndex, value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 检测连接是否建立
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return mClassic.isConnected();
	}

	/**
	 * Read 16-byte block. 读取指定块数据
	 * 
	 * @param blockIndex
	 * @return
	 */
	public byte[] readBlock(int blockIndex) {
		try {
			return mClassic.readBlock(blockIndex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将一个值快赋值到临时快
	 * 
	 * @param blockIndex
	 */
	public void restore(int blockIndex) {
		try {
			mClassic.readBlock(blockIndex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Return the first block of a given sector. 返回指定扇区的第一块
	 * 
	 * @param sectorIndex
	 * @return
	 */
	public int sectorToBlock(int sectorIndex) {
		return mClassic.sectorToBlock(sectorIndex);
	}

	/**
	 * 写入连接超时时间
	 * 
	 * @param timeout
	 */
	public void setTimeout(int timeout) {
		mClassic.setTimeout(timeout);
	}

	/**
	 * Send raw NfcA data to a tag and receive the response. 原始数据发送到卡和接受响应
	 * 
	 * @param data
	 * @return
	 */
	public byte[] transceive(byte[] data) {
		try {
			return mClassic.transceive(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Copy from the temporary block to a value block. 从临时快赋值一个值
	 * 
	 * @param blockIndex
	 */
	public void transfer(int blockIndex) {
		try {
			mClassic.transfer(blockIndex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 写入字节到指定块
	 * 
	 * @param blockIndex
	 * @param data
	 */
	public void writeBlock(int blockIndex, byte[] data) {
		try {
			mClassic.writeBlock(blockIndex, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 写入数据到块
	 */
	@SuppressLint("NewApi")
	public String wirteCarCode(String carCode, int block, String key) {
		boolean isTrue;
		String string = "";
		try {
			if (block % 4 == 3) {
				string = "此块是秘钥块！";
				return string;
			}
			connect();
			int keyblock = block / 4;
			if ("".equals(key))
				isTrue = authenticateSectorWithKeyA(keyblock,
						MifareClassic.KEY_DEFAULT);
			else {
				byte[] bytes = hexStringToByte(key);
				byte[] keyBytes = Arrays.copyOf(bytes, 6);
				isTrue = authenticateSectorWithKeyA(keyblock, keyBytes);
			}
			byte[] bytes = carCode.trim().getBytes("UTF-8");
			byte[] a = Arrays.copyOf(bytes, 16);
			writeBlock(block, a);
			if (isTrue) {
			} else {
				string = "秘钥错误！";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			string = "写入失败！";
		} finally {
			try {
				colse();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return string;
	}

	/**
	 * 读取块数据
	 */
	@SuppressLint("NewApi")
	public String readCarCode(int block, String key) {
		String string = "";
		boolean isTrue;
		try {
			connect();
			int keyblock = block / 4;
			if ("".equals(key))
				isTrue = authenticateSectorWithKeyA(keyblock,
						MifareClassic.KEY_DEFAULT);
			else {
				byte[] bytes = hexStringToByte(key);
				byte[] keyBytes = Arrays.copyOf(bytes, 6);
				isTrue = authenticateSectorWithKeyA(keyblock, keyBytes);
			}
			if (isTrue) {
				byte[] k = readBlock(block);
				string = new String(k, "utf-8");
			} else {
				string = "秘钥错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			string = "读取失败";
		} finally {
			try {
				colse();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return string;
	}

	/**
	 * 修改密码
	 * 
	 * @param block
	 * @param newPassword
	 *            不得超过6字节
	 * @param key
	 * @return
	 */
	@SuppressLint("NewApi")
	public String modifyPassword(int block, String newPassword, String key) {
		boolean isTrue;
		String string = "";
		try {
			if (block % 4 != 3) {
				string = "此块不是秘钥块！";
				return string;
			}

			connect();
			int keyblock = block / 4;// 计算扇区
			if ("".equals(key))
				isTrue = authenticateSectorWithKeyA(keyblock,
						MifareClassic.KEY_DEFAULT);
			else {
				byte[] bytes = hexStringToByte(key);
				byte[] keyBytes = Arrays.copyOf(bytes, 6);
				isTrue = authenticateSectorWithKeyA(keyblock, keyBytes);
			}
			if (isTrue) {
				byte[] k = readBlock(block);
				byte[] bytes = hexStringToByte(newPassword);
				byte[] a = Arrays.copyOf(bytes, 6);
				for (int i = 0; i < 6; i++) {
					k[i] = a[i];
				}
				writeBlock(block, k);
			} else {
				string = "秘钥错误！";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			string = "写入失败！";
		} finally {
			try {
				colse();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return string;
	}

	// 字符序列转换为16进制字符串
	private String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
			System.out.println(buffer);
			stringBuilder.append(buffer);
		}
		return stringBuilder.toString();
	}

	/**
	 * 把16进制字符串转换成字节数组
	 * 
	 * @param hexString
	 * @return byte[]
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static int toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
}
