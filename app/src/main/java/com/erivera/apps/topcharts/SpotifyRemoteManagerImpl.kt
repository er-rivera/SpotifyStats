package com.erivera.apps.topcharts

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyRemoteManagerImpl @Inject constructor() : SpotifyRemoteManager {
    override fun getConnectionParams(): String {
        return ""
    }

    override fun initialize() {

    }
}