package com.gane.smartcheck

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.*
import android.device.ScanManager
import android.device.scanner.configuration.PropertyID
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.gane.smartcheck.bean.BaseBean
import com.gane.smartcheck.bean.ProductBean
import com.gane.smartcheck.db.RoomDb
import com.gane.smartcheck.service.HttpCore.COMMIT
import com.gane.smartcheck.service.HttpCore.INST
import com.gane.smartcheck.service.HttpCore.QUERYNO
import com.gane.smartcheck.service.HttpCore.volleyQueue
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.ui_check.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * 盘点
 */
class CheckActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog

    private var scanManager = ScanManager()

    private lateinit var mNo: String

    /** (barcode, productList) */
    private var sumList = ArrayList<ProductBean>()

    private var allList = ArrayList<ProductBean>()

    private lateinit var adapter: CheckAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_check)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        subTitle()


        initScan()

        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)

        adapter = CheckAdapter(this)
        layoutManager = LinearLayoutManager(this)
        rv_goods.layoutManager = layoutManager
        rv_goods.adapter = adapter

        btn_submit.setOnClickListener {
            hideSoft(rv_goods)
            if (sumList.isEmpty()) {
                Toast.makeText(this, "暂无数据.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            commitFactcheck()
        }
        tv_revoke.setOnClickListener { // 撤销
            if (allList.isEmpty()) {
                scanResultChange()
                return@setOnClickListener
            }
            val pro = allList.removeAt(allList.size - 1)

            scanResultChange()
            for (i in 0 until sumList.size) {
                if (sumList[i].barcode == pro.barcode) {
                    if (sumList[i].count > 0) {
                        sumList[i].count -= 1
                        moveToIndex(i)
                        adapter.notifyItemChanged(i)
                    }
                    break
                }
            }
        }
        tv_his.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java)
                    .putExtra("id", mNo))
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
                tv_no.text = "单号:$mNo"

//                queryBarCode("6970607350010")
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
        for (i in 0 until sumList.size) {
            if (sumList[i].barcode == barcode) { // 需添加的barcode已经存在， 则直接添加
                moveToIndex(i)
                sumList[i].count += 1
                adapter.notifyItemChanged(i)

                allList.add(sumList[i])
                scanResultChange()
                return
            }
        }

        progressDialog.setMessage("查询中...")
        progressDialog.show()
        val request = StringRequest(String.format(INST, barcode), {
            val result = Gson().fromJson<BaseBean<ProductBean>>(it, object : TypeToken<BaseBean<ProductBean>>() {}.type)
            if (result != null && result.code == 200) {
                allList.add(result.data)
                scanResultChange()
                for (i in 0 until sumList.size) {
                    if (sumList[i].barcode == barcode) { // 需添加的barcode已经存在， 则直接添加
                        sumList[i].count += 1
                        moveToIndex(i)
                        adapter.notifyItemChanged(i)
                        return@StringRequest
                    }
                }

                result.data.count = 1
                sumList.add(result.data)
                val last_index = sumList.lastIndex
                moveToIndex(last_index)
                adapter.notifyItemChanged(last_index)
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

        val resultList = ArrayList<ProductBean>()

        val jsonAry = JSONArray()
        for (i in 0 until sumList.size) {
            if (sumList[i].count <= 0)
                break
            resultList.add(sumList[i])
            val obj = JSONObject()
            obj.put("barcode", sumList[i].barcode)
            obj.put("checknum", sumList[i].count)
            jsonAry.put(obj)
        }

        if (resultList.isEmpty()) {
            Toast.makeText(this, "暂无数据.", Toast.LENGTH_SHORT).show()
            return
        }


        val jsonObj = JSONObject()
        jsonObj.put("checkno", mNo)
        jsonObj.put("operator", loginBean?.userno)
        jsonObj.put("shopno", loginBean?.shopno)
        jsonObj.put("info", jsonAry)

        val request = JsonObjectRequest(Request.Method.POST,COMMIT, jsonObj, {
            if (it != null && it.getInt("code") == 200) {
                var factcheckno = it.getJSONObject("data").getString("factcheckno")
                var opdate = it.getJSONObject("data").getString("opdate")
                for (i in 0 until resultList.size) {
                    resultList[i].factcheckno = factcheckno
                    resultList[i].opdate = opdate
                    resultList[i].checkno = mNo
                }
                RoomDb.getInstance(applicationContext).productDao().insert(resultList)
                
                AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("提交判断数据成功，是否继续")
                        .setPositiveButton("继续") { _, _ ->
                            sumList.clear()
                            allList.clear()
                            scanResultChange()
                            adapter.notifyDataSetChanged()
                        }
                        .setNegativeButton("退出", { _, _ ->
                            finish()
                        }).show()
                
            } else {
                Toast.makeText(this, "盘点商品失败.", Toast.LENGTH_SHORT).show()
            }
        }, {
            Toast.makeText(this, "盘点发生异常.", Toast.LENGTH_SHORT).show()
        })
        request.tag = "CheckActivity"
        volleyQueue.add(request)
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
        scanManager.openScanner()
    }


    override fun onDestroy() {
        scanManager.closeScanner()
        unregisterReceiver(scannerGunReceiver)
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }


    /**
     * 扫描结果的UI变化
     */
    private fun scanResultChange() {
        if (allList.isEmpty()) {
            rl_scan.visibility = View.GONE
        } else {
            rl_scan.visibility = View.VISIBLE
            tv_scan_result.text = allList.last().goodsname
        }
    }

    /**
     * 扫描到商品定位的商品的item
     */
    private fun moveToIndex(position: Int) {
        if (position >= 0 && position < adapter.itemCount) {
            layoutManager.scrollToPositionWithOffset(position, 0)
        }
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

                scanVib()
                val barcodeText = String(barcodeByte, 0, barcodeSize)
                queryBarCode(barcodeText)
            }
        }

    }


    inner class CheckAdapter(val context: Context) : RecyclerView.Adapter<CheckAdapter.VH>() {


        override fun onBindViewHolder(vh: VH, position: Int) {
            vh.tv_num.text = (position + 1).toString()
            vh.tv_name.text = sumList[position].goodsname
            vh.et_count.setText(sumList[position].count.toString())

            vh.iv_add.setOnClickListener {
                hideSoft(rv_goods)
                sumList[position].count += 1
                notifyItemChanged(position)
            }
            vh.iv_minus.setOnClickListener {
                hideSoft(rv_goods)
                if (sumList[position].count > 1) {
                    sumList[position].count -= 1
                    notifyItemChanged(position)
                }
            }


            vh.et_count.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    val count: Int = try {
                        p0?.toString()?.toInt() ?: 0
                    } catch (e: Exception) {
                        0
                    }
                    sumList[position].count = count
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })

        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
            return VH(LayoutInflater.from(context).inflate(R.layout.adapter_check, parent, false))
        }

        override fun getItemCount(): Int {
            return sumList.size
        }


        inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val et_count = itemView.findViewById<EditText>(R.id.et_count)
            val tv_name = itemView.findViewById<TextView>(R.id.tv_name)
            val iv_minus = itemView.findViewById<ImageView>(R.id.iv_minus)
            val iv_add = itemView.findViewById<ImageView>(R.id.iv_add)
            val tv_num = itemView.findViewById<TextView>(R.id.tv_num)
        }

    }


}


class PlusFilter : InputFilter {
    override fun filter(source: CharSequence?, start: Int, end: Int,
                        dest: Spanned?, dstart: Int, dend: Int): CharSequence {
        if (source.toString() == "0")
            return dest.toString()
        return ""
    }


}







