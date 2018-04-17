package com.gane.shoper.ui.goods

import com.gane.shoper.app.ShoperApp
import com.gane.shoper.http.HttpCode
import com.gane.shoper.http.RetrofitCore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 */
class GoodsPresenter(private val view: GoodsContract.View) : GoodsContract.Presenter {

    override fun attachView() {

    }

    override fun detachView() {
    }

    override fun reqGoodsList() {
        val supplierno = ShoperApp.appEntity.supplierno ?: ""
        RetrofitCore.getInstance().getInstBarcodes(supplierno)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    if (it.code == HttpCode.OK && it.data != null && it.data!!.isNotEmpty()) {
                        // 保存数据到application中
                        view.loadGoodsListSuccess(it.data!!)
                    } else {
                        view.loadGoodsListError()
                    }
                }, {
                    view.loadGoodsListError()
                })
    }


}