package com.erivera.apps.topcharts.repository

import android.content.Context
import android.util.Log
import com.erivera.apps.topcharts.models.api.*
import com.erivera.apps.topcharts.repository.network.RetrofitFactory
import com.erivera.apps.topcharts.repository.network.SpotifyService
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(val context: Context) : RemoteDataSource {
    private var spotifyService: SpotifyService? = null

    override fun startSpotifyService(clientId: String) {
        spotifyService = RetrofitFactory.makeRetrofitService(clientId, context)
    }

    override suspend fun getArtists(limit: String, termLength: String): List<ArtistsRetrofit> {
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

    override suspend fun getAlbum(albumId: String): AlbumResponse {
        val response = spotifyService?.getAlbum(albumId)
        try {
            if (response?.isSuccessful == true) {
                Log.i(javaClass.simpleName, "Response: ${response.body()}")
                return response.body() ?: AlbumResponse()
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
        return AlbumResponse()
    }

    override suspend fun getAudioFeatures(trackId: String): AudioFeaturesResponse {
        val response = spotifyService?.getAudioFeatures(trackId)
        try {
            if (response?.isSuccessful == true) {
                Log.i(javaClass.simpleName, "Response: ${response.body()}")
                return response.body() ?: AudioFeaturesResponse()
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
        return AudioFeaturesResponse()
    }

    override suspend fun getTracks(limit: String, termLength: String): List<TrackRetrofit> {
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

    override suspend fun hasValidToken(): Boolean {
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
}