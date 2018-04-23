package com.gane.smartcheck

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.ui_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_main)


        btn_query.setOnClickListener {
            startActivity(Intent(this, QueryActivity::class.java))
        }
        btn_check.setOnClickListener {
            startActivity(Intent(this, CheckActivity::class.java))
        }


    }









}