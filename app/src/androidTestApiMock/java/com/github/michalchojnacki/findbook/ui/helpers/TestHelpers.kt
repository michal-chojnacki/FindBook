package com.github.michalchojnacki.findbook.ui.helpers

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun wait(waitTime: WaitPeriod) {
    runBlocking { delay(waitTime.timeInMillis) }
}

enum class WaitPeriod(val timeInMillis: Long) {
    SHORT(1_000),
    REGULAR(2_000),
    LONG(3_000),
}