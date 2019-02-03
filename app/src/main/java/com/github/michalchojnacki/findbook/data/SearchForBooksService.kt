package com.github.michalchojnacki.findbook.data

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchForBooksService {
    @GET("search/index.xml")
    fun searchForBooksWithQuery(@Query("q") query: String): Deferred<Response<BooksSearchRawModel>>
}