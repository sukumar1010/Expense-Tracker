package com.sukumar.et.ETData

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ExtApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ExtApp)
            modules(ExtModule)
        }
    }

}