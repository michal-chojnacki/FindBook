package com.github.michalchojnacki.findbook.data

import com.github.michalchojnacki.findbook.data.model.BookDetailsRawModel
import com.github.michalchojnacki.findbook.data.model.BooksSearchRawModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchForBooksService {
    @GET("search/index.xml")
    suspend fun searchForBooksWithQuery(@Query("q") query: String): Response<BooksSearchRawModel>

    @GET("book/show/{bookId}.xml")
    suspend fun getBookDetails(@Path("bookId") bookId: Long): Response<BookDetailsRawModel>
}