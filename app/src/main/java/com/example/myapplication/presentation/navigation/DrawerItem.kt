package com.example.myapplication.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerItem<T : Any>(
    val icon: ImageVector,
    val title: String,
    val route: T
)