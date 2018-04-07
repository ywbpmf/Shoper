package com.gane.shoper.ui.login

import com.gane.shoper.entity.UserLoginBean
import com.gane.shoper.mvp.BasePresenter
import com.gane.shoper.mvp.BaseView

/**
 *
 */
interface LoginContract {

    interface View: BaseView<Presenter> {

        /**
         * 登录失败
         */
        fun loginFailed()

        /**
         * 登录成功
         */
        fun loginSuccess(data: UserLoginBean)

        /**
         * 加载进度条
         */
        fun showLoading()
    }

    interface Presenter : BasePresenter {

        /**
         * 登录前的验证
         */
        fun doLogin(phone: String, pass: String)

        /**
         * 请求网络登录
         */
        fun toLogin(phone: String, pass: String)

    }

}