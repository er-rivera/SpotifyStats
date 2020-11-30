package com.erivera.apps.topcharts.ui.listener

import com.spotify.sdk.android.authentication.AuthenticationRequest

interface LoginInteractionListener {
    fun loginButtonClick(request: AuthenticationRequest?)
}