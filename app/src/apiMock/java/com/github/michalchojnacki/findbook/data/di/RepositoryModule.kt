package com.github.michalchojnacki.findbook.data.di

import com.github.michalchojnacki.findbook.data.BooksSearchRawModel
import com.github.michalchojnacki.findbook.data.SearchForBooksService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import org.simpleframework.xml.core.Persister
import retrofit2.Response
import java.io.InputStream

@Module(includes = [RepositoryBindsModule::class])
class RepositoryModule {

    @Provides
    @Reusable
    fun provideSearchForBooksService() = object : SearchForBooksService {
        override suspend fun searchForBooksWithQuery(query: String): Response<BooksSearchRawModel> {
            val serializer = Persister()
            val response = serializer.read(
                BooksSearchRawModel::class.java,
                getResponse("fakeApi/books_successful_response.xml")
            )
            return Response.success(response)
        }
    }

    private fun getResponse(path: String): InputStream {
        return this.javaClass.classLoader.getResourceAsStream(path)
    }
}