package com.github.michalchojnacki.findbook.data

import okhttp3.Interceptor
import okhttp3.Response

class SigningInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (request.method() == "GET") {
            val url = chain.request().url()
                .newBuilder()
                .addQueryParameter("key", apiKey)
                .build()
            request = request.newBuilder().url(url).build()
        }
        return chain.proceed(request)
    }
}