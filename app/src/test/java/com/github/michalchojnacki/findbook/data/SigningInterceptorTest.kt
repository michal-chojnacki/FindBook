package com.github.michalchojnacki.findbook.data

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Assert
import org.junit.Before
import org.junit.Test

private const val FAKE_API_URL = "http://www.fakeapi.com"
private const val FAKE_API_KEY = "fake_api_key"

class SigningInterceptorTest {
    private val signingInterceptor = SigningInterceptor(FAKE_API_KEY)
    private val requestSlot = slot<Request>()
    private val mockInterceptorChain = mockk<Interceptor.Chain>()
    private lateinit var fakeRequest: Request

    @Before
    fun setUp() {
        coEvery { mockInterceptorChain.request() }.answers { fakeRequest }
        coEvery { mockInterceptorChain.proceed(capture(requestSlot)) }.returns(mockk())
    }

    @Test
    fun `test adding api key param on GET request`() {
        fakeRequest = Request.Builder().url(FAKE_API_URL).method("GET", null).build()

        signingInterceptor.intercept(mockInterceptorChain)

        coVerify(exactly = 1) { mockInterceptorChain.proceed(any()) }
        Assert.assertEquals(FAKE_API_KEY, requestSlot.captured.url().queryParameterValue(0))
        Assert.assertEquals(1, requestSlot.captured.url().querySize())
    }

    @Test
    fun `test not adding api key param on not GET request`() {
        fakeRequest = Request.Builder().url(FAKE_API_URL).method("POST", mockk()).build()

        signingInterceptor.intercept(mockInterceptorChain)

        coVerify(exactly = 1) { mockInterceptorChain.proceed(any()) }
        Assert.assertEquals(0, requestSlot.captured.url().querySize())
    }
}