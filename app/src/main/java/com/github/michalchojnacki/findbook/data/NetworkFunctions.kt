package com.github.michalchojnacki.findbook.data

import com.github.michalchojnacki.findbook.domain.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

/**
 * Wrap a suspending API [call] in try/catch. In case an exception is thrown, a [Result.Error] is
 * created based on the [errorMessage].
 */
suspend fun <T : Any> safeApiCall(coroutineDispatcher: CoroutineDispatcher, call: suspend () -> Result<T>, errorMessage: String): Result<T> {
    return try {
        withContext(context = coroutineDispatcher) { call() }
    } catch (e: Exception) {
        // An exception was thrown when calling the API so we're converting this to an IOException
        Result.Error(IOException(errorMessage, e))
    }
}

suspend fun <T : Any> retryIO(
        times: Int = 3,
        initialDelay: Long = 100, // 0.1 second
        maxDelay: Long = 1000,    // 1 second
        factor: Double = 2.0,
        block: suspend () -> Response<T>): Response<T> {
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            val result = block()
            if (result.isSuccessful) {
                return result
            }
        } catch (e: IOException) {
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block()
}
