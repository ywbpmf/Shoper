package com.gane.shoper.ui.sales

import com.gane.shoper.entity.InstBarcodesBean
import com.gane.shoper.entity.OrderCommitBean
import com.gane.shoper.mvp.BasePresenter
import com.gane.shoper.mvp.BaseView
import com.gane.shoper.ui.billing.Billing


interface BillingContract {

    interface View: BaseView<Presenter> {


        fun loadAllBillingSuccess(data: List<InstBarcodesBean>)
        fun loadAllBillingError()

        fun commitOrderSuccess(data: OrderCommitBean)
        fun commitOrderError()

    }

    interface Presenter : BasePresenter {

        /**
         * 加载所有的开单码
         */
        fun loadAllBilling()

        /**
         * 开单
         */
        fun openBilling(data: List<Billing>)

    }

}