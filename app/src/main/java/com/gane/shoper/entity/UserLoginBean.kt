package com.gane.shoper.entity

/**
 * 登录返回的实体
 */
data class UserLoginBean(

    var token: String? = null,
    var user:User? = null,
    var insts: List<Insts>? = null

)

/**
 * 用户信息
 */
data class User (
        var seftimpowerflag: String? = null,
        var clerkname: String? = null,
        var multiloginflag: String? = null,
        var logflag: String?= null,
        var state: String? = null,
        var clerkno: String? = null,
        var shopno: String? = null,
        var clerkattr: String? = null
)


data class Insts (
    var clerkrole: String? = null,
    var state: Int = -1,
    var updateddate: String? = null,
    var createddate: String? = null,
    var clerkno: String? = null,
    var supplierno: String? = null,
    var inst: Inst? = null
)

/**
 * 用户绑定的机构信息
 */
data class Inst (
        var contactfax: String? = null,
        var servicephone: String? = null,
        var goodsmark: String? = null,
        var remark: String? = null,
        var achieve: String? = null,
        var tradeno: String? = null,
        var trademarkname: String? = null,
        var contactman: String? = null,
        var ipaddress: String? = null,
        var area: String? = null,
        var supplierstate: String? = null,
        var singlegoodsnum: Int = -1,
        var supplierztno: String? = null,
        var hiretaxline: Int = -1,
        var operatdate: String? = null,
        var licence: String? = null,
        var bankaccout: String? = null,
        var bankname: String? = null,
        var suppliername: String? = null,
        var ower: String? = null,
        var supplierattr: String? = null,
        var hiretaxcode: String? = null,
        var postcode: String? = null,
        var hiretaxrate: Int = -1,
        var companyattr: String? = null,
        var operator: String? = null,
        var supplierno: String? = null,
        var balancecardno: String? = null,
        var cancelflag: Int = -1,
        var fallrate: Int = -1,
        var storerm: String? = null,
        var orderdays: Int = -1,
        var shoppecate: String? = null,
        var email: String? = null,
        var address: String? = null,
        var suppliercode: String? = null,
        var contactphone: String? = null,
        var invoicetitle: String? = null,
        var taxno: String? = null
)