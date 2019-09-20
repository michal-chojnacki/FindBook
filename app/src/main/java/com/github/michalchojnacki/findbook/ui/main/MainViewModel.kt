package com.github.michalchojnacki.findbook.ui.main

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.michalchojnacki.findbook.ui.common.BaseViewModel
import com.github.michalchojnacki.findbook.ui.common.Event
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel() {
    private val _uiResultLiveData = MutableLiveData<Event<UiResult>>()
    val uiResultLiveData: LiveData<Event<UiResult>>
        get() = _uiResultLiveData

    val onEditorActionListener = object : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onShowBookClick(v)
                return true
            }
            return false
        }
    }

    fun onShowBookClick(queryEditText: TextView) {
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
