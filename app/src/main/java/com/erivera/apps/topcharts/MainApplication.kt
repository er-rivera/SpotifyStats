package com.erivera.apps.topcharts

import android.app.Application
import com.erivera.apps.topcharts.dagger.AppComponent
import com.erivera.apps.topcharts.dagger.DaggerAppComponent

open class MainApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        private var INSTANCE: MainApplication? = null

        @JvmStatic
        fun get(): MainApplication = INSTANCE!!
    }
}