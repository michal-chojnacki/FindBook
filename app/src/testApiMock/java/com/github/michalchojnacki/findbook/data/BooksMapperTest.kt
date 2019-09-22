package com.github.michalchojnacki.findbook.data

import com.github.michalchojnacki.findbook.data.di.MockSearchForBooksService
import org.junit.Assert
import org.junit.Test

class BooksMapperTest {
    private val fakeSearchForBooksService = MockSearchForBooksService()
    private val booksMapper = BooksMapper()

    @Test
    fun `test mapping with happy scenario`() {
        val booksSearchRawModel = fakeSearchForBooksService.successfulResponseBody

        val books = booksMapper.map(booksSearchRawModel)

        Assert.assertEquals(booksSearchRawModel.search!!.results!!.size, books.size)
        for (i in books.indices) {
            Assert.assertEquals(
                booksSearchRawModel.search!!.results!![i].bestBook!!.title,
                books[i].title
            )
        }
    }

    @Test
    fun `test mapping with empty value`() {
        val booksSearchRawModel = BooksSearchRawModel().apply {
            search = BooksSearchRawModel.Search().apply {
                results =
                    fakeSearchForBooksService.successfulResponseBody.search!!.results!!.toMutableList()
                        .apply { add(BooksSearchRawModel.Work()) }
            }
        }

        val books = booksMapper.map(booksSearchRawModel)

        Assert.assertEquals(booksSearchRawModel.search!!.results!!.size - 1, books.size)
        for (i in books.indices) {
            Assert.assertEquals(
                booksSearchRawModel.search!!.results!![i].bestBook!!.title,
                books[i].title
            )
        }
    }
}