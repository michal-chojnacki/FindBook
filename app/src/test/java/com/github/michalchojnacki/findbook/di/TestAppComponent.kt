package com.github.michalchojnacki.findbook.di

import com.github.michalchojnacki.findbook.domain.CapturedTextValidUseCaseTest
import com.github.michalchojnacki.findbook.domain.SearchForBooksDataSource
import com.github.michalchojnacki.findbook.domain.SearchForBooksWithQueryUseCaseTest
import com.github.michalchojnacki.findbook.ui.di.AssistedInjectModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AssistedInjectModule::class])
interface TestAppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance mockSearchForBooksDataSource: SearchForBooksDataSource): TestAppComponent
    }

    fun inject(test: SearchForBooksWithQueryUseCaseTest)

    fun inject(test: CapturedTextValidUseCaseTest)
}