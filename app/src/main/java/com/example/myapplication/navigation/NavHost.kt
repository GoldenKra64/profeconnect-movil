package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.view.ClientView
import com.example.myapplication.view.UserView
import com.example.myapplication.viewmodel.ClientViewModel
import com.example.myapplication.viewmodel.UserViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = UserScreen
    ) {
        composable<UserScreen> {
            val viewModel: UserViewModel = viewModel()

            UserView(
                viewModel = viewModel,
                onNavigateToClient = {
                    navController.navigate(ClientScreen)
                }
            )
        }

        composable<ClientScreen> {
            val viewModel: ClientViewModel = viewModel()

            ClientView(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}