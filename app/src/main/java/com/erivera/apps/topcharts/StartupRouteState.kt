package com.erivera.apps.topcharts

sealed class StartupRouteState {
    object Login : StartupRouteState()

    object Home : StartupRouteState()
}