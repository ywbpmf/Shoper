package com.gane.smartcheck.bean

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "t_product")
data class ProductBean(
        @PrimaryKey(autoGenerate=true)
        var id: Int = 0,
        var storemno: String? = null,
        var goodscode: String? = null,
        var changesetno: String? = null,
        var saleprice: Float = 0f,
        var goodsstate: String? = null,
        var supplierno: String? = null,
        var discount: Int = 0,
        var goodsname: String? = null,
        var model: String = "",
        var type: Int = -1,

        var barcode: String? = null,
        var goodsnum: Int = 0,

        var checkno: String = "", // 盘点单号
        var factcheckno: String = "", // 盘点批次
        var opdate: String = "", // 盘点时间
        var count: Int = 0 // 盘点需要记录的数量

) {

    constructor() : this(0)

}