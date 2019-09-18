package com.github.michalchojnacki.findbook.ui.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michalchojnacki.findbook.domain.SearchForBooksWithQueryUseCase
import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.domain.model.Result
import com.github.michalchojnacki.findbook.ui.common.NonNullMutableLiveData
import kotlinx.coroutines.launch

class BookListViewModel(
    private val query: String,
    private val searchForBooksWithQuery: SearchForBooksWithQueryUseCase
) : ViewModel() {
    val uiState = NonNullMutableLiveData(UiState())

    init {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(progressBarVisible = true)
            val result = searchForBooksWithQuery(query)
            if (result is Result.Success) {
                uiState.value = uiState.value.copy(books = result.data, progressBarVisible = false)
            }
        }
    }

    data class UiState(val books: List<Book> = emptyList(), val progressBarVisible: Boolean = false)
}