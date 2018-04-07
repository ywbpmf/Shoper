package com.gane.shoper.di.module

import android.app.Activity
import android.content.Context
import com.gane.shoper.di.ActivityContext
import com.gane.shoper.di.ActivityScope
import dagger.Module
import dagger.Provides

/**
 *
 */
@Module
class ActivityModule(private val activity: Activity) {

    @ActivityScope
    @Provides
    @ActivityContext
    fun providersContext(): Context = activity

}