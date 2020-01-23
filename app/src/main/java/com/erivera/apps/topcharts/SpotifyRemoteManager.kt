package com.erivera.apps.topcharts

interface SpotifyRemoteManager {
    fun initialize()

    fun getConnectionParams(): String
}