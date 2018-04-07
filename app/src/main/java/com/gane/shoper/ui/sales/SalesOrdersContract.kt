package com.gane.shoper.ui.sales

import com.gane.shoper.entity.OrderQueryBean
import com.gane.shoper.mvp.BasePresenter
import com.gane.shoper.mvp.BaseView


interface SalesOrdersContract {

    interface View: BaseView<Presenter> {

        /**
         * 加载数据成功
         */
        fun loadDataSuccess(data: List<OrderQueryBean>)

        /**
         * 加载数据失败
         */
        fun loadDataError()

        fun loadMoreEnabled(enabled: Boolean)

    }

    interface Presenter : BasePresenter {

        /**
         * 加载所有的订单列表数据
         */
        fun loadSalesOrders()

    }

}