package com.gane.shoper.ui.billing

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

        ll_wx.setOnClickListener { choicePay(0) }
        ll_zfb.setOnClickListener { choicePay(1) }
        ll_sc.setOnClickListener { choicePay(2) }
        rb_wx.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rb_zfb.isChecked = false
                rb_sc.isChecked = false
            }
        }
        rb_zfb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rb_wx.isChecked = false
                rb_sc.isChecked = false
            }
        }
        rb_sc.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rb_zfb.isChecked = false
                rb_wx.isChecked = false
            }
        }
    }

    /**
     * 支付方式UI变化
     */
    private fun choicePay(index: Int) {
        rb_wx.isChecked = index == 0
        rb_zfb.isChecked = index == 1
        rb_sc.isChecked = index == 2
    }

}