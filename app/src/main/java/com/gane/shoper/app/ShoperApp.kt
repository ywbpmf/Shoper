package com.gane.shoper.app

import android.app.Application
import com.gane.shoper.entity.AppEntity

/**
 *
 */
class ShoperApp : Application() {

    companion object {

        lateinit var self: ShoperApp

        var appEntity: AppEntity = AppEntity()

    }


//    init {
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
//            ClassicsHeader(context)
//        }
//        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> ClassicsFooter(context) }
//    }

//    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        self = this
        initDagger2()
    }

    private fun initDagger2() {
//        component = DaggerAppComponent.builder()
//                .appModule(AppModule(this))
//                .build()
    }


}