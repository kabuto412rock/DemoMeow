package com.blogspot.zongjia.demomeow.application

import android.app.Application
import com.blogspot.zongjia.demomeow.module.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class DemoMeowApplication :Application() {
    override fun onCreate() {
        super.onCreate()
        // Adding Koin modules
        startKoin {
            androidContext(this@DemoMeowApplication)
            loadKoinModules(appModules)
        }
    }
}