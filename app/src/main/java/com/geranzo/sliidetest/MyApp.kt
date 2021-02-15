package com.geranzo.sliidetest

import android.app.Application
import com.geranzo.sliidetest.di.AppComponent
import com.geranzo.sliidetest.di.DaggerAppComponent

class MyApp : Application() {
    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}
