package com.erivera.apps.topcharts.models.api

import com.google.gson.annotations.SerializedName

class TrackResponse {

    @SerializedName("items")
    var items: List<TrackRetrofit>? = null

    @SerializedName("total")
    var total: Int = 0

    @SerializedName("limit")
    var limit: Int = 0

    @SerializedName("offset")
    var offset: Int = 0

    @SerializedName("href")
    var href: String? = null

    @SerializedName("previous")
    var previous: Any? = null

    @SerializedName("next")
    var next: Any? = null

}