package com.github.michalchojnacki.findbook.data

import org.junit.Assert
import org.junit.Test
import org.simpleframework.xml.core.Persister

class BooksMapperTest {
    private val serializer = Persister()
    private val booksMapper = BooksMapper()

    @Test
    fun `test mapping with happy scenario`() {
        val booksSearchRawModel = produceBooksSearchRawModel()

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
                results = produceBooksSearchRawModel().search!!.results!!.toMutableList()
                    .apply { add(BooksSearchRawModel.Work()) }
            }
        }

        val books = booksMapper.map(booksSearchRawModel)

        Assert.assertEquals(booksSearchRawModel.search!!.results!!.size - 1, books.size)
        for (i in 0..(books.size - 1)) {
            Assert.assertEquals(booksSearchRawModel.search!!.results!![i].bestBook!!.title, books[i].title)
        }
    }

    private fun produceBooksSearchRawModel(): BooksSearchRawModel {
        val fakeResponseAsString = javaClass.classLoader.getResourceAsStream("books_response.xml")
        return serializer.read(BooksSearchRawModel::class.java, fakeResponseAsString)
    }
}