package com.github.michalchojnacki.findbook.ui.booklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.michalchojnacki.findbook.domain.SearchForBooksWithQueryUseCase
import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.domain.model.Result
import com.github.michalchojnacki.findbook.ui.common.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class BookListViewModel(
    private val query: String,
    private val searchForBooksWithQuery: SearchForBooksWithQueryUseCase,
    coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel() {
    private val scope = CoroutineScope(parentJob + coroutineDispatcher)
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState>
        get() = _uiState

    init {
        scope.launch {
            val result = searchForBooksWithQuery(query)
            if (result is Result.Success) {
                sendUiState(books = result.data)
            }
        }
    }

    private fun sendUiState(
        progressBarVisible: Boolean? = null,
        books: List<Book>? = null
    ) {
        val previousUiState = _uiState.value ?: UiState()
        val nextUiState = UiState(if (books != null) BookListAdapter(books) else previousUiState.adapter)
        _uiState.value = nextUiState
    }

    data class UiState(val adapter: BookListAdapter? = null)
}