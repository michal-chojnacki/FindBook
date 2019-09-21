package com.github.michalchojnacki.findbook.ui.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michalchojnacki.findbook.domain.SearchForBooksWithQueryUseCase
import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.domain.model.Result
import com.github.michalchojnacki.findbook.ui.common.NonNullMutableLiveData
import com.github.michalchojnacki.findbook.util.exhaustive
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch

class BookListViewModel @AssistedInject constructor(
    @Assisted private val query: String,
    private val searchForBooksWithQuery: SearchForBooksWithQueryUseCase
) : ViewModel() {
    val uiState = NonNullMutableLiveData(UiState())

    @AssistedInject.Factory
    interface Factory {
        fun create(query: String): BookListViewModel
    }

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(progressBarVisible = true, error = false)
            when (val result = searchForBooksWithQuery(query)) {
                is Result.Success -> uiState.value =
                    UiState(books = result.data, progressBarVisible = false, error = false)
                is Result.Error -> uiState.value =
                    UiState(books = emptyList(), progressBarVisible = false, error = true)
            }.exhaustive
        }
    }

    fun onRetryClick() {
        loadData()
    }

    data class UiState(
        val books: List<Book> = emptyList(),
        val progressBarVisible: Boolean = false,
        val error: Boolean = false
    )
}