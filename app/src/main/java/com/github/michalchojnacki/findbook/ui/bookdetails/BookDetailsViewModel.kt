package com.github.michalchojnacki.findbook.ui.bookdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.RequestManager
import com.github.michalchojnacki.findbook.domain.LoadBookDetailsUseCase
import com.github.michalchojnacki.findbook.domain.model.BookDetails
import com.github.michalchojnacki.findbook.domain.model.Result
import com.github.michalchojnacki.findbook.ui.common.NonNullMutableLiveData
import com.github.michalchojnacki.findbook.util.exhaustive
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch

class BookDetailsViewModel @AssistedInject constructor(
    @Assisted val requestManager: RequestManager,
    @Assisted private val bookId: Long,
    private val loadBookDetailsUseCase: LoadBookDetailsUseCase
) : ViewModel() {
    @AssistedInject.Factory
    interface Factory {
        fun create(requestManager: RequestManager, bookId: Long): BookDetailsViewModel
    }

    val uiState = NonNullMutableLiveData(UiState())

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(
                progressBarVisible = true,
                isError = false
            )
            when (val result = loadBookDetailsUseCase(bookId)) {
                is Result.Success -> uiState.value =
                    UiState(
                        bookDetails = result.data,
                        progressBarVisible = false,
                        isError = false
                    )
                is Result.Error -> uiState.value =
                    UiState(
                        bookDetails = null,
                        progressBarVisible = false,
                        isError = false
                    )
            }.exhaustive
        }
    }

    fun onRetryClick() {
        loadData()
    }

    data class UiState(
        val bookDetails: BookDetails? = null,
        val progressBarVisible: Boolean = false,
        val isError: Boolean = false
    )
}