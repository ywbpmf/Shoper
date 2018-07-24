package com.gane.shoper.http

import com.gane.shoper.app.ShoperApp
import com.gane.shoper.ext.token
import okhttp3.Interceptor
import okhttp3.Response


/**
 *
 *
 */
class TokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val request = original .newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", ShoperApp.self.token())
                .method(original .method(), original .body())
                .build()
        return chain.proceed(request)
    }

}