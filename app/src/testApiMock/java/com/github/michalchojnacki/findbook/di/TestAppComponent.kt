package com.github.michalchojnacki.findbook.di

import com.github.michalchojnacki.findbook.data.SearchForBooksRemoteDataSourceTest
import com.github.michalchojnacki.findbook.data.di.RepositoryModule
import com.github.michalchojnacki.findbook.domain.CapturedTextValidUseCaseTest
import com.github.michalchojnacki.findbook.ui.di.AssistedInjectModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AssistedInjectModule::class, RepositoryModule::class])
interface TestAppComponent {

    fun inject(test: SearchForBooksRemoteDataSourceTest)

    fun inject(test: CapturedTextValidUseCaseTest)
}