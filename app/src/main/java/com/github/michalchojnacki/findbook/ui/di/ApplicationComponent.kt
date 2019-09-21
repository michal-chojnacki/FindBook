package com.github.michalchojnacki.findbook.ui.di

import android.content.Context
import com.github.michalchojnacki.findbook.data.di.RepositoryModule
import com.github.michalchojnacki.findbook.ui.booklist.BookListViewModel
import com.github.michalchojnacki.findbook.ui.main.MainNavigationViewModel
import com.github.michalchojnacki.findbook.ui.typingsearch.TypingSearchViewModel
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(modules = [AssistedInjectModule::class, RepositoryModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    val bookListViewModelFactory: BookListViewModel.Factory

    val mainNavigationViewModel: MainNavigationViewModel

    val typingSearchViewModel: TypingSearchViewModel
}

@AssistedModule
@Module(includes = [AssistedInject_AssistedInjectModule::class])
interface AssistedInjectModule