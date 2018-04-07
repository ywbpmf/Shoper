package com.gane.shoper.ui

import android.view.Menu
import com.gane.shoper.R
import com.gane.shoper.app.SuperActivity
import com.gane.shoper.ui.dialog.ClauseSheetDialog
import com.gane.shoper.ui.dialog.InputDataDialog
import com.gane.shoper.util.ViewUtils
import kotlinx.android.synthetic.main.ui_goods_action.*
import kotlin.collections.ArrayList

/**
 *
 */
class GoodsActionActivity : SuperActivity() {

    private var unitList = ArrayList<String>()

    override fun layoutId() = R.layout.ui_goods_action

    override fun initView() {
        ViewUtils.setHitText(et_goods_name, getString(R.string.input_goods_name), 12)
    }

    override fun initListener() {
        resources.getStringArray(R.array.GOODS_UNITS).forEach { unitList.add(it) }
        rl_unit.setOnClickListener { ClauseSheetDialog(this,  object : ClauseSheetDialog.ClauseSheetListener {
            override fun onClauseSheet(index: Int, text: String) {
                tv_unit.text = text
            }}, unitList, choose = tv_unit.text.toString()).show() }


        rl_jhj.setOnClickListener { InputDataDialog(this, "").show() }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_goods_action, menu)
        return true
    }



}