package com.github.michalchojnacki.findbook.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchForBooksService {
    @GET("search/index.xml")
    suspend fun searchForBooksWithQuery(@Query("q") query: String): Response<BooksSearchRawModel>
}