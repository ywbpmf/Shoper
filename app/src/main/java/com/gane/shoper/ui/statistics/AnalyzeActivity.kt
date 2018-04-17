package com.gane.shoper.ui.statistics

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import com.gane.shoper.R
import com.gane.shoper.app.SuperActivity
import com.gane.shoper.entity.SaleEntity
import com.gane.shoper.entity.StatisticsEntity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.ui_analyze.*
import java.util.ArrayList

/**
 * 销售分析
 */
class AnalyzeActivity : SuperActivity(), AnalyzeContract.View {


    override var presenter: AnalyzeContract.Presenter = AnalyzePresenter(this)

    private lateinit var adapter: AnalyzeAdapter

    override fun layoutId() = R.layout.ui_analyze

    override fun initView() {
        rv_list.layoutManager = LinearLayoutManager(this)
        adapter = AnalyzeAdapter()
        rv_list.adapter = adapter

        // 加载数据
        presenter.reqSalesData()

        drawLineChart()
    }

    override fun loadSalesDataError() {

    }

    override fun loadSalesDataSuccess(data: StatisticsEntity) {
        line_chart.data = bindLineData(data.list!!)
        line_chart.animateX(2500)

        var list = ArrayList<SaleEntity>()
        for (i in 0 until data.list!!.size) {
            if (data.list!![i].salesum <= 0f)
                continue
            list.add(data.list!![i])
        }
        adapter.setNewData(list)
    }

    private fun drawLineChart() {
        // 图的文本说明
        line_chart.description.text = "每月销售图"
        line_chart.description.textColor = Color.RED
        line_chart.description.textSize = 12f
        line_chart.description.isEnabled = true
        line_chart.setDrawGridBackground(false) // 不绘制区域的grid背景, 会覆盖主体背景
        line_chart.setTouchEnabled(true)
        line_chart.isDragEnabled = true
        line_chart.setScaleEnabled(false)
        line_chart.setPinchZoom(false)
        line_chart.setViewPortOffsets(10f, 0f, 10f, 0f)

        val l = line_chart.legend
        l.isEnabled = false
        line_chart.axisLeft.isEnabled = false
        line_chart.axisLeft.spaceTop = 40f
        line_chart.axisLeft.spaceBottom = 40f
        line_chart.axisRight.isEnabled = false
        line_chart.xAxis.isEnabled = false

    }

    /**
     * 每月的销售数据
     * */
    private fun bindLineData(list: List<SaleEntity>) : LineData {
        val lineData = ArrayList<Entry>()
        for (i in 0 until list.size) {
            lineData.add(Entry(i.toFloat(), list[i].salesum))
        }
        val dataSet = LineDataSet(lineData, "DataSet 1")

        dataSet.lineWidth = 0.75f // 线条的粗细
        dataSet.color = Color.parseColor("#aaffffff") // 点与点之间线条的颜色
        dataSet.setDrawValues(false) // 不绘制点的值

        // data中的点的设置
        dataSet.circleRadius = 2f
        dataSet.circleHoleRadius = 0.5f
        dataSet.setCircleColor(Color.WHITE)
        dataSet.setCircleColorHole(Color.RED)

        return LineData(dataSet)
    }



}
