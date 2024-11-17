package com.example.androidcookbook.ui.nav

import androidx.navigation.NavHostController

fun NavHostController.navigateIfNotOn(route: String) {
    if (currentDestination?.route != route) {
        navigate(route)
    }
}