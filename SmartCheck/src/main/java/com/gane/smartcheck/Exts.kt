package com.gane.smartcheck

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.gane.smartcheck.db.RoomDb
import com.gane.smartcheck.service.HttpCore
import java.text.SimpleDateFormat
import java.util.*

fun Context.ip() = getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE)
        .getString("IP_CONFIG", HttpCore.DEFAULT_IP)

fun Context.ip(name: String?) {
    getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE).edit().putString("IP_CONFIG", name).commit()
}

fun Context.name() = getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE)
        .getString("LOGIN_NAME", null)

fun Context.name(name: String?) {
    getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE).edit().putString("LOGIN_NAME", name).commit()
}

fun String.timeMdHm(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val date = format.parse(this)
    return SimpleDateFormat("MM-dd HH:mm").format(date)

}

fun Long.timeMdHm(): String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
    return format.format(Date(this))
}

fun Context.username(text: String?) : String? {
    val prefs = getSharedPreferences("smart_check.prefs", Context.MODE_PRIVATE)
    return if (text == null) {
        prefs.getString("username", null)
    } else {
        prefs.edit().putString("username", text).commit()
        null
    }
}

fun Context.password(text: String?) : String? {
    val prefs = getSharedPreferences("smart_check.prefs", Context.MODE_PRIVATE)
    return if (text == null) {
        prefs.getString("password", null)
    } else {
        prefs.edit().putString("password", text).commit()
        null
    }
}


/**
 * 扫描震动
 */
fun Context.scanVib() {
    (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(300)
}

fun Activity.hideSoft(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

     imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)

}


fun AppCompatActivity.subTitle() {
    val login = RoomDb.getInstance(applicationContext).loginDao().getLoginData()
    if (login != null && !TextUtils.isEmpty(login.shopname)) {
        supportActionBar?.subtitle = "分店：" + login.shopname
    }
}

