package com.erivera.apps.topcharts.repository.keyvalue

interface DataStore {
    fun saveSpotifyClientId(id: String)

    fun getSpotifyClientId(): String

    fun setLong(key: String, value: Long)

    fun getLong(key: String): Long

    fun setString(key: String, value: String)

    fun getString(key: String): String
}