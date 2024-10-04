package com.vadimraspopin.mangotest.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MangoApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "authorization") {
        composable("authorization") {
            AuthorizationScreen(navController)
        }
        composable("registration/{phoneNumber}") { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            RegistrationScreen(navController, phoneNumber = phoneNumber)
        }
    }
}