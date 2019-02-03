package com.github.michalchojnacki.findbook

import android.content.Context
import io.mockk.mockk
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.test.KoinTest
import org.koin.test.checkModules

class KoinModulesTest : KoinTest {
    @Test
    fun checkKoinModules() {
        checkModules(listOf(module {
            single { mockk<Context>() }
        }, *appModules.toTypedArray()))
    }
}
