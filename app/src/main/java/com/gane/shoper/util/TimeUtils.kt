package com.gane.shoper.util

import java.text.SimpleDateFormat


/**
 *
 */
object TimeUtils {


    private val FORMAT_1 = "yyyy-MM-dd hh:mm:ss"

    fun formatNow() : String {
        val format = SimpleDateFormat(FORMAT_1)
        return format.format(System.currentTimeMillis())
    }

}
