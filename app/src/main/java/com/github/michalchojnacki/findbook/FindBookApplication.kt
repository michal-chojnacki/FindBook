package com.github.michalchojnacki.findbook

import android.app.Application
import org.koin.android.ext.android.startKoin

class FindBookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModules)
    }
}