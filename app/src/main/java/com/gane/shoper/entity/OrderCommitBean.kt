package com.gane.shoper.entity

/**
 * 提交订单的反馈
 */
data class OrderCommitBean(
        val netstate: String? = null,
        val saledate: String? = null,
        val transdate: String? = null,
        val saleno: String? = null,
        val posno: String? = null,
        val owesum: Double? = null,
        val paystate: String? = null,
        val firstbarcodedate: String? = null,
        val shopno: String? = null,
        val transf: Int = 0,
        val discount: Int = 0,
        val salesman: String? = null,
        val saletype: String? = null,
        val paidsum: Double? = null,
        val clerkno: String? = null
)