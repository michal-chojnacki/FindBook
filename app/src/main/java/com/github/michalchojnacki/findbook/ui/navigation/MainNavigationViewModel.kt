package com.github.michalchojnacki.findbook.ui.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.michalchojnacki.findbook.ui.common.BaseViewModel
import com.github.michalchojnacki.findbook.ui.common.Event
import javax.inject.Inject

class MainNavigationViewModel @Inject constructor() : BaseViewModel() {
    val uiResultLiveData: LiveData<Event<UiResult>>
        get() = _uiResultLiveData

    private val _uiResultLiveData = MutableLiveData<Event<UiResult>>(Event(UiResult.ShowOcrScanner))

    fun showOcrScanner() {
        _uiResultLiveData.postValue(Event(UiResult.ShowOcrScanner))
    }

    fun showBookList(query: String) {
        _uiResultLiveData.postValue(Event(UiResult.ShowBookList(query)))
    }

    fun showTypingSearch(becauseOfNoCameraPermission: Boolean = false) {
        _uiResultLiveData.postValue(Event(UiResult.ShowTypingSearch(becauseOfNoCameraPermission = becauseOfNoCameraPermission)))
    }

    sealed class UiResult {
        object ShowOcrScanner : UiResult()
        data class ShowBookList(val query: String) : UiResult()
        data class ShowTypingSearch(val becauseOfNoCameraPermission: Boolean) : UiResult()
    }
}
