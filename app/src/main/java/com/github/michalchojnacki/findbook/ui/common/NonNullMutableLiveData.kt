package com.github.michalchojnacki.findbook.ui.common

import androidx.lifecycle.MutableLiveData

class NonNullMutableLiveData<T>(initialValue: T) : MutableLiveData<T>() {
    init {
        value = initialValue
    }

    override fun getValue(): T {
        // Safe casting to not null value, because of initialization in constructor.
        return super.getValue()!!
    }
}
