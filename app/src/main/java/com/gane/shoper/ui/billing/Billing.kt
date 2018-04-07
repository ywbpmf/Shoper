package com.gane.shoper.ui.billing

/**
 * 开单数据的实体
 */
data class Billing(
        val barcode: String,
        val amount: Float
)

data class BillingAll(
        val supplierno: String,
        val clerkno: String,
        val orderInfo: List<Billing>
)