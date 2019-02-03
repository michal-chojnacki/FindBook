package com.github.michalchojnacki.findbook.ui.main

import androidx.lifecycle.ViewModel
import com.github.michalchojnacki.findbook.domain.SearchForBooksWithQueryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val searchForBooksWithQuery: SearchForBooksWithQueryUseCase,
    coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val parentJob = Job()
    private val scope = CoroutineScope(parentJob + coroutineDispatcher)

    fun searchForBook(query: String) {
        scope.launch {
            val result = searchForBooksWithQuery(query)
            result.toString()
        }
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
