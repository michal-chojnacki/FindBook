package com.github.michalchojnacki.findbook.ui.booklist

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.RequestManager
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.domain.SearchForBooksWithQueryUseCase
import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.domain.model.Result
import com.github.michalchojnacki.findbook.ui.common.NonNullMutableLiveData
import com.github.michalchojnacki.findbook.util.exhaustive
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch

class BookListViewModel @AssistedInject constructor(
    @Assisted val requestManager: RequestManager,
    @Assisted private val query: String,
    private val searchForBooksWithQuery: SearchForBooksWithQueryUseCase
) : ViewModel() {
    val uiState = NonNullMutableLiveData(UiState())

    @AssistedInject.Factory
    interface Factory {
        fun create(requestManager: RequestManager, query: String): BookListViewModel
    }

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(
                progressBarVisible = true,
                errorRes = null,
                retryPossible = false
            )
            when (val result = searchForBooksWithQuery(query)) {
                is Result.Success -> uiState.value =
                    UiState(books = result.data,
                        progressBarVisible = false,
                        errorRes = R.string.book_list_empty_error.takeIf { result.data.isEmpty() },
                        retryPossible = false
                    )
                is Result.Error -> uiState.value =
                    UiState(
                        books = emptyList(),
                        progressBarVisible = false,
                        errorRes = R.string.book_list_error,
                        retryPossible = true
                    )
            }.exhaustive
        }
    }

    fun onRetryClick() {
        loadData()
    }

    data class UiState(
        val books: List<Book> = emptyList(),
        val progressBarVisible: Boolean = false,
        @StringRes val errorRes: Int? = null,
        val retryPossible: Boolean = false
    )
}