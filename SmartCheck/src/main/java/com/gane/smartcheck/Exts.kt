package com.gane.smartcheck

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

/**
 *
 */
fun AppCompatActivity.initToolbar(toolbar: Toolbar) {
   initToolbar(toolbar, false)
}

fun AppCompatActivity.initToolbar(toolbar: Toolbar, back: Boolean) {
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)
    supportActionBar?.setDisplayHomeAsUpEnabled(back)
    supportActionBar?.setDisplayShowHomeEnabled(false)
}

