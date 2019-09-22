package com.github.michalchojnacki.findbook.domain

import com.github.michalchojnacki.findbook.data.di.MockSearchForBooksService
import com.github.michalchojnacki.findbook.di.DaggerTestAppComponent
import com.github.michalchojnacki.findbook.domain.model.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class CapturedTextValidUseCaseTest {
    @Inject
    lateinit var mockSearchForBooksService: MockSearchForBooksService
    @Inject
    lateinit var capturedTextValidUseCase: CapturedTextValidUseCase

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
    }

    @Test
    fun `test when data is valid`() = runBlocking {
        val testQuery = "test query"
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(testQuery) }.returns(
            Response.success(mockSearchForBooksService.successfulResponseBody)
        )

        val result = capturedTextValidUseCase(testQuery)

        assertTrue(result)
    }

    @Test
    fun `test when data is not valid`() = runBlocking {
        val testQuery = "test query"
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(testQuery) }.returns(
            Response.success(mockSearchForBooksService.emptyResponseBody)
        )

        val result = capturedTextValidUseCase(testQuery)

        assertFalse(result)
    }

    @Test
    fun `test when data there is error during fetching data`() = runBlocking {
        val testQuery = "test query"
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(testQuery) }.answers {
            throw IOException("Fake exception!")
        }

        val result = capturedTextValidUseCase(testQuery)

        assertFalse(result)
    }
}