package com.erivera.apps.topcharts.repository.dagger

import android.content.Context
import com.erivera.apps.topcharts.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun repository(applicationContext: Context) : Repository = Repository(applicationContext)
}