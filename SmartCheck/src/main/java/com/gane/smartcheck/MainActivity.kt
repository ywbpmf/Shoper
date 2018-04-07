package com.gane.smartcheck

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gane.smartcheck.service.ScannerGunReceiver
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.ui_main.*
import android.device.scanner.configuration.PropertyID
import android.content.IntentFilter
import android.device.ScanManager
import com.gane.smartcheck.bean.Goods


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_main)


        initToolbar(toolbar)
        tv_title.text = title

        btn_query.setOnClickListener {  }
        btn_check.setOnClickListener {
            startActivity(Intent(this, CheckActivity::class.java))
        }


    }









}