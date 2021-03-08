package com.erivera.apps.topcharts.navigation

sealed class NavigationFlow() {
    object HomeFlow: NavigationFlow()
    object TopListFlow: NavigationFlow()
}