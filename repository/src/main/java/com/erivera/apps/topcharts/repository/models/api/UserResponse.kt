package com.erivera.apps.topcharts.repository.models.api

import com.google.gson.annotations.SerializedName

class UserResponse {
    @SerializedName("display_name")
    var displayName: String? = null

    @SerializedName("external_urls")
    var externalUrls: ExternalUrls? = null

    @SerializedName("followers")
    var followers: Followers? = null

    @SerializedName("href")
    var href: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("images")
    var images = ArrayList<ImageResponse>()

    @SerializedName("type")
    var type: String? = null

    @SerializedName("uri")
    var uri: String? = null
}