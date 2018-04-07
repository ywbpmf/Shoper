package com.gane.shoper.app

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.app_toolbar.*

/**
 *
 */
abstract class SuperActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        initToolbar()
        initView()
        initListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @LayoutRes abstract fun layoutId(): Int
    open fun initView() {}
    open fun initListener() {}

    fun initToolbar() {
        if (toolbar == null) return

        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}