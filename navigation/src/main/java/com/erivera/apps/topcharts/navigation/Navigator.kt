package com.erivera.apps.topcharts.navigation

import androidx.navigation.NavController

class Navigator {
    lateinit var navController: NavController

    fun navigateToFlow(navigationFlow: NavigationFlow){
        when(navigationFlow){
            NavigationFlow.HomeFlow -> {
                navController.navigate(BaseNavGraphDirections.actionGlobalHomeFlow())
            }
            NavigationFlow.TopListFlow -> {
                navController.navigate(BaseNavGraphDirections.actionGlobalTopListFlow())
            }
        }
    }
}