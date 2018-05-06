package com.gane.smartcheck

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.ui_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_main)
        subTitle()

        btn_query.setOnClickListener {
            startActivity(Intent(this, QueryActivity::class.java))
        }
        btn_check.setOnClickListener {
            startActivity(Intent(this, CheckActivity::class.java))
        }


    }


    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_exit, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit) {
            startActivity(Intent(this, LoginActivity::class.java).putExtra("auto_login", false))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_MENU)
            return true
        return super.dispatchKeyEvent(event)
    }


}