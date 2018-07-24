package com.gane.shoper.ui.pay

import android.app.AlertDialog
import android.text.TextUtils
import android.widget.Toast
import com.gane.shoper.R
import com.gane.shoper.app.SuperActivity
import com.gane.shoper.http.HttpCode
import com.gane.shoper.http.RetrofitCore
import com.gane.shoper.ui.billing.PayBody
import com.kaopiz.kprogresshud.KProgressHUD
import com.landicorp.android.scan.scanDecoder.ScanDecoder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.ui_pay_channel.*
import sdk.landi.CameraScaner

class PayChannelActivity : SuperActivity(), ScanDecoder.ResultCallback {


    companion object {
        val KEY_MONEY = "MONEY"
        val KEY_NO = "NO"
    }

    var orderMoney: Double = 0.0    // 订单金额
    lateinit var orderNo: String

    private var cameraScaner: CameraScaner? = null

    override fun layoutId() = R.layout.ui_pay_channel

    private var kCommiting: KProgressHUD? = null

    override fun initView() {

        orderMoney = intent.getDoubleExtra(KEY_MONEY, 0.00)
        orderNo = intent.getStringExtra(KEY_NO)

        kCommiting = KProgressHUD(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).setLabel(getString(R.string.pay_ing))

        tv_no.text = orderNo
        tv_money.text = orderMoney.toString()
        tv_pay_money.text = orderMoney.toString()
    }

    override fun initListener() {
        btn_pay.setOnClickListener {
            if (rb_union.isChecked || rb_mall.isChecked) {
                Toast.makeText(this, "暂不支持改方式支付", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        if (cameraScaner == null) {
            cameraScaner = CameraScaner(this, this)
        }
        cameraScaner!!.openBack()
    }



    override fun onResult(p0: String?) {
        if (TextUtils.isEmpty(p0))
            return

        kCommiting?.show()
    }

    override fun onCancel() {
    }

    override fun onTimeout() {
    }

    /**
     * 扫描支付
     * orderId(必须) 订单id,注意一个订单号只能发起扫码支付一次
    barcode(必须): 扫描用户支付二维码信息
    {
    "orderId": "2018040790010003",
    "barcode": "134562548143099859"
    }
     */
    private fun scanPay(scanCode: String) {
        RetrofitCore.getInstance().scanPay(PayBody(orderNo, scanCode))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    kCommiting?.dismiss()
                    if (it.code == HttpCode.OK) {
                        // 保存数据到application中
                        AlertDialog.Builder(this@PayChannelActivity)
                                .setTitle("支付成功")
                                .setMessage("成功收款：" + orderMoney + "元")
                                .setCancelable(false)
                                .setPositiveButton("确认", {_,_ ->
                                    run {
                                        finish()
                                    }
                                })
                                .show()

                    } else {
                        Toast.makeText(this@PayChannelActivity, "支付失败", Toast.LENGTH_SHORT).show()
                    }
                }, {
                    kCommiting?.dismiss()
                    Toast.makeText(this@PayChannelActivity, "支付失败", Toast.LENGTH_SHORT).show()
                })
    }





}