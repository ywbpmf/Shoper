package sdk.landi

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.landicorp.android.eptapi.DeviceService
import com.landicorp.android.eptapi.device.Beeper
import com.landicorp.android.eptapi.device.MagCardReader
import com.landicorp.android.eptapi.device.Printer
import com.landicorp.android.eptapi.device.Scanner

/**
 * 联迪Sdk公共管理类
 */
object LandiSdk {

    fun toast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    /************************************* 设备服务***************************************** */

    /**
     * 使用设备之前，必须先绑定设备的金融服务
     * 这里则登录到金融设备服务,设备中同时只允许一个进程登录到主控中
     *
     * @return false = 发生异常, 需要重新登录
     */
    fun bind(context: Context) : Boolean {
        return try {
            DeviceService.login(context)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 解绑
     */
    fun unBind() {
        DeviceService.logout()
    }

    /************************************* 蜂鸣器 ***************************************** */

    /**
     * 开始蜂鸣
     *
     * @param ms 毫秒
     */
    fun startBeep(ms: Int) {
        Beeper.startBeep(ms)
    }

    /**
     * 结束蜂鸣
     */
    fun stopBeep() {
        Beeper.stopBeep()
    }


    /************************************* 扫描枪 ***************************************** */

    fun openScanner(context: Context) {
        val scanner = Scanner("COM2")
        val listener = object : Scanner.OnScanListener() {
            // 设备奔溃
            override fun onCrash() {
                toast(context, "设备奔溃")
                scanner.close()
            }

            // 扫描成功
            override fun onScanSuccess(code: String?) {
                toast(context, "扫描结果： " + code)

            }
            // 扫描失败
            override fun onScanFail(error: Int) {
                when (error) {
                    ERROR_COMMERR -> toast(context, "通讯错误")
                    ERROR_TIMEOUT -> toast(context, "超时")
                    ERROR_FAIL -> toast(context, "其它错误")

                }

            }
        }
        scanner.setOnScanListener(listener)

        // 扫描器打开成功，则开启扫描
        if (Scanner.ERROR_NONE != scanner.open()) {
            toast(context, "扫描器打开失败")
            return
        }

        scanner.scan()
    }

    /************************************* 前/后 摄像头 ***************************************** */
    // 见 CameraScanner





    /************************************* 磁条卡 ***************************************** */

    /**
     * 启用磁道
     *
     * @param id 磁道id  MagCardReader.TRK1  TRK2   TRK3
     */
    fun enableTrack(id: Int) {
        MagCardReader.getInstance().enableTrack(id)
    }

    /**
     * 禁用磁道
     */
    fun disableTrack(id: Int) {
        MagCardReader.getInstance().disableTrack(id)
    }

    /**
     * 设置 LRC 检验开关  默认开启
     */
    fun setLRCSwitch(switch: Boolean) {
        MagCardReader.getInstance().setLRCCheckEnabled(switch)
    }

    /**
     * 开始寻卡(如果已经调用了该方法，暂未返回。继续调用将被视为无效)
     *
     * @param listener 寻卡监听器
     *          checkValid(int[] trackStates, String[] track)  检查磁道信息  会在 onCardStriped 前触发
     *          如果检查失败则不会触发onCardStriped。检查到磁道不为空并且状态不为 TRACK_STATE_OK 就认为刷卡失败
     *              trackStates 磁道状态, 数组长度固定为3 有效值如下:
     *                      { TRACK_STATE_NULL, TRACK_STATE_OK, TRACK_STATE_HEADERR, TRACK_STATE_PARERR,
     *                          TRACK_STATE_TAILERR, TRACK_STATE_LRCERR, TRACK_STATE_ENDERR }
     *              track 3 个磁道数据
     *              return false 表示磁道错误
     *         isTrackExists  检查磁道是否存在。用于决定 onCardStriped 的输入参数，如果磁道数据存在则在 onCardStriped 触发时体现
     *                  默认情况下判断磁道数据不为 TRACK_STATE_NULL，则认为磁道存在。
     *         onCardStriped 刷卡成功时触发的消息
     *         onFail(code) 刷卡失败时触发的消息
     *              ERROR_TIMEOUT - 超时, ERROR_FAILED - 失败
     *         getEventId()  获取对应的事件 ID
     *
     */
    fun searchCard(listener: MagCardReader.OnSearchListener) {
        MagCardReader.getInstance().searchCard(listener)
    }

    /**
     * 同上
     *
     * @param trk 磁道 ID，如 TRK1|TRK2
     *
     *
     */
    fun searchCard(activity: Activity, trk: Int, listener: MagCardReader.OnSearchListener) {
        MagCardReader.getInstance().searchCard(activity, trk, listener)
    }

    /**
     * 停止寻卡
     */
    fun stopSearch() {
        MagCardReader.getInstance().stopSearch()
    }


    /************************************* 打印机 ***************************************** */

    /**
     * 打印条形码
     *
     * @param codeWidth 条码宽度(建议>=2 有效值:1~8, -1为默认值)
     * @param codeHeight 1~320 且必须是8的倍数（-1表示默认值）
     * @param columnOff 列偏移量（-1表示默认值）
     * @param lineOff 行偏移量（-1表示默认值）
     * @param barcode 条码内容
     */
    fun printBarCode(landiService: LandiService) {
        PrintCoder(landiService).start()
    }

    /**
     * 获取当前打印机的状态
     *
     * @return ERROR_NONE  正常
     *          ERROR_PAPERENDED 缺纸
     *          ERROR_HARDERR 硬件错误
     *          ERROR_OVERHEAT 打印头过热
     *          ERROR_BUFOVERFLOW - 缓冲模式下所操作的位置超出范围
     *          ERROR_LOWVOL 低压保护
     *          ERROR_PAPERENDING 纸张将要用尽，还允许打印(单步进针打特有返回值)
     *          ERROR_MOTORERR 打印机芯故障(过快或者过慢)
     *          ERROR_PENOFOUND 自动定位没有找到对齐位置,纸张回到原来位置
     *          ERROR_PAPERJAM 卡纸
     *          ERROR_NOBM 没有找到黑标
     *          ERROR_BUSY 打印机处于忙状态
     *          ERROR_BMBLACK 黑标探测器检测到黑色信号
     *          ERROR_WORKON 打印机电源处于打开状态
     *          ERROR_LIFTHEAD 打印头抬起(自助热敏打印机特有返回值)
     *          ERROR_CUTPOSITIONERR 切纸刀不在原位(自助热敏打印机特有返回值)
     *          ERROR_LOWTEMP 低温保护或 AD 出错(自助热敏打印机特有返回值)
     *          ERROR_COMMERR  手座机状态正常，但通讯失败（520 针打特有返回值）
     *
     */
    fun getPrintStatus() =  Printer.getInstance().status



}