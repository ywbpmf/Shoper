package com.gane.smartcheck.bean

data class ProductBean(
        var storemno: String? = null,
        var goodscode: String? = null,
        var changesetno: String? = null,
        var saleprice: Float = 0f,
        var goodsstate: String? = null,
        var supplierno: String? = null,
        var discount: Int = 0,
        var goodsname: String? = null,
        var model: Int = -1,
        var type: Int = -1,
        var barcode: String? = null,
        var goodsnum: Int = 0
)