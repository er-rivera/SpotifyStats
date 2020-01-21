package com.erivera.apps.topcharts.dagger

import com.erivera.apps.topcharts.repository.LocalDataSource
import com.erivera.apps.topcharts.repository.LocalDataSourceImpl
import com.erivera.apps.topcharts.repository.Repository
import com.erivera.apps.topcharts.repository.RepositoryImpl
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
}