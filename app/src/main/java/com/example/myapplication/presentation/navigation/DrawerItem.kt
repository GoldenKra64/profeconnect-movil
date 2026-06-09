package com.example.myapplication.presentation.navigation

data class DrawerItem<T : Any>(
    val title: String,
    val route: T
)