package com.gane.shoper.di.module

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *
 */
@Module
class AppModule(private val application: Application) {


    @Provides
    @Singleton
    fun providersApplication() = application

    @Provides
    @Singleton
    fun providersResources() = application.resources


}