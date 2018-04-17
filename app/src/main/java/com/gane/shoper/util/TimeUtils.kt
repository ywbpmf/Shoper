package com.gane.shoper.util

import java.text.SimpleDateFormat
import java.util.*


/**
 *
 */
object TimeUtils {


    private val FORMAT_1 = "yyyy-MM-dd hh:mm:ss"

    fun formatNow() : String {
        val format = SimpleDateFormat(FORMAT_1)
        return format.format(System.currentTimeMillis())
    }

    fun format(format: String, timeMs: Long) : String {
        val format = SimpleDateFormat(format)
        return format.format(timeMs)
    }
    fun parse(format: String, timeText: String) : Date {
        val format = SimpleDateFormat(format)
        return format.parse(timeText)
    }

    /**
     * 获取每月的开始
     * 只返回  yyyy-MM-dd
     */
    fun getMonthStartDayText(timeMs: Long): String {
        val now = Calendar.getInstance()
        now.timeInMillis = timeMs
        now.set(Calendar.DAY_OF_MONTH, now.getActualMinimum(Calendar.DAY_OF_MONTH))
        return format("yyyy-MM-dd", now.timeInMillis)
    }

    /**
     * 获取每月的最后一天
     * 只返回  yyyy-MM-dd
     */
    fun getMonthLastDayText(timeMs: Long): String {
        val now = Calendar.getInstance()
        now.timeInMillis = timeMs
        now.set(Calendar.DAY_OF_MONTH, now.getActualMaximum(Calendar.DAY_OF_MONTH))
        return format("yyyy-MM-dd", now.timeInMillis)
    }

    fun getWeekText(dateText: String): String {
        val now = Calendar.getInstance()
        now.time = parse("yyyy-MM-dd", dateText)
        when (now.get(Calendar.DAY_OF_WEEK)) {
            1 -> return "星期天"
            2 -> return "星期一"
            3 -> return "星期二"
            4 -> return "星期三"
            5 -> return "星期四"
            6 -> return "星期五"
            7 -> return "星期六"
        }
        return ""
    }

}
