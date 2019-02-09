package com.github.michalchojnacki.findbook

import com.github.michalchojnacki.findbook.data.BooksMapper
import com.github.michalchojnacki.findbook.data.SearchForBooksService
import com.github.michalchojnacki.findbook.data.SearchForForBooksRemoteDataSource
import com.github.michalchojnacki.findbook.data.SigningInterceptor
import com.github.michalchojnacki.findbook.domain.SearchForBooksDataSource
import com.github.michalchojnacki.findbook.domain.SearchForBooksWithQueryUseCase
import com.github.michalchojnacki.findbook.ui.booklist.BookListViewModel
import com.github.michalchojnacki.findbook.ui.main.MainViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

private val appModule = module {
    single {
        CoroutinesDispatcherProvider(
            Dispatchers.Main,
            Dispatchers.Default,
            Dispatchers.IO
        )
    }
}

private val repositoryModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(SigningInterceptor(BuildConfig.API_KEY))
            .followSslRedirects(true)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.api_service_url))
            .client(get())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
    single { get<Retrofit>().create(SearchForBooksService::class.java) }
}

private val searchForBooksModule = module {
    single { BooksMapper() }
    single<SearchForBooksDataSource> {
        SearchForForBooksRemoteDataSource(
            get(),
            get(),
            get<CoroutinesDispatcherProvider>().io
        )
    }
    single { SearchForBooksWithQueryUseCase(get(), get<CoroutinesDispatcherProvider>().computation) }
}

private val bookListModule = module {
    viewModel { (query: String) -> BookListViewModel(query, get(), get<CoroutinesDispatcherProvider>().main) }
}

private val mainViewModule = module {
    viewModel { MainViewModel() }
}

val appModules = listOf(appModule, bookListModule, mainViewModule, repositoryModule, searchForBooksModule)