package com.erivera.apps.topcharts.dagger

import com.erivera.apps.topcharts.SpotifyRemoteManager
import com.erivera.apps.topcharts.SpotifyRemoteManagerImpl
import com.erivera.apps.topcharts.repository.*
import dagger.Binds
import dagger.Module


@Module
abstract class AppModule {
    @Binds
    abstract fun providesChildDependency(childDependency: ChildDependencyImpl) : ChildDependency

    @Binds
    abstract fun providesRepository(repository: RepositoryImpl) : Repository

    @Binds
    abstract fun providesLocalDataSource(localDataSource: LocalDataSourceImpl): LocalDataSource

    @Binds
    abstract fun providesRemoteDataSource(remoteDataSource: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    abstract fun providesSpotifyRemoteManager(remoteDataSource: SpotifyRemoteManagerImpl): SpotifyRemoteManager
}