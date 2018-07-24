package com.gane.shoper.ui.sales

import com.gane.shoper.app.ShoperApp
import com.gane.shoper.entity.OrderQueryBean
import com.gane.shoper.http.HttpCode
import com.gane.shoper.http.RetrofitCore
import com.gane.shoper.ui.billing.Billing
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.gane.shoper.entity.User
import com.gane.shoper.ui.billing.BillingAll


class BillingPresenter(private val view: BillingContract.View) : BillingContract.Presenter {


    init {
        view.presenter = this
    }

    // 页数
    private var page = 1

    override fun attachView() {

    }

    override fun detachView() {
    }

    override fun loadAllBilling() {
        val supplierno = ShoperApp.appEntity.supplierno ?: ""
        RetrofitCore.getInstance().getInstBarcodes(supplierno)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    if (it.code == HttpCode.OK && it.data != null && it.data!!.isNotEmpty()) {
                        // 保存数据到application中
                        view.loadAllBillingSuccess(it.data!!)
                    } else {
                        view.loadAllBillingError()
                    }
                }, {
                    view.loadAllBillingError()
                })
    }


    override fun openBilling(data: List<Billing>) {
        val supplierno = ShoperApp.appEntity.supplierno ?: ""
        val clerkno = ShoperApp.appEntity.clerkno ?: ""

        val billingAll = BillingAll(supplierno, clerkno, data)

        RetrofitCore.getInstance().commitOrders(billingAll)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    if (it.code == HttpCode.OK && it.data != null) {
                        // 保存数据到application中
                        view.commitOrderSuccess(it.data!!)
                    } else {
                        view.commitOrderError()
                    }
                }, {
                    view.commitOrderError()
                })
    }

    override fun loadVipCard(cardno: String) {
        RetrofitCore.getInstance().getVipCard(cardno)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    if (it.code == HttpCode.OK && it.data != null) {
                        // 保存数据到application中
                        view.loadCardSuccess(it.data!!)
                    } else {
                        view.loadCardError()
                    }
                }, {
                    view.loadCardError()
                })
    }


}