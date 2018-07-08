package com.gane.shoper.ui.goods

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.gane.shoper.R
import com.gane.shoper.app.SuperActivity
import com.gane.shoper.entity.InstBarcodesBean
import com.gane.shoper.ui.GoodsActionActivity
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.ui_goods.*

/**
 * 商品列表
 */
class GoodsInfoActivity : SuperActivity(), GoodsInfoContract.View {

    override var presenter: GoodsInfoContract.Presenter = GoodsPresenter(this)

    private lateinit var adapter: GoodsAdapter

    override fun layoutId() = R.layout.ui_goods

    override fun initView() {
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_goods, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> startActivityForResult(Intent(this, GoodsActionActivity::class.java), 9)
        }
        return super.onOptionsItemSelected(item)
    }



}