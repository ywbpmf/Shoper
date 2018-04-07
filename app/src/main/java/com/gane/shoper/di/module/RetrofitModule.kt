package com.gane.shoper.di.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 *
 */
@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun providersRetrofit() = Retrofit.Builder().baseUrl("").build()

}