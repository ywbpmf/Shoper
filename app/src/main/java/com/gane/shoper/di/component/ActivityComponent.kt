package com.gane.shoper.di.component

import com.gane.shoper.di.module.ActivityModule
import com.gane.shoper.di.ActivityScope
import dagger.Component

/**
 *
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class),
        modules = arrayOf(ActivityModule::class))
interface ActivityComponent