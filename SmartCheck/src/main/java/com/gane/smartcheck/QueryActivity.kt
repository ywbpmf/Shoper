package com.gane.smartcheck

import android.app.ProgressDialog
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

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_query)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("查询中...")

        loadQueryData()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    /**
     * 扫描获取
     */
    fun loadQueryData() {
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
                tv_name.text = result.data.goodsname
                tv_price.text = result.data.saleprice.toString()
                tv_barcode.text = result.data.barcode
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

}