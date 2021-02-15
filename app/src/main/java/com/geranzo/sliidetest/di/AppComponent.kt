package com.geranzo.sliidetest.di

import android.content.Context
import com.geranzo.data.di.NetworkModule
import com.geranzo.data.di.StorageModule
import com.geranzo.sliidetest.ui.main.MainFragment
import com.geranzo.sliidetest.ui.main.MainViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(fragment: MainFragment)

    val mainViewModelFactory: MainViewModel.Factory
}
