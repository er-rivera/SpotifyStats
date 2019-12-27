package com.erivera.apps.topcharts.repository.dagger

import android.content.Context
import com.erivera.apps.topcharts.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(private val appContext: Context) {
    @Provides
    @Singleton
    fun provideApplicationContext() : Context = appContext

    @Provides
    @Singleton
    fun provideRepository(context: Context) : Repository = Repository(context)
}