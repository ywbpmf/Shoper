package com.gane.smartcheck.service

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.gane.smartcheck.App
import com.gane.smartcheck.ip

object HttpCore {

    val volleyQueue: RequestQueue by lazy {
        Volley.newRequestQueue(App.instance)
    }

    const val DEFAULT_IP = "119.145.110.173"

    private fun ip(): String {
        var ip = App.instance!!.ip().toLowerCase()
        if (ip.startsWith("http://"))
            ip = ip.substring("http://".length)
        return ip
    }


    private val URL_FORMAT = "http://%s:9101/api/"

    private fun url(): String {
        val url = String.format(URL_FORMAT, ip())
        return String.format(URL_FORMAT, ip())
    }

    /** 登录 */
    val LOGIN = url() + "user/login"

    /** 查询商品信息(库存中查找) 080001*/
    val QUERYALL = url() + "product/queryAll?barcode=%s&shopno=%s"

    /**
     * 获取单号
     */
    val QUERYNO = url() + "factcheck/queryno?shopno=%s"

    /** 提交盘点 */
    val COMMIT = url() + "factcheck/commit"

    /**
     * 查询商品信息（不包括库存）
     */
    val INST = url() + "product/query?barcode=%s"

}