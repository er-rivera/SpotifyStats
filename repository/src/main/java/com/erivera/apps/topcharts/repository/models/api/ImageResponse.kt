package com.erivera.apps.topcharts.repository.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ImageResponse {

    @SerializedName("height")
    @Expose
    var height: Int = 0
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("width")
    @Expose
    var width: Int = 0

}