package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.myapplication.presentation.chat.ChatScreen
import com.example.myapplication.presentation.components.MainShell
import com.example.myapplication.ui.theme.MyApplicationTheme
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                MainShell()
            }
        }
    }
}