package com.gane.shoper.ui.pay

import com.gane.shoper.R
import com.gane.shoper.app.SuperActivity
import kotlinx.android.synthetic.main.ui_pay_channel.*

class PayChannelActivity : SuperActivity() {

    companion object {
        val KEY_MONEY = "MONEY"
        val KEY_NO = "NO"
    }

    override fun layoutId() = R.layout.ui_pay_channel

    override fun initView() {
        super.initView()
        tv_no.text = intent.getStringExtra(KEY_NO)
        tv_money.text = intent.getDoubleExtra(KEY_MONEY, 0.00).toString()
    }

    override fun initListener() {
        btn_pay.setOnClickListener {

        }



    }


}