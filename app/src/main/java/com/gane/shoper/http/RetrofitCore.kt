package com.gane.shoper.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by ywbpmf on 2018/3/15.
 */
object RetrofitCore {

    private val BASE_URL = "http://120.24.182.233:9001/api/"

    private val httpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

    private val httpService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(HttpService::class.java)

    fun getInstance() = httpService



    private val loginHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

    private val loginHttpService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(loginHttpClient)
            .build()
            .create(HttpService::class.java)

    fun getLoginInstance() = loginHttpService


}