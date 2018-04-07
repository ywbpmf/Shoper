package com.gane.shoper.ui.main

import android.content.Context
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gane.shoper.R

/**
 *
 */
class MainAdapter(context: Context) : BaseQuickAdapter<Int, MainAdapter.ViewHolder>
            (R.layout.adapter_main, arrayListOf(0, 1, 2, 3, 4, 5)) {

    private val titles = context.resources.getStringArray(R.array.MAIN_TITLES)
    private var icons = arrayOf(
            R.drawable.main_billing, R.drawable.main_sales_ticket, R.drawable.main_sales_return,
            R.drawable.main_analyze, R.drawable.main_send_out, R.drawable.main_shop_manager)

    override fun convert(helper: ViewHolder, item: Int) {
        helper.setText(R.id.text, titles[item])
                .setImageResource(R.id.icon, icons[item])
    }


    inner class ViewHolder(view: View) : BaseViewHolder(view)

}

