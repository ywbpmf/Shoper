package com.gane.shoper.ui.billing

import android.app.AlertDialog
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import com.gane.shoper.R
import com.gane.shoper.app.SuperActivity
import com.gane.shoper.entity.InstBarcodesBean
import com.gane.shoper.ui.sales.BillingContract
import com.gane.shoper.ui.sales.BillingPresenter
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.internal.operators.flowable.FlowableTake
import kotlinx.android.synthetic.main.ui_biliing.*

/**
 *
 */
class BillingActivity : SuperActivity(), BillingContract.View {

    override var presenter: BillingContract.Presenter = BillingPresenter(this)

    private var kLoading: KProgressHUD? = null
    private var kCommiting: KProgressHUD? = null

    private lateinit var spinnerAdapter: ArrayAdapter<String>

    /**
     * 记录着所有的开单数据
     */
    private var billingList = ArrayList<Billing>()

    /**
     * 记录着所有的开单数据  barcode 不允许重复
     */
    private var billingMap = HashMap<String, Float>()


    /** 商户所有的开单吗 */
    private var allBilling: List<InstBarcodesBean>? = null


    override fun layoutId() = R.layout.ui_biliing


    override fun initView() {
        et_money.filters = arrayOf(MoneyFilter())

        kLoading = KProgressHUD(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
        kCommiting = KProgressHUD(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).setLabel(getString(R.string.commit_orders_ing))


        spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        loadAllBilling()
    }

    override fun initListener() {
        btn_submit.setOnClickListener {
            if(billingList.isNotEmpty()) {
                kCommiting?.show()
                var list = ArrayList<Billing>()
                for ((k, v) in billingMap) {
                    list.add(Billing(k, v))
                }
                presenter.openBilling(list)
            }
        }
        btn_next.setOnClickListener {
            if (inputIsOk()) {
                et_money.setText("")
                et_money.requestFocus()
            }
        }
    }

    /**
     * 当前输入的文本是否正确
     *
     */
    private fun inputIsOk(): Boolean {
        val code = spinner.selectedItem.toString()
        var moneyStr = et_money.text.toString().trim()

        if (TextUtils.isEmpty(code) || TextUtils.isEmpty(moneyStr)) {
            Toast.makeText(this, R.string.input_money, Toast.LENGTH_SHORT).show()
            return false
        }

        val money: Float
        try {
            money = moneyStr.toFloat()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, R.string.input_money_err, Toast.LENGTH_SHORT).show()
            return false
        }


        billingList.add(Billing(code, money))

        if (billingMap.containsKey(code)) {
            val oldMoney = billingMap[code]!!
            billingMap.put(code, oldMoney + money)
        } else {
            billingMap.put(code, money)
        }

        return true
    }

    override fun onBackPressed() {
        if (kLoading != null && kLoading!!.isShowing){
            kLoading!!.dismiss()
        }
        super.onBackPressed()
    }

    /**
     * 加载所有的开单码
     */
    private fun loadAllBilling() {
        kLoading?.show()
        presenter.loadAllBilling()
    }

    override fun loadAllBillingSuccess(data: List<InstBarcodesBean>) {
        allBilling = data
        for (i in 0 until data.size) {
            spinnerAdapter.add(data[i].barcode)
        }

        kLoading?.dismiss()
    }

    override fun loadAllBillingError() {
        kLoading?.dismiss()
    }

    override fun commitOrderSuccess() {
        kCommiting?.dismiss()
        AlertDialog.Builder(this).setTitle(R.string.hint)
                .setMessage(R.string.commit_orders_success)
                .setNegativeButton(R.string.okey) { dialog, which -> finish() }
                .setOnCancelListener { finish() }
                .show()
    }

    override fun commitOrderError() {
        kCommiting?.dismiss()
        AlertDialog.Builder(this).setTitle(R.string.hint)
                .setMessage(R.string.commit_orders_err)
                .setNegativeButton(R.string.okey) { dialog, which ->  }
                .show()
    }



}