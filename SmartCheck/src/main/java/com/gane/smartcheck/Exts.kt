package com.gane.smartcheck

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar


fun Context.name() = getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE)
        .getString("LOGIN_NAME", null)

fun Context.name(name: String?) {
    getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE).edit().putString("LOGIN_NAME", name).commit()
}

