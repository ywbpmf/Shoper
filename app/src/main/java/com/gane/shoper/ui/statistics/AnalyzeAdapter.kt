package com.gane.shoper.ui.statistics

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gane.shoper.R
import com.gane.shoper.entity.SaleEntity
import com.gane.shoper.util.TimeUtils

class AnalyzeAdapter : BaseQuickAdapter<SaleEntity, BaseViewHolder>(R.layout.adapter_analyze) {

    override fun convert(helper: BaseViewHolder, item: SaleEntity) {
        helper.setText(R.id.tv_date, item.day)
                .setText(R.id.tv_week, TimeUtils.getWeekText(item.day!!))
                .setText(R.id.tv_money, item.salesum.toString())
    }




}