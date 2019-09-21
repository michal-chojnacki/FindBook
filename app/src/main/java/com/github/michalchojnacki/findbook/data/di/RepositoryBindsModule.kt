package com.github.michalchojnacki.findbook.data.di

import com.github.michalchojnacki.findbook.data.SearchForBooksRepository
import com.github.michalchojnacki.findbook.data.SearchForForBooksLocalDataSource
import com.github.michalchojnacki.findbook.data.SearchForForBooksRemoteDataSource
import com.github.michalchojnacki.findbook.domain.SearchForBooksDataSource
import dagger.Binds
import dagger.Module

@Module
interface RepositoryBindsModule {
    @Binds
    @Remote
    fun provideSearchForForBooksRemoteDataSource(searchForBooksRepository: SearchForForBooksRemoteDataSource): SearchForBooksDataSource

    @Binds
    @Local
    fun provideSearchForForBooksLocalDataSource(searchForForBooksLocalDataSource: SearchForForBooksLocalDataSource): SearchForBooksDataSource

    @Binds
    fun provideSearchForBooksRepository(searchForBooksRepository: SearchForBooksRepository): SearchForBooksDataSource
}