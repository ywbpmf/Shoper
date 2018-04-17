package com.gane.shoper.ui.statistics

import com.gane.shoper.app.ShoperApp
import com.gane.shoper.http.HttpCode
import com.gane.shoper.http.RetrofitCore
import com.gane.shoper.util.TimeUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AnalyzePresenter(private val view: AnalyzeContract.View) :  AnalyzeContract.Presenter {

    /**
     * 当前查询的时间，（只用于精确到月份）
     */
    private var queryDate: Long = System.currentTimeMillis()

    override fun attachView() {

    }

    override fun detachView() {
    }

    override fun reqSalesData() {
        val clerkno = ShoperApp.appEntity.clerkno!!
        val beginTime = TimeUtils.getMonthStartDayText(queryDate)
        val endTime = TimeUtils.getMonthLastDayText(queryDate)

        RetrofitCore.getInstance().getStatistics(clerkno, beginTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    if (it.code == HttpCode.OK && it.data != null) {
                        view.loadSalesDataSuccess(it.data!!)
                    } else {
                        view.loadSalesDataError()
                    }
                }, {
                    view.loadSalesDataError()
                } )
    }




}