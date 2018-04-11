package com.gane.shoper.ui.main

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gane.shoper.R
import com.gane.shoper.app.SuperFragment
import com.gane.shoper.ui.statistics.AnalyzeActivity
import com.gane.shoper.ui.goods.GoodsActivity
import com.gane.shoper.ui.sales.SalesOrdersActivity
import com.gane.shoper.ui.billing.BillingActivity
import com.gane.shoper.ui.billing.PayChannelActivity
import com.gane.shoper.ui.sales_return.SalesReturnActivity
import com.gane.shoper.ui.sales_send.SalesSendActivity
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration
import kotlinx.android.synthetic.main.fm_main.*

/**
 *
 */
class MainFragment : SuperFragment(), BaseQuickAdapter.OnItemClickListener {

    private lateinit var mainAdapter: MainAdapter

    private val pages = arrayOf(BillingActivity::class.java, SalesOrdersActivity::class.java,
            SalesReturnActivity::class.java, AnalyzeActivity::class.java,
            SalesSendActivity::class.java, GoodsActivity::class.java)

    override fun layoutId() = R.layout.fm_main

    override fun initView() {
        mainAdapter = MainAdapter(context)
        mainAdapter.onItemClickListener = this
        recycler_view.adapter = mainAdapter
        recycler_view.layoutManager = GridLayoutManager(context, 2)
        recycler_view.addItemDecoration(HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.white).sizeResId(R.dimen.margin).showLastDivider().build())
        recycler_view.addItemDecoration(VerticalDividerItemDecoration.Builder(context)
                .colorResId(R.color.white).sizeResId(R.dimen.margin).showLastDivider().build())
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (pages[position] != null) startActivity(Intent(context, pages[position]))
    }

}