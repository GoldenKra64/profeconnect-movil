package com.example.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.local.AuthSessionStorage
import com.example.myapplication.presentation.components.MainShell
import com.example.myapplication.presentation.login.LoginScreen
import com.example.myapplication.presentation.register.RegisterScreen

@Composable
fun RootNavHost() {
    val navController = rememberNavController()
    val startDestination: Any = if (AuthSessionStorage.getToken() != null) {
        AppShellRoute
    } else {
        LoginRoute
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<LoginRoute> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppShellRoute) {
                        popUpTo(LoginRoute) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onRegisterClick = {
                    navController.navigate(RegisterRoute)
                }
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                showBackNavigation = true,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<AppShellRoute> {
            MainShell(
                onLogout = {
                    AuthSessionStorage.clearSession()
                    navController.navigate(LoginRoute) {
                        popUpTo(AppShellRoute) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
