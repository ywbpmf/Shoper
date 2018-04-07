package com.gane.shoper.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.gane.shoper.R
import com.gane.shoper.ui.LandiActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
            R.id.nav_about -> {
                startActivity(Intent(this, LandiActivity::class.java
                 ))
            }
            R.id.nav_update -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.setDataAndType(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)), "*/*")
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
