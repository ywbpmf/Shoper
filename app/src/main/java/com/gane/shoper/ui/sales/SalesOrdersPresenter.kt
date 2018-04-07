package com.gane.shoper.ui.sales

import com.gane.shoper.app.ShoperApp
import com.gane.shoper.http.HttpCode
import com.gane.shoper.http.RetrofitCore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SalesOrdersPresenter(private val view: SalesOrdersContract.View) : SalesOrdersContract.Presenter {

    init {
        view.presenter = this
    }

    // 页数
    private var page = 1

    override fun attachView() {

    }

    override fun detachView() {

    }

    override fun loadSalesOrders() {
        val clerkno = ShoperApp.appEntity.clerkno
        val limit = 100 // 一页显示20条数据
        val offset = (page - 1) * 20
        val paystate = "00"

        RetrofitCore.getInstance().getOrderList(clerkno?:"", limit, offset/*, paystate*/)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    if (it.code == HttpCode.OK) {
                        // 保存数据到application中
                        view.loadDataSuccess(it.data!!)
                    } else {
                        view.loadDataError()
                    }
                }, {
                    view.loadDataError()
                })
    }


}