package sdk.landi

import com.gane.shoper.util.TimeUtils
import com.landicorp.android.eptapi.device.Printer
import com.landicorp.android.eptapi.device.Printer.Format
import com.landicorp.android.eptapi.exception.RequestException

/**
 * 打印模板
 */
class PrintCoder(val landiService: LandiService) {


    private val progress: Printer.Progress = object : Printer.Progress() {
        override fun doPrint(printer: Printer) {
            val format = Format()
            format.ascSize = Format.ASC_DOT5x7
            format.ascScale = Format.ASC_SC1x2
            printer.setFormat(format)
            printer.printText("            我的店铺\n")

            format.ascScale = Format.ASC_SC1x1
            printer.printText("\n订单号  ：" + 201805050505)
            printer.printText("\n柜台    ：" + "    ")
            printer.printText("\n营业员  ：" + "张三")
            printer.printText("\n开单时间：" + TimeUtils.formatNow())
            printer.printText("\n----------------------------------")
            printer.printText("\n顾客联")
            printer.printText("\n----------------------------------")

            printer.printText("\n商品编号：" + "0000000000000")
            printer.printText("\n库区    ：" + "0000000000000")
            printer.printText("\n数量    ：" + 1)
            printer.printText("\n应付    ：" + 200.00)
            printer.printText("\n实付    ：" + 200.00)
            printer.printText("\n----------------------------------")

            printer.printText("\n合计")
            printer.printText("\n数量    ：" + 1)
            printer.printText("\n应付    ：" + 200.00)
            printer.printText("\n实付    ：" + 200.00)
            printer.printText("\n可开发票金额：" + 200.00)
            printer.printText("\n")
            printer.printText("\n===================================")


            // 进退纸（行数）正数进纸
            printer.feedLine(3)
        }

        override fun onCrash() {
            landiService.onDeviceServiceCrash()
        }

        override fun onFinish(code: Int) {
            when (code) {
                Printer.ERROR_NONE -> {
                    // 打印完成
                }
                else -> {
                    getErrorDescription(code)
                }
            }
        }

        fun getErrorDescription(code: Int): String {
            when (code) {
                Printer.ERROR_PAPERENDED -> return "Paper-out, the operation is invalid this time"
                Printer.ERROR_HARDERR -> return "Hardware fault, can not find HP signal"
                Printer.ERROR_OVERHEAT -> return "Overheat"
                Printer.ERROR_BUFOVERFLOW -> return "The operation buffer mode position is out of range"
                Printer.ERROR_LOWVOL -> return "Low voltage protect"
                Printer.ERROR_PAPERENDING -> return "Paper-out, permit the latter operation"
                Printer.ERROR_MOTORERR -> return "The printer core fault (too fast or too slow)"
                Printer.ERROR_PENOFOUND -> return "Automatic positioning did not find the alignment position, the paper back to its original position"
                Printer.ERROR_PAPERJAM -> return "paper got jammed"
                Printer.ERROR_NOBM -> return "Black mark not found"
                Printer.ERROR_BUSY -> return "The printer is busy"
                Printer.ERROR_BMBLACK -> return "Black label detection to black signal"
                Printer.ERROR_WORKON -> return "The printer power is open"
                Printer.ERROR_LIFTHEAD -> return "Printer head lift"
                Printer.ERROR_LOWTEMP -> return "Low temperature protect"
            }
            return "unknown error ($code)"
        }

    }


    init {
        progress.addStep { printer ->
            printer.setAutoTrunc(true)
            printer.setMode(Printer.MODE_VIRTUAL)
        }
    }

    fun start() {
        try {
            progress.start()
        } catch (e: RequestException) {
            e.printStackTrace()
            landiService.onDeviceServiceCrash()
        }
    }

}