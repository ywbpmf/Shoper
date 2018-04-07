package com.gane.shoper.entity


data class OrderQueryBean(
        var saledate: String? = null,
        var owesum: Int = -1,
        var posno: String? = null,
        var saleno: String? = null,
        var paystate: String? = null,
        var paidsum: Int = -1,
        var clerkno: String? = null,
        var shopno: String? = null,
        var discount: Int = -1
)
