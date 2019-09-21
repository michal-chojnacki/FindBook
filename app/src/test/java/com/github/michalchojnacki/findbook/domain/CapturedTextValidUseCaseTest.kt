package com.github.michalchojnacki.findbook.domain

import com.github.michalchojnacki.findbook.di.DaggerTestAppComponent
import com.github.michalchojnacki.findbook.domain.model.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException
import javax.inject.Inject

class CapturedTextValidUseCaseTest {
    private val mockSearchForBooksDataSource: SearchForBooksDataSource = mockk()
    @Inject
    lateinit var capturedTextValidUseCase: CapturedTextValidUseCase

    @Before
    fun setUp() {
        DaggerTestAppComponent.factory().create(mockSearchForBooksDataSource).inject(this)
    }

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