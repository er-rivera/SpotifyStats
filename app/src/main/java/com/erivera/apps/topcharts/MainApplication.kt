package com.erivera.apps.topcharts

import android.app.Activity
import android.app.Application
import android.app.Fragment
import com.erivera.apps.topcharts.repository.dagger.AppComponent
import com.erivera.apps.topcharts.repository.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasFragmentInjector
import javax.inject.Inject

class MainApplication : Application(), HasActivityInjector, HasFragmentInjector {
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    override fun fragmentInjector(): AndroidInjector<Fragment> = dispatchingFragmentInjector

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }

    companion object {
        private var INSTANCE: MainApplication? = null

        @JvmStatic
        fun get(): MainApplication = INSTANCE!!
    }
}