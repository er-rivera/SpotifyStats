package com.erivera.apps.topcharts.repository

import android.util.Log
import com.erivera.apps.topcharts.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.models.api.TrackRetrofit
import com.erivera.apps.topcharts.repository.network.RetrofitFactory
import com.erivera.apps.topcharts.repository.network.SpotifyService
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(val localDataSource: LocalDataSource) : Repository {
    private var spotifyService: SpotifyService? = null

    override fun saveSpotifyClientId(id: String) {
        localDataSource.saveSpotifyClientId(id)
    }

    override suspend fun hasValidSpotifyClientId(): Boolean {
        val clientId = localDataSource.getSpotifyClientId()
        Log.i(javaClass.simpleName, "Client ID: $clientId")
        return clientId != "" && isValidToken()
    }

    private fun getSpotifyClientId(): String {
        return localDataSource.getSpotifyClientId()
    }

    private fun startSpotifyService(clientId: String) {
        spotifyService = RetrofitFactory.makeRetrofitService(clientId)
    }

    override suspend fun getLongTermArtists(): List<ArtistsRetrofit> {
        return getArtists("10", TermLength.LONG_TERM.value)
    }

    override suspend fun getMediumTermArtists(): List<ArtistsRetrofit> {
        return getArtists("10", TermLength.MEDIUM_TERM.value)
    }

    override suspend fun getShortTermArtists(): List<ArtistsRetrofit> {
        return getArtists("10", TermLength.SHORT_TERM.value)
    }

    override suspend fun getLongTermTracks(): List<TrackRetrofit> {
        return getTracks("10", TermLength.LONG_TERM.value)
    }

    override suspend fun getMediumTermTracks(): List<TrackRetrofit> {
        return getTracks("10", TermLength.MEDIUM_TERM.value)
    }

    override suspend fun getShortTermTracks(): List<TrackRetrofit> {
        return getTracks("10", TermLength.SHORT_TERM.value)
    }

    private suspend fun getArtists(limit: String, termLength: String): List<ArtistsRetrofit> {
        val response = spotifyService?.getArtists(limit, termLength)
        try {
            if (response?.isSuccessful == true) {
                Log.i(javaClass.simpleName, "Response: ${response.body()}")
                return response.body()?.items ?: mutableListOf()
                //Do something with response e.g show to the UI.
            } else {
                Log.e(
                    javaClass.simpleName,
                    "Error - code: ${response?.code()} json: ${response?.errorBody()?.string()}"
                )
            }
        } catch (e: HttpException) {
            Log.e(javaClass.simpleName, "Exception ${e.message}")
        } catch (e: Throwable) {
            Log.e(javaClass.simpleName, "Ooops: Something else went wrong")
        }
        return mutableListOf()
    }

    private suspend fun getTracks(limit: String, termLength: String): List<TrackRetrofit> {
        val response = spotifyService?.getTracks(limit, termLength)
        try {
            if (response?.isSuccessful == true) {
                Log.i(javaClass.simpleName, "Response: ${response.body()}")
                return response.body()?.items ?: mutableListOf()
                //Do something with response e.g show to the UI.
            } else {
                Log.e(
                    javaClass.simpleName,
                    "Error - code: ${response?.code()} json: ${response?.errorBody()?.string()}"
                )
            }
        } catch (e: HttpException) {
            Log.e(javaClass.simpleName, "Exception ${e.message}")
        } catch (e: Throwable) {
            Log.e(javaClass.simpleName, "Ooops: Something else went wrong")
        }
        return mutableListOf()
    }

    override fun startService() {
        val clientId = getSpotifyClientId()
        startSpotifyService(clientId)
    }

    private suspend fun isValidToken(): Boolean {
        val response = spotifyService?.getUser()
        try {
            return response?.code() == 200
        } catch (e: HttpException) {
            Log.e(javaClass.simpleName, "Exception ${e.message}")
        } catch (e: Throwable) {
            Log.e(javaClass.simpleName, "Ooops: Something else went wrong")
        }
        return false
    }

    sealed class TermLength(val value: String) {

        object LONG_TERM : TermLength("long_term")
        object MEDIUM_TERM : TermLength("medium_term")
        object SHORT_TERM : TermLength("short_term")

    }
}