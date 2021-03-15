package com.erivera.apps.topcharts.repository.models.api

import com.google.gson.annotations.SerializedName

class Copyright {
    @SerializedName("text")
    var text: String? = null

    @SerializedName("type")
    var type: String? = null
}