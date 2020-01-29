package com.erivera.apps.topcharts

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp
import com.spotify.android.appremote.api.error.NotLoggedInException
import com.spotify.android.appremote.api.error.UserNotAuthorizedException
import com.spotify.protocol.client.Subscription
import com.spotify.protocol.types.PlayerState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyRemoteManagerImpl @Inject constructor(val context: Context) : SpotifyRemoteManager {
    lateinit var mSpotifyAppRemote: SpotifyAppRemote

    var playerStateSubscriber: Subscription<PlayerState>? = null

    var isConnected = false

    private val connectionListener: Connector.ConnectionListener =
        object : Connector.ConnectionListener {
            override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote
                playerStateSubscriber = spotifyAppRemote.playerApi.subscribeToPlayerState()?.apply {
                    setEventCallback { playerState ->
                        listenerList.forEach {
                            it.onNextPlayerState(playerState)
                        }
                    }
                    setErrorCallback { throwable ->
                        listenerList.forEach {
                            it.onNextPlayerError(throwable)
                        }

                    }
                }
                listenerList.forEach {
                    it.onConnected()
                }
            }

            override fun onFailure(error: Throwable) {
                if (error is NotLoggedInException || error is UserNotAuthorizedException) {
                    // Show login button and trigger the login flow from auth library when clicked
                } else if (error is CouldNotFindSpotifyApp) {
                    // Show button to download Spotify
                }
                listenerList.forEach {
                    it.onFailure(error)
                }
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
            SpotifyAppRemote.connect(context, connectionParams, connectionListener)
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
    }

    override fun removeListener(viewModelListener: SpotifyRemoteManager.ViewModelListener) {
        listenerList.remove(viewModelListener)
    }

    override fun next() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun previous() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun play() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}