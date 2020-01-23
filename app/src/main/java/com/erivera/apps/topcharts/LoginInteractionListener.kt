package com.erivera.apps.topcharts

import com.spotify.sdk.android.authentication.AuthenticationRequest

interface LoginInteractionListener {
    fun loginButtonClick(request: AuthenticationRequest?)
}