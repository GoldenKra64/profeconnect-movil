package com.example.myapplication.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import kotlinx.serialization.Serializable

@Serializable
object Feed

@Serializable
object Chat

@Serializable
object Profile

@Serializable
object Register

@Serializable
object CloseSession

val drawerItems = listOf(
    DrawerItem(
        icon = Icons.Default.Home,
        title = "Feed",
        route = Feed
    ),
    DrawerItem(
        icon = Icons.Default.ChatBubble,
        title = "Chatbot",
        route = Chat
    ),
    DrawerItem(
        icon = Icons.Default.Person,
        title = "Perfil",
        route = Profile
    ),
    DrawerItem(
        icon = Icons.Default.PersonAdd,
        title = "Registro Docente",
        route = Register
    ),
    DrawerItem(
        icon = Icons.AutoMirrored.Filled.ExitToApp,
        title = "Cerrar Sesión",
        route = CloseSession
    )
)