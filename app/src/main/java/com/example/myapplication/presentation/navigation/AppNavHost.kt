package com.example.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.presentation.chat.ChatScreen
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.data.repository.FeedRepositoryImpl
import com.example.myapplication.presentation.feed.FeedScreen
import com.example.myapplication.presentation.feed.FeedViewModel
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.presentation.navigation.Feed
import com.example.myapplication.presentation.navigation.Chat
import com.example.myapplication.data.remote.api.FeedApi

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Feed,
        modifier = modifier
    ) {

        composable<Feed> {
            val context = LocalContext.current

            // CORRECCIÓN: Usamos getInstance en lugar de getDatabase
            val database = AppDatabase.getInstance(context)

            val feedRepository = FeedRepositoryImpl(
                api = RetrofitClient.feedApi,
                dao = database.feedDao(),
                context = context
            )

            // Instanciamos el ViewModel usando el Factory
            val viewModel: FeedViewModel = viewModel(
                factory = FeedViewModel.Factory(feedRepository)
            )

            // Llamamos a la pantalla visual
            FeedScreen(
                viewModel = viewModel,
                onMenuClick = openDrawer
            )
        }

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

