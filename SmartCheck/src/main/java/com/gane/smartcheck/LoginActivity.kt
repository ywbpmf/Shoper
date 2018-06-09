package com.gane.smartcheck

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gane.smartcheck.bean.BaseBean
import com.gane.smartcheck.bean.LoginBean
import com.gane.smartcheck.db.RoomDb
import com.gane.smartcheck.service.HttpCore.LOGIN
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.ui_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), Response.Listener<JSONObject>, Response.ErrorListener {

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_login)

        btn_login.setOnClickListener {
            val name = et_user.text.toString()
            val pass = et_pass.text.toString()
            if (name.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "输入账号或密码", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            toLogin(name, pass)
        }


        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("登录中...")
        progressDialog.setCancelable(false)

        val name = username(null)
        val pass = password(null)
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)) {
            et_user.setText(name)
            et_pass.setText(pass)
            if (intent.getBooleanExtra("auto_login", true)) {
                toLogin(name!!, pass!!)
            }
        }

    }

    /**
     * 登录
     */
    private fun toLogin(name: String, pass: String) {
        progressDialog.show()
       val jsonObj =  JSONObject()
        jsonObj.put("username", name)
        jsonObj.put("password", pass)
        val request = JsonObjectRequest(Request.Method.POST, LOGIN, jsonObj, this, this)
        Volley.newRequestQueue(this).add(request)
    }


    override fun onResponse(response: JSONObject?) {
        progressDialog.dismiss()
        if (response == null) {
            Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show()
            return
        }
        val result = Gson().fromJson<BaseBean<LoginBean>>(response.toString(), object : TypeToken<BaseBean<LoginBean>>() {}.type)
        if (result.code == 200) {
            username(et_user.text.toString())
            password(et_pass.text.toString())
            RoomDb.getInstance(applicationContext).loginDao().addLoginData(result.data)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onErrorResponse(error: VolleyError?) {
        progressDialog.dismiss()
        Toast.makeText(this, "登录异常", Toast.LENGTH_SHORT).show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_login, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (R.id.ip == item?.itemId) {
            val dialogView = layoutInflater.inflate(R.layout.dialog_ip, null)
            val etIp = dialogView.findViewById<EditText>(R.id.et_ip)
            etIp.setText(ip())
            etIp.setSelection(ip().length)
            AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setTitle("IP配置")
                    .setPositiveButton("确认") { _, _ ->
                        ip(etIp.text.toString())
                    }
                    .show()
        }
        return super.onOptionsItemSelected(item)
    }


}