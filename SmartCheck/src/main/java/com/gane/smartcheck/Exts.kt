package com.gane.smartcheck

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import java.text.SimpleDateFormat


fun Context.name() = getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE)
        .getString("LOGIN_NAME", null)

fun Context.name(name: String?) {
    getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE).edit().putString("LOGIN_NAME", name).commit()
}



fun String.timeMdHm() {
    val format = SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSSZ")


}
