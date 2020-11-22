package com.erivera.apps.topcharts

import android.app.Application
import android.util.Log
import com.erivera.apps.topcharts.utils.Constants
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.client.Subscription
import com.spotify.protocol.types.PlayerState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyRemoteManagerImpl @Inject constructor(val application: Application) : SpotifyRemoteManager {
    lateinit var mSpotifyAppRemote: SpotifyAppRemote

    var playerStateSubscriber: Subscription<PlayerState>? = null

    var isConnected = false

    var isPaused = false

    var currentPlayerState: PlayerState? = null

    private val connectionListener: Connector.ConnectionListener =
        object : Connector.ConnectionListener {
            override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote
                playerStateSubscriber = spotifyAppRemote.playerApi.subscribeToPlayerState()?.apply {
                    setEventCallback { playerState ->
                        listenerList.forEach {
                            it.onPauseStateChanged(playerState.isPaused)
                        }
                        isPaused = playerState.isPaused
                        playerState?.track?.let { track ->
                            if(currentPlayerState?.track?.uri != track.uri){
                                listenerList.forEach {
                                    it.onCurrentTrackChanged(track)
                                }
                            }
                        }
                        currentPlayerState = playerState
                    }
                    setErrorCallback { throwable ->
                        Log.d(SpotifyRemoteManagerImpl::class.java.name, "onNextPlayerError:$throwable")
                    }
                }
                Log.d(SpotifyRemoteManagerImpl::class.java.name, "onConnected")
            }

            override fun onFailure(error: Throwable) {
                Log.d(SpotifyRemoteManagerImpl::class.java.name, "onFailure:$error")
            }
        }

    private val listenerList = mutableListOf<SpotifyRemoteManager.ViewModelListener>()

    private val connectionParams = ConnectionParams.Builder(Constants.CLIENT_ID)
        .setRedirectUri(Constants.REDIRECT_URI)
        .build()

    override fun connect() {
        if (!isConnected) {
            if (::mSpotifyAppRemote.isInitialized) {
                SpotifyAppRemote.disconnect(mSpotifyAppRemote)
            }
            SpotifyAppRemote.connect(application.applicationContext, connectionParams, connectionListener)
            isConnected = true
        }
    }

    override fun disconnect() {
        if (isConnected) {
            playerStateSubscriber?.cancel()
            SpotifyAppRemote.disconnect(mSpotifyAppRemote)
            isConnected = false
        }
    }

    override fun addListener(viewModelListener: SpotifyRemoteManager.ViewModelListener) {
        listenerList.add(viewModelListener)
        connect()
        currentPlayerState?.track?.let {
            viewModelListener.onCurrentTrackChanged(track = it)
        }
        isPaused()?.let {
            viewModelListener.onPauseStateChanged(isPaused = it)
        }
    }

    override fun removeListener(viewModelListener: SpotifyRemoteManager.ViewModelListener) {
        listenerList.remove(viewModelListener)
        disconnect()
    }

    override fun next() {
        mSpotifyAppRemote.playerApi.skipNext()
    }

    override fun previous() {
        mSpotifyAppRemote.playerApi.skipPrevious()
    }

    private fun resume() {
        mSpotifyAppRemote.playerApi.resume()
    }

    private fun pause() {
        mSpotifyAppRemote.playerApi.pause()
    }

    private fun isPaused(): Boolean? {
        return currentPlayerState?.isPaused
    }

    override fun togglePlayPause() {
        isPaused()?.let {
            if (it) {
                resume()
            } else {
                pause()
            }
        }
    }
}