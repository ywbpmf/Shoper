package com.gane.shoper.ui.goods

import com.gane.shoper.entity.InstBarcodesBean
import com.gane.shoper.mvp.BasePresenter
import com.gane.shoper.mvp.BaseView

/**
 *
 */
interface GoodsContract {

    interface View : BaseView<Presenter> {

        fun loadGoodsListSuccess(data: List<InstBarcodesBean>)

        fun loadGoodsListError()

    }


    interface Presenter : BasePresenter {

        /**
         * 请求加载商品列表
         */
        fun reqGoodsList()





    }


}