package com.gane.smartcheck.bean

/**
 * 商品
 * 展示名称、库区名称、数量

每扫描一个，数量加1

可手动加减、输入数量
 */
data class Goods(
        // 商品ID
        var id: String? = null,
        // 商品名称
        var name: String? = null,
        // 仓库名称
        var house: String? = null,
        // 商品数量
        var number: Int = 0

)