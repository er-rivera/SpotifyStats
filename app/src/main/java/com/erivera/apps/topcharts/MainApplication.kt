package com.erivera.apps.topcharts

import android.app.Application
import com.erivera.apps.topcharts.dagger.AppComponent
import com.erivera.apps.topcharts.dagger.DaggerAppComponent

open class MainApplication : Application() {

    val appComponent: AppComponent? by lazy {
        DaggerAppComponent.builder().application(this)?.build()
    }

    override fun onCreate() {
        super.onCreate()
    }
}