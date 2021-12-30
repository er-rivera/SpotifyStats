package com.erivera.apps.topcharts.repository.models.api

import com.google.gson.annotations.SerializedName

class AlbumResponse {
    @SerializedName("album_type")
    val albumType: String? = null

    @SerializedName("artists")
    val artists: List<ArtistsRetrofit>? = null

    @SerializedName("available_markets")
    val availableMarkets: List<String>? = null

    @SerializedName("copyrights")
    val copyrights: List<Copyright>? = null

    @SerializedName("external_ids")
    val externalIds: ExternalIds? = null

    @SerializedName("external_urls")
    val externalUrls: ExternalUrls? = null

    @SerializedName("genres")
    val genres: List<String>? = null

    @SerializedName("href")
    val href: String? = null

    @SerializedName("id")
    val id: String? = null

    @SerializedName("images")
    val images: List<ImageResponse>? = null

    @SerializedName("name")
    val name: String? = null

    @SerializedName("popularity")
    val popularity: Int? = null

    @SerializedName("release_date")
    val releaseDate: String? = null

    @SerializedName("release_date_precision")
    val releaseDatePrecision: String? = null

    @SerializedName("tracks")
    val tracks: TrackRetrofit? = null

    @SerializedName("type")
    val type: String? = null

    @SerializedName("uri")
    val uri: String? = null
}