package com.erivera.apps.topcharts.repository.models.api

import com.google.gson.annotations.SerializedName

class AlbumRetrofit {
    @SerializedName("album_type")
    var albumType: String? = null

//    @SerializedName("artists")
//    var artists: List<Artist>? = null

    @SerializedName("available_markets")
    var availableMarkets: List<String>? = null

    @SerializedName("external_urls")
    var externalUrls: ExternalUrls? = null

    @SerializedName("href")
    var href: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("images")
    var images: List<Image>? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("release_date")
    var releaseDate: String? = null

    @SerializedName("release_date_precision")
    var releaseDatePrecision: String? = null

    @SerializedName("total_tracks")
    var totalTracks: Int? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("uri")
    var uri: String? = null
}