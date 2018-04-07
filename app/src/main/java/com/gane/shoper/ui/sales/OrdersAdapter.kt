package com.gane.shoper.ui.sales

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gane.shoper.R
import com.gane.shoper.entity.OrderQueryBean

/**
 *
 */
class OrdersAdapter : BaseQuickAdapter<OrderQueryBean, BaseViewHolder>(R.layout.adapter_orders) {


    override fun convert(helper: BaseViewHolder, item: OrderQueryBean) {
        helper.setText(R.id.tv_billing, mContext.getString(R.string.saleno_, item.saleno))
        helper.setText(R.id.tv_time, item.saledate)
        helper.setText(R.id.tv_name, "商品编号：" + item.shopno)
        helper.setText(R.id.tv_state, if (item.paystate == "00") R.string.pay_no else R.string.pay_yes)
        helper.setText(R.id.tv_money, mContext.getString(R.string.money_, item.owesum.toString()))
    }


}