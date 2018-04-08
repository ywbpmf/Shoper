package com.gane.shoper.ui.billing

import android.app.AlertDialog
import android.graphics.Color
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gane.shoper.R
import com.gane.shoper.app.SuperActivity
import com.gane.shoper.entity.InstBarcodesBean
import com.gane.shoper.ext.hideKeyboard
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


    private lateinit var bottomBehavior: BottomSheetBehavior<View>


    private lateinit var billingAdapter: BillingAdapter

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

        billingAdapter = BillingAdapter()
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = billingAdapter
        billingAdapter.bindToRecyclerView(rv_list)
        billingAdapter.setEmptyView(R.layout.view_billing_empty)

        bottomBehavior = BottomSheetBehavior.from(ll_bottom)
        bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    ll_div.visibility = View.GONE
                } else {
                    ll_div.visibility = View.VISIBLE
                }
            }
        })

        loadAllBilling()
    }

    override fun initListener() {
        ll_div.setOnClickListener {
            if (bottomBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
        ll_bottom.setOnClickListener {  }
        btn_query.setOnClickListener {
            if (bottomBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                hideKeyboard()
                bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        iv_down.setOnClickListener {
            if (bottomBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
        btn_submit.setOnClickListener {
            if(billingList.isNotEmpty()) {
                kCommiting?.show()
                var list = ArrayList<Billing>()
                for ((k, v) in billingMap) {
                    list.add(Billing(k, v))
                }
                presenter.openBilling(list)
            } else {
                Toast.makeText(this, R.string.not_data, Toast.LENGTH_SHORT).show()
            }
        }
        btn_next.setOnClickListener {
            if (inputIsOk()) {
                Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show()
                billingAdapter.notifyDataSetChanged()
                et_money.setText("")
                et_money.requestFocus()
                hideKeyboard()
            } else {
                et_money.requestFocus()
            }
        }
        billingAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
            if (view.id == R.id.iv_remote) {
                billingList.removeAt(position)
                billingAdapter.notifyDataSetChanged()
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
        if (bottomBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            return
        }

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


    inner class BillingAdapter: BaseQuickAdapter<Billing, BaseViewHolder>(R.layout.adapter_billing, billingList) {

        override fun convert(helper: BaseViewHolder, item: Billing) {
            helper.setText(R.id.tv_num, (helper.layoutPosition + 1).toString())
            helper.setText(R.id.tv_code,item.barcode)
            helper.setText(R.id.tv_money, mContext.getString(R.string.money_f, item.amount.toString()))
            helper.addOnClickListener(R.id.iv_remote)

        }


    }

}