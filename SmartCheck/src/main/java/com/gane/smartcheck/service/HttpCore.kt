package com.gane.smartcheck.service

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.gane.smartcheck.App

object HttpCore {

    val volleyQueue: RequestQueue by lazy {
        Volley.newRequestQueue(App.instance)
    }



    private const val URL = "http://119.145.110.173:9101/api/"

    /** 登录 */
    const val LOGIN = URL + "user/login"

    /** 查询商品信息(库存中查找) 080001*/
    const val QUERYALL = URL + "product/queryAll?barcode=%s&shopno=%s"

    /**
     * 获取单号
     */
    const val QUERYNO = URL + "factcheck/queryno?shopno=%s"

    /** 提交盘点 */
    const val COMMIT = URL + "factcheck/commit"

    /**
     * 查询商品信息（不包括库存）
     */
    const val INST = URL + "product/query?barcode=%s"

}