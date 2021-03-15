package com.erivera.apps.topcharts.repository

interface LocalDataSource{
    fun saveSpotifyClientId(id: String)

    fun getSpotifyClientId(): String
}