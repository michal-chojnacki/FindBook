package com.github.michalchojnacki.findbook.data.di

import com.github.michalchojnacki.findbook.data.SearchForBooksService
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module(includes = [RepositoryBindsModule::class])
interface RepositoryModule {

    @Binds
    @Reusable
    fun provideSearchForBooksService(mockSearchForBooksService: MockSearchForBooksService): SearchForBooksService
}