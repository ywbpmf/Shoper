package com.gane.shoper.http

import com.gane.shoper.entity.*
import com.gane.shoper.ui.billing.BillingAll
import io.reactivex.Observable
import retrofit2.http.*

/**
 *
 */
interface HttpService {

    /**
     * 用户登录
     */
    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username") username: String,
              @Field("password") password: String) : Observable<BaseBean<UserLoginBean>>

    /**
     * 查询机构所有的开单码
     */
    @GET("inst/barcodes")
    fun getInstBarcodes(@Query("supplierno") supplierno: String) : Observable<BaseBean<List<InstBarcodesBean>>>

    /**
     * 提交订单
     */
    @Headers("Content-Type: application/json")
    @POST("order/commit")
    fun commitOrders(@Body billingAll: BillingAll) : Observable<BaseBean<OrderCommitBean>>

    /**
     * 订单查询
     *
     * @param clerkno 用户编号
     * @param paystate 订单支付状态 00 = 未支付  01 = 已支付
     * @param offset 偏移量
     * @param limit 查询数量
     */
    @GET("order/query")
    fun getOrderList(@Query("clerkno") clerkno: String, @Query("limit") limit: Int,
              @Query("offset") offset: Int/*, @Query("paystate") paystate: String*/) : Observable<BaseBean<List<OrderQueryBean>>>


    /**
     * 获取销售情况
     */
    @GET("statistics/query")
    fun getStatistics(@Query("clerkno") clerkno: String, @Query("begindate") begindate: String,
                      @Query("enddate") enddate: String) : Observable<BaseBean<List<OrderQueryBean>>>


}