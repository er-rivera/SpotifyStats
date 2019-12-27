package com.erivera.apps.topcharts.models.api

import com.google.gson.annotations.SerializedName

class ArtistsRetrofit {

    @SerializedName("external_urls")
    var externalUrls: ExternalUrls? = null

    @SerializedName("followers")
    var followers: Followers? = null

    @SerializedName("genres")
    var genres: List<String>? = null

    @SerializedName("href")
    var href: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("images")
    var images: List<Image>? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("popularity")
    var popularity: Int = 0

    @SerializedName("type")
    var type: String? = null

    @SerializedName("uri")
    var uri: String? = null
}