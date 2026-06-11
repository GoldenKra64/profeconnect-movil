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

        composable<Profile> {
            val context = androidx.compose.ui.platform.LocalContext.current
            val application = context.applicationContext as android.app.Application
            val viewModel: com.example.myapplication.presentation.profile.ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = com.example.myapplication.presentation.profile.ProfileViewModel.Factory(application)
            )

            com.example.myapplication.presentation.profile.ProfileScreen(
                viewModel = viewModel
            )
        }

        /*
        composable<CloseSession> {
            // JWT Exit Function
        }
        */
    }
}

