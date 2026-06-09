package com.example.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.presentation.chat.ChatScreen
import com.example.myapplication.presentation.register.RegisterScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Chat,
        modifier = modifier
    ) {

        /*
        composable<Feed> {
            HomeScreen(
                onMenuClick = openDrawer
            )
        }
        */

        composable<Chat> {
            ChatScreen(
                onMenuClick = openDrawer
            )
        }

        composable<Register> {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        /*
        composable<Profile> {
            ProfileScreen(
                onMenuClick = openDrawer
            )
        }

        composable<CloseSession> {
            // JWT Exit Function
        }
        */
    }
}
