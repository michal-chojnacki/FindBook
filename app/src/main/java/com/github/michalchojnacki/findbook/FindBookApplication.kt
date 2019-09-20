package com.github.michalchojnacki.findbook

import android.app.Application
import com.github.michalchojnacki.findbook.ui.di.DaggerApplicationComponent
import com.github.michalchojnacki.findbook.ui.di.InjectorProvider

class FindBookApplication : Application(), InjectorProvider {

    override val component by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }
}