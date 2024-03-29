package com.erivera.apps.topcharts.repository.network

import android.content.Context
import android.util.Log
import com.erivera.apps.topcharts.repository.models.api.AlbumResponse
import com.erivera.apps.topcharts.repository.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.repository.models.api.AudioFeaturesResponse
import com.erivera.apps.topcharts.repository.models.api.TrackRetrofit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(@ApplicationContext val context: Context) :
    RemoteDataSource {
    private var spotifyService: SpotifyService? = null

    override fun startSpotifyService(clientId: String) {
        spotifyService = RetrofitFactory.makeRetrofitService(clientId, context)
    }

    override suspend fun getArtists(
        limit: String,
        termLength: String
    ): Flow<List<ArtistsRetrofit>> {
        val response = spotifyService?.getArtists(limit, termLength)
        try {
            if (response?.isSuccessful == true) {
                Log.i(javaClass.simpleName, "Response: ${response.body()}")
                return flow { emit(response.body()?.items ?: mutableListOf<ArtistsRetrofit>()) }
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
        return flow { emit(mutableListOf()) }
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

    override suspend fun getTracks(limit: String, termLength: String): Flow<List<TrackRetrofit>> {
        val response = spotifyService?.getTracks(limit, termLength)
        try {
            if (response?.isSuccessful == true) {
                Log.i(javaClass.simpleName, "Response: ${response.body()}")
                return flow { emit(response.body()?.items ?: mutableListOf<TrackRetrofit>()) }
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
        return flow { emit(mutableListOf()) }
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