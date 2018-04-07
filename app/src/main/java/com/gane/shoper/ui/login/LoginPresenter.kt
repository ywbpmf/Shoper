package com.gane.shoper.ui.login

import com.gane.shoper.app.ShoperApp
import com.gane.shoper.http.HttpCode
import com.gane.shoper.http.RetrofitCore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 *
 */
class LoginPresenter(private val view: LoginContract.View) : LoginContract.Presenter  {

    init {
        view.presenter = this
    }

    override fun attachView() {

    }

    override fun detachView() {
    }

    override fun doLogin(phone: String, pass: String) {
        view.showLoading()
        toLogin(phone, pass)
    }

    override fun toLogin(phone: String, pass: String) {
        RetrofitCore.getLoginInstance().login(phone, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe( {
                    if (it.code == HttpCode.OK) {
                        // 保存数据到application中
                        ShoperApp.appEntity.clerkname = it.data?.user?.clerkname
                        ShoperApp.appEntity.clerkno = it.data?.user?.clerkno
                        ShoperApp.appEntity.supplierno = it.data?.insts?.get(0)?.supplierno

                        view.loginSuccess(it.data!!)
                    } else {
                        view.loginFailed()
                    }
                }, {
                    view.loginFailed()
                })

    }


}