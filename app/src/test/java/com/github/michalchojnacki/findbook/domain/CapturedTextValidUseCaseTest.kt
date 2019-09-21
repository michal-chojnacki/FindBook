package com.github.michalchojnacki.findbook.domain

import com.github.michalchojnacki.findbook.domain.model.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class CapturedTextValidUseCaseTest {
    private val mockSearchForBooksDataSource: SearchForBooksDataSource = mockk()
    private val capturedTextValidUseCase =
        CapturedTextValidUseCase(SearchForBooksWithQueryUseCase(mockSearchForBooksDataSource))

    @Test
    fun `test when data is valid`() = runBlocking {
        val testQuery = "test query"
        coEvery { mockSearchForBooksDataSource.searchForBooksWithQuery(testQuery) }.returns(
            Result.Success(
                listOf(mockk())
            )
        )

        val result = capturedTextValidUseCase(testQuery)

        assertTrue(result)
    }

    @Test
    fun `test when data is not valid`() = runBlocking {
        val testQuery = "test query"
        coEvery { mockSearchForBooksDataSource.searchForBooksWithQuery(testQuery) }.returns(
            Result.Success(
                listOf()
            )
        )

        val result = capturedTextValidUseCase(testQuery)

        assertFalse(result)
    }

    @Test
    fun `test when data there is error during fetching data`() = runBlocking {
        val testQuery = "test query"
        coEvery { mockSearchForBooksDataSource.searchForBooksWithQuery(testQuery) }.returns(
            Result.Error(
                IOException("Fake exception!")
            )
        )

        val result = capturedTextValidUseCase(testQuery)

        assertFalse(result)
    }
}