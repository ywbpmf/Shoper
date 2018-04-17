package com.gane.smartcheck

import android.app.ProgressDialog
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
import android.widget.Checkable
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.gane.smartcheck.bean.BaseBean
import com.gane.smartcheck.bean.LoginBean
import com.gane.smartcheck.bean.ProductBean
import com.gane.smartcheck.db.RoomDb
import com.gane.smartcheck.service.HttpCore.COMMIT
import com.gane.smartcheck.service.HttpCore.INST
import com.gane.smartcheck.service.HttpCore.QUERYNO
import com.gane.smartcheck.service.HttpCore.volleyQueue
import com.gane.smartcheck.service.ScannerGunReceiver
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.ui_check.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * 盘点
 */
class CheckActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog

    private lateinit var scannerGunReceiver: ScannerGunReceiver
    private var scanManager = ScanManager()

    private lateinit var mNo: String

    /** (barcode, checknum) */
    private var commitMap = LinkedHashMap<String, Int>()
    /** (barcode, ProductBean)  */
    private var commitBeanMap = HashMap<String, ProductBean>()

    private var commitList = ArrayList<ProductBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_check)

        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)

        tv_title.text = title

        rv_goods.adapter = CheckAdapter(this)
        btn_submit.setOnClickListener {
            if (commitMap.isEmpty()) {
                Toast.makeText(this, "暂无数据.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            commitFactcheck()
        }

        loadNowNo()
    }

    /**
     * 进入加载最新的盘点单号
     */
    private fun loadNowNo() {
        progressDialog.setMessage("获取单号...")
        volleyQueue.add(StringRequest(String.format(QUERYNO,
                RoomDb.getInstance(applicationContext).loginDao().getLoginData()?.shopno), {
            val jsonObj = JsonParser().parse(it) as JsonObject
            if (jsonObj != null && jsonObj.get("code").asInt == 200) {
                mNo = jsonObj.get("data").asJsonObject.get("factcheckno").asString
                initScan()
            } else {
                Toast.makeText(this, "获取单号失败", Toast.LENGTH_SHORT).show()
            }
            progressDialog.dismiss()
        }, {
            Toast.makeText(this, "获取单号失败", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }))
    }

    /**
     * 根据code查询商品信息
     */
    private fun queryBarCode(barcode: String) {
        if (commitMap.containsKey(barcode)) { // 查询该barcode是否已在添加列表，如果在  直接数量+1即可
            commitMap[barcode] = commitMap[barcode]!! + 1
            commitList.add(commitBeanMap[barcode]!!)
            return
        }
        progressDialog.setMessage("查询中...")
        val request = StringRequest(String.format(INST, barcode), {
            val result = Gson().fromJson<BaseBean<ProductBean>>(it, object : TypeToken<BaseBean<ProductBean>>() {}.type)
            if (result != null && result.code == 200) {
                commitList.add(result.data)
                if (commitMap.containsKey(barcode)) {
                    commitMap[barcode] = commitMap[barcode]!! + 1
                } else {
                    commitMap[barcode] = 1
                }
                if (!commitBeanMap.containsKey(barcode))
                    commitBeanMap[barcode] = result.data

            } else {
                Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show()
            }
            progressDialog.dismiss()
        }, {
            Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        })
        volleyQueue.add(request)
    }

    /**
     * 提交盘点
     */
    private fun commitFactcheck() {
        val loginBean = RoomDb.getInstance(applicationContext).loginDao().getLoginData()

        val jsonAry = JSONArray()
        for ((k, v) in commitMap) {
            val obj = JSONObject()
            obj.put("barcode", k)
            obj.put("checknum", v)
            jsonAry.put(obj)
        }

        val jsonObj = JSONObject()
        jsonObj.put("factcheckno", mNo)
        jsonObj.put("operator", loginBean?.userno)
        jsonObj.put("shopno", loginBean?.shopno)
        jsonObj.put("info", mNo)

        val request = JsonObjectRequest(Request.Method.POST,COMMIT, jsonObj, {

        }, {

        })
        request.tag = Checkable::class.simpleName
        volleyQueue.add(request)
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


