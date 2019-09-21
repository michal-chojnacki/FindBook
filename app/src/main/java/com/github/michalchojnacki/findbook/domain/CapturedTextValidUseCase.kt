package com.github.michalchojnacki.findbook.domain

import com.github.michalchojnacki.findbook.domain.model.Result
import javax.inject.Inject

class CapturedTextValidUseCase @Inject constructor(private val searchForBooksWithQueryUseCase: SearchForBooksWithQueryUseCase) {
    suspend operator fun invoke(query: String): Boolean {
        return query.length > 3 && (searchForBooksWithQueryUseCase(query) as? Result.Success)?.data?.isNotEmpty() == true
    }
}