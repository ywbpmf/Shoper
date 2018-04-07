package com.gane.shoper.di.module

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *
 */
@Module
class ApiModule {

    @Provides
    @Singleton
    fun baseUrl() = ""


}