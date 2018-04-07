package com.gane.smartcheck

import android.content.Context
import android.content.IntentFilter
import android.device.ScanManager
import android.device.scanner.configuration.PropertyID
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gane.smartcheck.service.ScannerGunReceiver
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.ui_check.*

/**
 * 盘点
 */
class CheckActivity : AppCompatActivity() {

    private lateinit var scannerGunReceiver: ScannerGunReceiver
    private var scanManager = ScanManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_check)

        initToolbar(toolbar, true)
        tv_title.text = title

        rv_goods.adapter = CheckAdapter(this)

        initScan()
    }

    private fun initScan() {
        scanManager.openScanner()
        scanManager.switchOutputMode(0)
        val filter = IntentFilter()
        val idbuf = intArrayOf(PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG)
        val value_buf = scanManager.getParameterString(idbuf)
        if (value_buf != null && value_buf!![0] != null && value_buf!![0] != "") {
            filter.addAction(value_buf!![0])
        } else {
            filter.addAction(ScanManager.ACTION_DECODE)
        }
        scannerGunReceiver = ScannerGunReceiver()
        registerReceiver(scannerGunReceiver, filter)
    }

    override fun onDestroy() {
        scanManager.closeScanner()
        unregisterReceiver(scannerGunReceiver)
        super.onDestroy()
    }

}


class CheckAdapter(val context: Context) : RecyclerView.Adapter<CheckAdapter.VH>() {

    override fun onBindViewHolder(holder: VH?, position: Int) {
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        return VH(LayoutInflater.from(context).inflate(R.layout.adapter_check, parent, false))
    }

    override fun getItemCount(): Int {
        return 20
    }


    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}


