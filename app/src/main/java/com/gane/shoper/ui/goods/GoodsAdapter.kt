package com.gane.shoper.ui.goods

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gane.shoper.R
import com.gane.shoper.entity.InstBarcodesBean

/**
 *
 */
class GoodsAdapter : BaseQuickAdapter<InstBarcodesBean, BaseViewHolder>(R.layout.adapter_goods) {


    override fun convert(helper: BaseViewHolder, item: InstBarcodesBean) {
        helper.setText(R.id.tv_name, item.goodsname)
                .setText(R.id.tv_code, mContext.getString(R.string.saleno_,item.barcode))
    }


}