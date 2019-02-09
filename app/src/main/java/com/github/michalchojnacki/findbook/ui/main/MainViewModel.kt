package com.github.michalchojnacki.findbook.ui.main

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.michalchojnacki.findbook.ui.common.BaseViewModel
import com.github.michalchojnacki.findbook.ui.common.Event

class MainViewModel : BaseViewModel() {
    private val _uiResultLiveData = MutableLiveData<Event<UiResult>>()
    val uiResultLiveData: LiveData<Event<UiResult>>
        get() = _uiResultLiveData

    fun onShowBookClick(queryEditText: EditText) {
        queryEditText.text.toString().takeIf { it.isNotBlank() }?.let {
            _uiResultLiveData.postValue(Event(UiResult.ShowBookList(it)))
        }
    }

    fun onSearchForOcrClick() {
        _uiResultLiveData.postValue(Event(UiResult.ShowOcrScanner))
    }

    sealed class UiResult {
        object ShowOcrScanner : UiResult()
        data class ShowBookList(val query: String) : UiResult()
    }
}
