package com.example.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.presentation.chat.ChatScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Chat,
        modifier = modifier
    ) {

        composable<Chat>{
            ChatScreen(
                onMenuClick = openDrawer
            );
        }
        /*
        composable<Home> {
            HomeScreen()
        }

        composable<Profile> {
            ProfileScreen()
        }

        composable<Settings> {
            SettingsScreen()
        }
        */
    }
}