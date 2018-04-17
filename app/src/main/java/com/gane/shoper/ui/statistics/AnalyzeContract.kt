package com.gane.shoper.ui.statistics

import com.gane.shoper.entity.SaleEntity
import com.gane.shoper.entity.StatisticsEntity
import com.gane.shoper.mvp.BasePresenter
import com.gane.shoper.mvp.BaseView

interface AnalyzeContract {

    interface View: BaseView<Presenter> {

        fun loadSalesDataSuccess(data: StatisticsEntity)

        fun loadSalesDataError()

    }

    interface Presenter: BasePresenter {

        /**
         * 加载每月的销售数据
         */
        fun reqSalesData()

    }

}