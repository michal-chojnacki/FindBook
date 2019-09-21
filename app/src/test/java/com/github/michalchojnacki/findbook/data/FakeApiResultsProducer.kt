package com.github.michalchojnacki.findbook.data

import org.simpleframework.xml.core.Persister

class FakeApiResultsProducer {
    private val serializer = Persister()

    fun produceBooksSearchRawModel(): BooksSearchRawModel {
        val fakeResponseAsString =
            javaClass.classLoader.getResourceAsStream("fakeApi/books_successful_response.xml")
        return serializer.read(BooksSearchRawModel::class.java, fakeResponseAsString)
    }
}