
package com.gane.shoper.ui.sales

import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gane.shoper.R
import com.gane.shoper.app.SuperActivity
import com.gane.shoper.entity.OrderQueryBean
import kotlinx.android.synthetic.main.ui_sales_orders.*

/**
 * 销售单
 */
class SalesOrdersActivity : SuperActivity(), SalesOrdersContract.View {

    override var presenter: SalesOrdersContract.Presenter = SalesOrdersPresenter(this)

    private lateinit var adapter: OrdersAdapter

    override fun layoutId() = R.layout.ui_sales_orders

    override fun initView() {
        adapter = OrdersAdapter()
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter



        presenter.loadSalesOrders()
    }

    override fun initListener() {
//        adapter.setOnLoadMoreListener(object: BaseQuickAdapter.RequestLoadMoreListener {
//            override fun onLoadMoreRequested() {
//
//            }
//
//        }, recycler_view)
//        adapter.setEnableLoadMore(false)
    }

    override fun loadDataSuccess(data: List<OrderQueryBean>) {
        adapter.setNewData(data)
    }

    override fun loadDataError() {

    }

    override fun loadMoreEnabled(enabled: Boolean) {
        adapter.setEnableLoadMore(enabled)
    }


}