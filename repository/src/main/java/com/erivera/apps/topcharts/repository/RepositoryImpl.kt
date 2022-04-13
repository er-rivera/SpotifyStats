package com.erivera.apps.topcharts.repository

import android.util.Log
import com.erivera.apps.topcharts.repository.keyvalue.DataStore
import com.erivera.apps.topcharts.repository.models.api.AlbumResponse
import com.erivera.apps.topcharts.repository.models.api.AudioFeaturesResponse
import com.erivera.apps.topcharts.repository.network.RemoteDataSource
import com.erivera.apps.topcharts.repository.persistance.artist.Artist
import com.erivera.apps.topcharts.repository.persistance.artist.ArtistDatabase
import com.erivera.apps.topcharts.repository.persistance.helper.PersistenceSynchronizationHelper
import com.erivera.apps.topcharts.repository.persistance.tracks.Track
import com.erivera.apps.topcharts.repository.persistance.tracks.TrackDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val localDataStore: DataStore,
    private val artistLocalDatabase: ArtistDatabase,
    private val tracksLocalDatabase: TrackDatabase,
    private val remoteDataSource: RemoteDataSource,
    private val persistenceSynchronizationHelper: PersistenceSynchronizationHelper
) : Repository {

    companion object {
        private val TAG: String = RepositoryImpl::class.java.name
        private const val ARTIST_LIMIT = 15
        private const val TRACKS_LIMIT = 20
    }

    init {
        startService()
    }

    override fun saveSpotifyClientId(id: String) {
        localDataStore.saveSpotifyClientId(id)
    }

    override suspend fun hasValidSpotifyClientId(): Boolean {
        val storedClientId = localDataStore.getSpotifyClientId()
        Log.i(javaClass.simpleName, "Client ID: $storedClientId")
        return storedClientId != "" && remoteDataSource.hasValidToken()
    }

    override suspend fun getLongTermArtists(): Flow<List<Artist>> {
        return artistLocalDatabase.getArtistDao().getLongTermArtists(ARTIST_LIMIT)
    }

    override suspend fun getMediumTermArtists(): Flow<List<Artist>> {
        return artistLocalDatabase.getArtistDao().getMidTermArtists(ARTIST_LIMIT)
    }

    override suspend fun getShortTermArtists(): Flow<List<Artist>> {
        return artistLocalDatabase.getArtistDao().getShortTermArtists(ARTIST_LIMIT)
    }

    override suspend fun getLongTermTracks(): Flow<List<Track>> {
        return tracksLocalDatabase.getTrackDao().getLongTermTracks(TRACKS_LIMIT)
    }

    override suspend fun getMediumTermTracks(): Flow<List<Track>> {
        return tracksLocalDatabase.getTrackDao().getMidTermTracks(TRACKS_LIMIT)
    }

    override suspend fun getShortTermTracks(): Flow<List<Track>> {
        return tracksLocalDatabase.getTrackDao().getShortTermTracks(TRACKS_LIMIT)
    }

    override suspend fun getAlbum(albumId: String): AlbumResponse {
        return remoteDataSource.getAlbum(albumId)
    }

    override suspend fun getAudioFeatures(trackId: String): AudioFeaturesResponse {
        return remoteDataSource.getAudioFeatures(trackId)
    }

    override fun startService() {
        val clientId = localDataStore.getSpotifyClientId()
        remoteDataSource.startSpotifyService(clientId)
    }

    override suspend fun refreshDb(refreshFlow: MutableStateFlow<Boolean>) {
        Log.d(TAG, "refreshDb: Should Refresh DB")
        persistenceSynchronizationHelper.updatePersistenceTables(
            localDbArtists = flow {
             emit(artistLocalDatabase.getArtistDao().getSingleShotArtists())
            },
            shortArtists = remoteDataSource.getArtists("50", TermLength.ShortTerm.key),
            midArtists = remoteDataSource.getArtists("50", TermLength.MediumTerm.key),
            longArtists = remoteDataSource.getArtists("50", TermLength.LongTerm.key),
            localDbTracks = flow {
               emit(tracksLocalDatabase.getTrackDao().getSingleShotTracks())
            },
            shortTracks = remoteDataSource.getTracks("50", TermLength.ShortTerm.key),
            midTracks = remoteDataSource.getTracks("50", TermLength.MediumTerm.key),
            longTracks = remoteDataSource.getTracks("50", TermLength.LongTerm.key),
            mutableFlow = refreshFlow
        )
    }

    sealed class TermLength(val key: String) {
        object LongTerm : TermLength("long_term")
        object MediumTerm : TermLength("medium_term")
        object ShortTerm : TermLength("short_term")

    }
}