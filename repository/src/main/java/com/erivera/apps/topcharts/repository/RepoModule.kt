package com.erivera.apps.topcharts.repository

import android.content.Context
import com.erivera.apps.topcharts.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class RepoModule {

    @Provides
    fun providesRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): Repository = RepositoryImpl(localDataSource, remoteDataSource)

    @Provides
    fun providesLocalDataSource(@ApplicationContext context: Context): LocalDataSource = LocalDataSourceImpl(context)

    @Provides
    fun providesRemoteDataSource(@ApplicationContext context: Context): RemoteDataSource = RemoteDataSourceImpl(context)
}