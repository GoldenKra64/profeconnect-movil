package com.example.myapplication.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.presentation.navigation.drawerItems

@Composable
fun AppDrawer(
    currentRoute: Any?,
    onDestinationClick: (Any) -> Unit
) {
    ModalDrawerSheet {

        Text(
            text = "ProfeConnect",
            modifier = Modifier.padding(16.dp)
        )
        drawerItems.forEach { item ->
            NavigationDrawerItem(
                label = {
                    Text(item.title)
                },
                selected = currentRoute == item.route,
                onClick = {
                    onDestinationClick(item.route)
                }
            )
        }
    }
}
