package com.erivera.apps.topcharts.models.domain

import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

data class HomeTab(
    val title: String,
    var list: MutableLiveData<List<TopListItem>> = MutableLiveData(),
    val id: Int = Random.nextInt()
)