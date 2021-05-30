package com.erivera.apps.topcharts.ui.listener

import com.spotify.sdk.android.auth.AuthorizationRequest

interface LoginInteractionListener {
    fun loginButtonClick(request: AuthorizationRequest?)
}