package com.example.myapplication.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object Feed

@Serializable
object Chat

@Serializable
object Profile

@Serializable
object CloseSession

val drawerItems = listOf(
    DrawerItem(
        title = "Feed",
        route = Feed
    ),
    DrawerItem(
        title = "Chatbot",
        route = Chat
    ),
    DrawerItem(
        title = "Perfil",
        route = Profile
    ),
    DrawerItem(
        title = "Cerrar Sesión",
        route = CloseSession
    )
)