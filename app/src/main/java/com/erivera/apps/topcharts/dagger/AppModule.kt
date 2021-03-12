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

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    fun providesChildDependency(): ChildDependency = ChildDependencyImpl()

    @Provides
    fun providesRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): Repository = RepositoryImpl(localDataSource, remoteDataSource)

    @Provides
    fun providesLocalDataSource(@ApplicationContext context: Context): LocalDataSource = LocalDataSourceImpl(context)

    @Provides
    fun providesRemoteDataSource(@ApplicationContext context: Context): RemoteDataSource = RemoteDataSourceImpl(context)

    @Provides
    fun providesSpotifyRemoteManager(@ApplicationContext context: Context): SpotifyRemoteManager = SpotifyRemoteManagerImpl(context)

    @Provides
    fun providesDeviceManager(@ApplicationContext context: Context): DeviceManager = DeviceManagerImpl(context)
}