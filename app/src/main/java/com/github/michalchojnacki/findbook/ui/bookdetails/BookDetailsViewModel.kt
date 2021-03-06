package com.github.michalchojnacki.findbook.ui.bookdetails

import androidx.annotation.ColorRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.RequestManager
import com.github.michalchojnacki.findbook.domain.LoadBookDetailsUseCase
import com.github.michalchojnacki.findbook.domain.model.BookDetails
import com.github.michalchojnacki.findbook.domain.model.Result
import com.github.michalchojnacki.findbook.ui.common.Event
import com.github.michalchojnacki.findbook.ui.common.ModelExts.averageRatingTextColor
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

    val onPictureLoaded : (Result<Unit>) -> Unit = {
        uiState.value = uiState.value.copy(pictureLoaded = true)
    }
    val imageUrl : LiveData<String> get() = _imageUrl
    private val _imageUrl = NonNullMutableLiveData("")

    val uiState = NonNullMutableLiveData(UiState())
    val uiResultLiveData: LiveData<Event<UiResult>>
        get() = _uiResultLiveData
    private val _uiResultLiveData = MutableLiveData<Event<UiResult>>()

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
                is Result.Success -> {
                    uiState.value = UiState(
                            bookDetails = result.data,
                            progressBarVisible = false,
                            isError = false
                        )
                    _imageUrl.postValue(result.data.imageUrl)
                }
                is Result.Error -> {
                    uiState.value = UiState(
                            bookDetails = null,
                            progressBarVisible = false,
                            isError = true
                        )
                    _imageUrl.postValue("")
                }
            }.exhaustive
        }
    }

    fun onRetryClick() {
        loadData()
    }

    fun showReviews(bookDetails: BookDetails?) {
        bookDetails ?: return
        _uiResultLiveData.postValue(Event(UiResult.ShowReviews(bookDetails)))

    }

    data class UiState(
        val bookDetails: BookDetails? = null,
        val progressBarVisible: Boolean = false,
        val isError: Boolean = false,
        val pictureLoaded: Boolean = false
    ) {
        val bookTitle: String? get() = bookDetails?.title
        val bookAuthors: String? get() = bookDetails?.authors?.joinToString { it }
        val publicationYear: String? get() = bookDetails?.originalPublicationYear?.toString()
        val hasPublicationYear: Boolean get() = bookDetails?.originalPublicationYear != null
        val averageRating: String? get() = bookDetails?.averageRating?.toString()
        @get:ColorRes
        val averageRatingTextColor: Int?
            get() = bookDetails?.averageRating?.averageRatingTextColor
        val ratingsCount: String? get() = bookDetails?.ratingsCount?.let { "(${it})" }
        val bookDescription: String? get() = bookDetails?.description
        val showContent : Boolean get() =  !isError && !progressBarVisible
    }

    sealed class UiResult {
        data class ShowReviews(val bookDetails: BookDetails) : UiResult()
    }
}