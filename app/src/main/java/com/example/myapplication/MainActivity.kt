package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.data.local.AuthSessionStorage
import com.example.myapplication.presentation.navigation.RootNavHost
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AuthSessionStorage.initialize(applicationContext)

        setContent {
            MyApplicationTheme {
                RootNavHost()
            }
        }
    }
}
