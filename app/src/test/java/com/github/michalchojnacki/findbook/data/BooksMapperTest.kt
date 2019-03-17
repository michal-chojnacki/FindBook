package com.github.michalchojnacki.findbook.data

import org.junit.Assert
import org.junit.Test

class BooksMapperTest {
    private val fakeApiResultsProducer = FakeApiResultsProducer()
    private val booksMapper = BooksMapper()

    @Test
    fun `test mapping with happy scenario`() {
        val booksSearchRawModel = fakeApiResultsProducer.produceBooksSearchRawModel()

        val books = booksMapper.map(booksSearchRawModel)

        Assert.assertEquals(booksSearchRawModel.search!!.results!!.size, books.size)
        for (i in 0..(books.size - 1)) {
            Assert.assertEquals(booksSearchRawModel.search!!.results!![i].bestBook!!.title, books[i].title)
        }
    }

    @Test
    fun `test mapping with empty value`() {
        val booksSearchRawModel = BooksSearchRawModel().apply {
            search = BooksSearchRawModel.Search().apply {
                results = fakeApiResultsProducer.produceBooksSearchRawModel().search!!.results!!.toMutableList()
                        .apply { add(BooksSearchRawModel.Work()) }
            }
        }

        val books = booksMapper.map(booksSearchRawModel)

        Assert.assertEquals(booksSearchRawModel.search!!.results!!.size - 1, books.size)
        for (i in 0..(books.size - 1)) {
            Assert.assertEquals(booksSearchRawModel.search!!.results!![i].bestBook!!.title, books[i].title)
        }
    }
}