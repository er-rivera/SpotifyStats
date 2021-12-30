package com.erivera.apps.topcharts.repository

import android.content.Context
import androidx.room.Room
import com.erivera.apps.topcharts.repository.keyvalue.DataStore
import com.erivera.apps.topcharts.repository.keyvalue.DataStoreImpl
import com.erivera.apps.topcharts.repository.network.RemoteDataSource
import com.erivera.apps.topcharts.repository.network.RemoteDataSourceImpl
import com.erivera.apps.topcharts.repository.persistance.artist.ArtistDao
import com.erivera.apps.topcharts.repository.persistance.artist.ArtistDatabase
import com.erivera.apps.topcharts.repository.persistance.helper.PersistenceSynchronizationHelper
import com.erivera.apps.topcharts.repository.persistance.helper.PersistenceMigrationHelperImpl
import com.erivera.apps.topcharts.repository.persistance.tracks.TrackDao
import com.erivera.apps.topcharts.repository.persistance.tracks.TrackDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun providesRepository(
        localDataSource: DataStore,
        remoteDataSource: RemoteDataSource,
        artistDatabase: ArtistDatabase,
        trackDatabase: TrackDatabase,
        persistenceMigrationHelper: PersistenceSynchronizationHelper
    ): Repository = RepositoryImpl(
        localDataSource,
        artistDatabase,
        trackDatabase,
        remoteDataSource,
        persistenceMigrationHelper
    )

    @Singleton
    @Provides
    fun providesLocalDataSource(@ApplicationContext context: Context): DataStore =
        DataStoreImpl(context)

    @Singleton
    @Provides
    fun providesRemoteDataSource(@ApplicationContext context: Context): RemoteDataSource =
        RemoteDataSourceImpl(context)

    @Singleton
    @Provides
    fun providesArtistDatabase(@ApplicationContext context: Context): ArtistDatabase =
        Room.databaseBuilder(
            context,
            ArtistDatabase::class.java,
            ArtistDatabase.DB_NAME
        ).build()

    @Singleton
    @Provides
    fun providesTrackDatabase(@ApplicationContext context: Context): TrackDatabase =
        Room.databaseBuilder(
            context,
            TrackDatabase::class.java,
            TrackDatabase.DB_NAME
        ).build()

    @Provides
    fun providesArtistDao(artistDatabase: ArtistDatabase): ArtistDao = artistDatabase.getArtistDao()

    @Provides
    fun providesTrackDao(trackDatabase: TrackDatabase): TrackDao = trackDatabase.getTrackDao()

    @Provides
    fun providesPersistenceMigrationHelper(
        dataStore: DataStore,
        artistDatabase: ArtistDatabase,
        trackDatabase: TrackDatabase
    ): PersistenceSynchronizationHelper =
        PersistenceMigrationHelperImpl(dataStore, artistDatabase, trackDatabase)
}