package com.github.michalchojnacki.findbook.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.michalchojnacki.findbook.domain.CapturedTextValidUseCase
import com.github.michalchojnacki.findbook.ui.common.BaseViewModel
import com.github.michalchojnacki.findbook.ui.common.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

class OcrCaptureViewModel @Inject constructor(private val capturedTextValid: CapturedTextValidUseCase) :
    BaseViewModel() {

    val onValidatedTextLiveData: LiveData<Event<String>>
        get() = _onValidatedTextLiveData
    private val _onValidatedTextLiveData = MutableLiveData<Event<String>>()

    fun onTextDetected(query: String) {
        viewModelScope.launch {
            if (capturedTextValid(query)) {
                _onValidatedTextLiveData.postValue(Event(query))
            }
        }
    }
}