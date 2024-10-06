package com.vadimraspopin.mangotest.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

object Routes {
    const val AUTHORIZATION = "authorization"
    const val REGISTRATION = "registration/{phoneNumber}"
    const val CHATS = "chats"
    const val CHAT = "chat/{chatId}"
}

@Composable
fun MangoApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "authorization") {
        composable(Routes.AUTHORIZATION) {
            AuthorizationScreen(navController)
        }
        composable(Routes.REGISTRATION) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            RegistrationScreen(navController, phoneNumber = phoneNumber)
        }
        composable(Routes.CHATS) {
            ChatsScreen(onChatClicked = { chatId ->
                navController.navigate("chat/$chatId")
            })
        }
        composable(
            route = Routes.CHAT,
            arguments = listOf(navArgument("chatId") { type = NavType.IntType })
        ) {
            ChatScreen()
        }
    }
}