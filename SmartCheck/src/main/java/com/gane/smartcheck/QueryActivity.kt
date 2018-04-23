package com.gane.smartcheck

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanManager
import android.device.scanner.configuration.PropertyID
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.gane.smartcheck.bean.BaseBean
import com.gane.smartcheck.bean.LoginBean
import com.gane.smartcheck.bean.ProductBean
import com.gane.smartcheck.service.HttpCore.QUERYALL
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.ui_query.*

class QueryActivity : AppCompatActivity(), Response.Listener<String>, Response.ErrorListener {

    private var scanManager = ScanManager()

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_query)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("查询中...")
        initScan()

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    /**
     * 扫描获取
     */
    fun loadQueryData(barcode: String) {
        progressDialog.show()
        val request = StringRequest(String.format(QUERYALL, "08000101"), this, this)
        Volley.newRequestQueue(this).add(request)
    }


    override fun onResponse(response: String?) {
        progressDialog.dismiss()
        if (response != null) {
            val result = Gson().fromJson<BaseBean<ProductBean>>(response.toString(), object : TypeToken<BaseBean<ProductBean>>() {}.type)
            if (result.code == 200) {
                sv_content.visibility = View.VISIBLE
                rl_hint.visibility = View.GONE
                tv_name.text = result.data.goodsname
                tv_price.text = result.data.saleprice.toString()
                tv_barcode.text = result.data.barcode
                tv_count.text = result.data.goodsnum.toString()
                tv_state.text = result.data.goodsstate
                tv_no.text = result.data.supplierno
                tv_discount.text = result.data.discount.toString()
                tv_model.text = result.data.model
                tv_event.text = result.data.changesetno
            } else {
                Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onErrorResponse(error: VolleyError?) {
        progressDialog.dismiss()
        Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show()
    }

    private fun initScan() {
        scanManager.switchOutputMode(0)
        val filter = IntentFilter()
        val idbuf = intArrayOf(PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG)
        val value_buf = scanManager.getParameterString(idbuf)
        if (value_buf != null && value_buf!![0] != null && value_buf!![0] != "") {
            filter.addAction(value_buf!![0])
        } else {
            filter.addAction(ScanManager.ACTION_DECODE)
        }
        registerReceiver(scannerGunReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        scanManager.closeScanner()
    }

    override fun onResume() {
        super.onResume()
        scanManager.openScanner()
    }

    override fun onDestroy() {
        scanManager.closeScanner()
        unregisterReceiver(scannerGunReceiver)
        super.onDestroy()
    }


    /**
     * 扫描枪的广播
     */
    private var scannerGunReceiver = object: BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (ScanManager.ACTION_DECODE == intent.action) {
                val barcodeByte = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG)
                val barcodeSize = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0)
                val temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, 0.toByte())

                val barcodeText = String(barcodeByte, 0, barcodeSize)
                loadQueryData(barcodeText)
            }
        }

    }


}