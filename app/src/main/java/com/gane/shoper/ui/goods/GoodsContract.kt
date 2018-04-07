package com.gane.shoper.ui.goods

import com.gane.shoper.mvp.BasePresenter
import com.gane.shoper.mvp.BaseView

/**
 *
 */
interface GoodsContract {

    interface View : BaseView<Presenter> {



    }


    interface Presenter : BasePresenter {

        /**
         * 请求加载商品列表
         */
        fun reqGoodsList()





    }


}