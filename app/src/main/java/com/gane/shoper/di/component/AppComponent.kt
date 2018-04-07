package com.gane.shoper.di.component

import android.app.Application
import com.gane.shoper.di.module.ApiModule
import com.gane.shoper.di.module.AppModule
import com.gane.shoper.di.module.RetrofitModule
import dagger.Component
import javax.inject.Singleton

/**
 *
 *
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, RetrofitModule::class, ApiModule::class))
interface AppComponent {

    fun application(): Application

}