package com.gane.shoper.ui.statistics

import android.graphics.Color
import com.gane.shoper.R
import com.gane.shoper.app.SuperActivity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.ui_analyze.*
import java.util.ArrayList

/**
 * 销售分析
 */
class AnalyzeActivity : SuperActivity() {


    override fun layoutId() = R.layout.ui_analyze

    override fun initView() {


        drawLineChart()
    }

    private fun drawLineChart() {
        // 图的文本说明
        line_chart.description.text = "每月销售图"
        line_chart.description.textColor = Color.RED
        line_chart.description.textSize = 12f
        line_chart.description.isEnabled = true

        line_chart.setDrawGridBackground(false) // 不绘制区域的grid背景, 会覆盖主体背景

        // enable touch gestures
        line_chart.setTouchEnabled(true)

        // enable scaling and dragging
        line_chart.isDragEnabled = true
        line_chart.setScaleEnabled(false)

        // if disabled, scaling can be done on x- and y-axis separately
        line_chart.setPinchZoom(false)


        // set custom chart offsets (automatic offset calculation is hereby disabled)
        line_chart.setViewPortOffsets(10f, 0f, 10f, 0f)

        // add data
        line_chart.data = bindLineData()

        // get the legend (only possible after setting data)
        val l = line_chart.legend
        l.isEnabled = false

        line_chart.axisLeft.isEnabled = false
        line_chart.axisLeft.spaceTop = 40f
        line_chart.axisLeft.spaceBottom = 40f
        line_chart.axisRight.isEnabled = false

        line_chart.xAxis.isEnabled = false

        // animate calls invalidate()...
        line_chart.animateX(2500)
    }

    /**
     * 每月的销售数据
     * */
    private fun bindLineData() : LineData {
        val lineData = ArrayList<Entry>()
        for (i in 0..30) {
            val y = (Math.random() * 10000).toFloat() + 100
            lineData.add(Entry(i.toFloat(), y))
        }
        val dataSet = LineDataSet(lineData, "DataSet 1")

        dataSet.lineWidth = 0.75f // 线条的粗细
        dataSet.color = Color.parseColor("#aaffffff") // 点与点之间线条的颜色
        dataSet.setDrawValues(false) // 不绘制点的值

        // data中的点的设置
        dataSet.circleRadius = 4f
        dataSet.circleHoleRadius = 2.5f
        dataSet.setCircleColor(Color.WHITE)
        dataSet.setCircleColorHole(Color.RED)

        return LineData(dataSet)
    }



}
