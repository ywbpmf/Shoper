package com.gane.shoper.ui

import android.content.Context
import android.util.Log
import android.view.View
import com.gane.shoper.R
import com.gane.shoper.app.SuperActivity
import com.landicorp.android.scan.scanDecoder.ScanDecoder
import kotlinx.android.synthetic.main.ui_landi.*
import sdk.landi.CameraScaner
import sdk.landi.LandiSdk
import sdk.landi.LandiService

/**
 * Created by eiibio on 2018/2/18.
 */
class LandiActivity : SuperActivity(), LandiService, ScanDecoder.ResultCallback {
    override fun onDeviceServiceCrash() {
        TODO(  "not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun baseContext(): Context = this.baseContext

    private val TAG: String = "LANDI_SDK"

    private var cameraScaner: CameraScaner? = null


    override fun layoutId() = R.layout.ui_landi


    override fun initListener() {
        val result = LandiSdk.bind(this)
        btn1.setOnClickListener {
            Log.e(TAG, "注册设备返回值：" + result)
        }

        btn2.setOnClickListener {

        }

        btn3.setOnClickListener {
            LandiSdk.startBeep(3000)
        }

        btn4.setOnClickListener {
            LandiSdk.stopBeep()
        }

        btn5.setOnClickListener {
            LandiSdk.printBarCode(this)
        }

        btn6.setOnClickListener {
            LandiSdk.openScanner(this)
        }


        btn7.setOnClickListener {
            if (cameraScaner == null) cameraScaner = CameraScaner(this, this)
            cameraScaner!!.openFront()
        }


        btn8.setOnClickListener {
            if (cameraScaner == null) cameraScaner = CameraScaner(this, this)
            cameraScaner!!.openBack()
        }
    }

    override fun onDestroy() {
        LandiSdk.unBind()
        super.onDestroy()
    }

    override fun onResult(p0: String?) {
        tv8.text = p0
    }

    override fun onTimeout() {
    }

    override fun onCancel() {
    }





}