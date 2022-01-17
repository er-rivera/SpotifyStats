package com.erivera.apps.topcharts.repository

import com.erivera.apps.topcharts.repository.keyvalue.DataStore
import com.erivera.apps.topcharts.repository.keyvalue.DataStoreImpl
import com.erivera.apps.topcharts.repository.network.RemoteDataSource
import com.erivera.apps.topcharts.repository.network.RemoteDataSourceImpl
import com.erivera.apps.topcharts.repository.persistance.helper.PersistenceSynchronizationHelper
import com.erivera.apps.topcharts.repository.persistance.helper.PersistenceMigrationHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun providesLocalDataSource(dataStore: DataStoreImpl): DataStore

    @Binds
    abstract fun providesRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    abstract fun providesPersistenceMigrationHelper(
        persistenceMigrationHelperImpl: PersistenceMigrationHelperImpl
    ): PersistenceSynchronizationHelper

    @Binds
    abstract fun providesRepository(repositoryImpl: RepositoryImpl): Repository
}