package com.erivera.apps.topcharts.repository.models.api

import com.google.gson.annotations.SerializedName

class TrackRetrofit {

    @SerializedName("album")
    var album: AlbumRetrofit? = null

    @SerializedName("artists")
    var artists: List<ArtistsRetrofit>? = null

    @SerializedName("available_markets")
    var availableMarkets: List<String>? = null

    @SerializedName("disc_number")
    var discNumber: Int? = null

    @SerializedName("duration_ms")
    var durationMs: Int? = null

    @SerializedName("explicit")
    var explicit: Boolean? = null

    @SerializedName("external_ids")
    var externalIds: ExternalIds? = null

    @SerializedName("external_urls")
    var externalUrls: ExternalUrls? = null

    @SerializedName("href")
    var href: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("is_local")
    var isLocal: Boolean? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("popularity")
    private val popularity: Int? = null

    @SerializedName("preview_url")
    var previewUrl: String? = null

    @SerializedName("track_number")
    var trackNumber: Int? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("uri")
    var uri: String? = null

}