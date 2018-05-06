package com.gane.smartcheck.bean

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "t_product")
data class ProductBean(
        @PrimaryKey(autoGenerate=true)
        var id: Int = 0,

        var storemname: String? = null, // 库区
        var suppliername: String? = null, // 供应商名字
        var saleprice: Float = 0f, // 售价
        var supplierno: String? = null, // 供应商编号
        var promadprice: Float = 0f, // 促销价
        var goodsname: String? = null, // 商品名称
        var model: String = "", // 规格
        var barcode: String? = null, // 条码
        var goodsnum: Int = 0, // 商品数量
        var rationflag: String = "", // 促销状态


        var checkno: String = "", // 盘点单号
        var factcheckno: String = "", // 盘点批次
        var opdate: String = "", // 盘点时间
        var count: Int = 0 // 盘点需要记录的数量

) {

    constructor() : this(0)

}