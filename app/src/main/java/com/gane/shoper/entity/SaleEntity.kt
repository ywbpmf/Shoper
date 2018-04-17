package com.gane.shoper.entity

/**
 * 销售数据实体
 */
data class StatisticsEntity(
        var total: SaleTotalEntity? = null,
        var list: List<SaleEntity>? = null
)

data class SaleTotalEntity(
        var salesum: Float = 0f,
        var paidsum: Float = 0f
)

data class SaleEntity(
        var day: String? = null,
        var clerkno: String? = null,
        var salesum: Float = 0f,
        var paidsum: Float = 0f
)