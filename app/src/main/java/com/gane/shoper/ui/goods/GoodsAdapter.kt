package com.gane.shoper.ui.goods

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gane.shoper.R

/**
 *
 */
class GoodsAdapter : BaseQuickAdapter<Int, BaseViewHolder>(R.layout.adapter_goods, arrayListOf(0, 1, 2, 3, 4, 5)) {


    override fun convert(helper: BaseViewHolder, item: Int) {

    }


}