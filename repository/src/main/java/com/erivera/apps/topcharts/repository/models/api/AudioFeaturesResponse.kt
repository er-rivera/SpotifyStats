package com.erivera.apps.topcharts.repository.models.api

import com.google.gson.annotations.SerializedName

class AudioFeaturesResponse {

    @SerializedName("duration_ms")
    val durationMs: Int? = null

    @SerializedName("key")
    val key: Int? = null

    @SerializedName("mode")
    val mode: Int? = null

    @SerializedName("time_signature")
    val timeSignature: Int? = null

    @SerializedName("acousticness")
    val acousticness: Float? = null

    @SerializedName("danceability")
    val danceability: Float? = null

    @SerializedName("energy")
    val energy: Float? = null

    @SerializedName("instrumentalness")
    val instrumentalness: Float? = null

    @SerializedName("liveness")
    val liveness: Float? = null

    @SerializedName("loudness")
    val loudness: Float? = null

    @SerializedName("speechiness")
    val speechiness: Float? = null

    @SerializedName("valence")
    val valence: Float? = null

    @SerializedName("tempo")
    val tempo: Float? = null

    @SerializedName("id")
    val id: String? = null

    @SerializedName("uri")
    val uri: String? = null

    @SerializedName("track_href")
    val trackHref: String? = null

    @SerializedName("analysis_url")
    val analysisUrl: String? = null

    @SerializedName("type")
    val type: String? = null
}