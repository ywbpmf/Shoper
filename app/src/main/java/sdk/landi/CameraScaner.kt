package sdk.landi

import android.app.Activity
import com.landicorp.android.scan.scanDecoder.ScanDecoder

/**
 * 前、后摄像头
 */
class CameraScaner(val activity: Activity, val callback: ScanDecoder.ResultCallback)  {

    private val scanDecoder = ScanDecoder(activity)
    private var cameraParams = HashMap<String, String>()
    private var map = mapOf<String, String>(
//            ScanDecoder.PAR_SCAN_TIMEOUT to "10000"
    )

    init {
        cameraParams
    }

    private fun openCameraById(cameraId: Int) {
        val ret = scanDecoder.Create(cameraId, callback)
        if (ret !=0 ) {
            scanDecoder.Destroy()
        }
        var msg = "camera create failed"
        if (ret == 0) { // 成功
            val res = scanDecoder.startScanDecode(activity, cameraParams)
            if (res != 0) {
                msg = "ScanDecoder no create" // 1
            } else {
                return
            }
        }

        when (ret) {
            1 -> msg = "init decode lib failed"
            2 -> msg = "you has Created once"
            3 -> msg = "open camera failed"
            4 -> msg = "license activate failed"
        }
        LandiSdk.toast(activity, msg)
    }


    fun openFront() {
        openCameraById(ScanDecoder.CAMERA_ID_FRONT)

    }

    fun openBack() {
        openCameraById(ScanDecoder.CAMERA_ID_BACK)
    }



}
