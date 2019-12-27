package com.erivera.apps.topcharts.models.domain

import androidx.lifecycle.MutableLiveData

data class HomeTab(
    val title: String,
    val list: MutableLiveData<List<TopListItem>> = MutableLiveData()
)