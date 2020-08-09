package com.erivera.apps.topcharts.repository

import android.util.Log
import com.erivera.apps.topcharts.models.api.AlbumResponse
import com.erivera.apps.topcharts.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.models.api.AudioFeaturesResponse
import com.erivera.apps.topcharts.models.api.TrackRetrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override fun saveSpotifyClientId(id: String) {
        localDataSource.saveSpotifyClientId(id)
    }

    override suspend fun hasValidSpotifyClientId(): Boolean {
        val storedClientId = localDataSource.getSpotifyClientId()
        Log.i(javaClass.simpleName, "Client ID: $storedClientId")
        return storedClientId != "" && remoteDataSource.hasValidToken()
    }

    override suspend fun getLongTermArtists(): List<ArtistsRetrofit> {
        return remoteDataSource.getArtists("10", TermLength.LongTerm.key)
    }

    override suspend fun getMediumTermArtists(): List<ArtistsRetrofit> {
        return remoteDataSource.getArtists("10", TermLength.MediumTerm.key)
    }

    override suspend fun getShortTermArtists(): List<ArtistsRetrofit> {
        return remoteDataSource.getArtists("10", TermLength.ShortTerm.key)
    }

    override suspend fun getLongTermTracks(): List<TrackRetrofit> {
        return remoteDataSource.getTracks("10", TermLength.LongTerm.key)
    }

    override suspend fun getMediumTermTracks(): List<TrackRetrofit> {
        return remoteDataSource.getTracks("10", TermLength.MediumTerm.key)
    }

    override suspend fun getShortTermTracks(): List<TrackRetrofit> {
        return remoteDataSource.getTracks("10", TermLength.ShortTerm.key)
    }

    override suspend fun getAlbum(albumId: String): AlbumResponse {
        return remoteDataSource.getAlbum(albumId)
    }

    override suspend fun getAudioFeatures(trackId: String): AudioFeaturesResponse {
        return remoteDataSource.getAudioFeatures(trackId)
    }

    override fun startService() {
        val clientId = localDataSource.getSpotifyClientId()
        remoteDataSource.startSpotifyService(clientId)
    }

    sealed class TermLength(val key: String) {

        object LongTerm : TermLength("long_term")
        object MediumTerm : TermLength("medium_term")
        object ShortTerm : TermLength("short_term")

    }
}