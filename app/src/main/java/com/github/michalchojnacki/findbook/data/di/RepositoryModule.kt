package com.github.michalchojnacki.findbook.data.di

import android.content.Context
import com.github.michalchojnacki.findbook.BuildConfig
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.data.SearchForBooksService
import com.github.michalchojnacki.findbook.data.SearchForForBooksRemoteDataSource
import com.github.michalchojnacki.findbook.data.SigningInterceptor
import com.github.michalchojnacki.findbook.domain.SearchForBooksDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

@Module(includes = [RepositoryModule.BindsModule::class])
class RepositoryModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(SigningInterceptor(BuildConfig.API_KEY))
        .followSslRedirects(true)
        .build()

    @Suppress("DEPRECATION")
    @Provides
    fun provideRetrofit(context: Context, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(context.getString(R.string.api_service_url))
        .client(client)
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()

    @Provides
    fun provideSearchForBooksService(retrofit: Retrofit): SearchForBooksService =
        retrofit.create(SearchForBooksService::class.java)

    @Module
    interface BindsModule {
        @Binds
        fun provideSearchForForBooksRemoteDataSource(searchForForBooksRemoteDataSource: SearchForForBooksRemoteDataSource): SearchForBooksDataSource
    }
}