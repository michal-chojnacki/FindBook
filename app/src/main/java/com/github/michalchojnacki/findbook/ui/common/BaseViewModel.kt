package com.github.michalchojnacki.findbook.ui.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {
    protected val parentJob = Job()

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}