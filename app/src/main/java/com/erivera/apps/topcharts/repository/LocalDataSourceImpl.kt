package com.erivera.apps.topcharts.repository

import android.content.Context
import android.content.SharedPreferences
import com.erivera.apps.topcharts.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(val context: Context) : LocalDataSource {
    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(
                R.string.shared_pref_key
            ), Context.MODE_PRIVATE
        )
    }

    private val clientId: String by lazy { context.getString(R.string.spotify_client_id) }

    override fun saveSpotifyClientId(id: String) {
        saveString(clientId, id)
    }

    override fun getSpotifyClientId(): String {
        return getString(clientId)
    }

    private fun saveString(key: String, value: String) {
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    private fun getString(key: String): String {
        return sharedPref.getString(key, "") ?: ""
    }
}