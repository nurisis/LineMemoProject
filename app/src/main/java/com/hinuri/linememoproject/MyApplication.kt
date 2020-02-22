package com.hinuri.linememoproject

import android.app.Application
import com.hinuri.linememoproject.common.di.appModule
import com.hinuri.linememoproject.common.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(appModule, viewModelModule))
        }
    }
}