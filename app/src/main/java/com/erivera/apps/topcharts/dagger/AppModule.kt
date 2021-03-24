package com.erivera.apps.topcharts.dagger

import android.content.Context
import com.erivera.apps.topcharts.DeviceManager
import com.erivera.apps.topcharts.DeviceManagerImpl
import com.erivera.apps.topcharts.SpotifyRemoteManager
import com.erivera.apps.topcharts.SpotifyRemoteManagerImpl
import com.erivera.apps.topcharts.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun providesSpotifyRemoteManager(@ApplicationContext context: Context): SpotifyRemoteManager = SpotifyRemoteManagerImpl(context)

    @Singleton
    @Provides
    fun providesDeviceManager(@ApplicationContext context: Context): DeviceManager = DeviceManagerImpl(context)
}