package com.erivera.apps.topcharts.models.domain

class Song(
    val id: String,
    val titleName: String,
    val description: String,
    val photoUrl: String,
    val position: Int,
    val uri: String
) : TopListItem