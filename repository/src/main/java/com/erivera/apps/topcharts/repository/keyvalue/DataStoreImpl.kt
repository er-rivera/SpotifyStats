package com.erivera.apps.topcharts.repository.keyvalue

import android.content.Context
import android.content.SharedPreferences
import com.erivera.apps.topcharts.repository.R
import javax.inject.Inject

class DataStoreImpl @Inject constructor(val context: Context) : DataStore {
    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(
                R.string.shared_pref_key
            ), Context.MODE_PRIVATE
        )
    }

    private val clientId: String by lazy { context.getString(R.string.spotify_client_id) }

    override fun saveSpotifyClientId(id: String) {
        setString(clientId, id)
    }

    override fun getSpotifyClientId(): String {
        return getString(clientId)
    }

    override fun setString(key: String, value: String) {
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun setLong(key: String, value: Long) {
        with(sharedPref.edit()) {
            putLong(key, value)
            apply()
        }
    }

    override fun getLong(key: String): Long {
        return sharedPref.getLong(key, 0L)
    }

    override fun getString(key: String): String {
        return sharedPref.getString(key, "") ?: ""
    }
}