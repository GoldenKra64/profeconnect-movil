package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.data.repository.ProfileRepositoryImpl
import com.example.myapplication.domain.usecase.GetProfileUseCase
import com.example.myapplication.presentation.profile.ProfileScreen
import com.example.myapplication.presentation.profile.ProfileViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Manual DI para inyectar dependencias al ViewModel
        val profileRepository = ProfileRepositoryImpl(RetrofitClient.profileApi)
        val getProfileUseCase = GetProfileUseCase(profileRepository)
        val factory = ProfileViewModel.Factory(getProfileUseCase)

        setContent {
            MyApplicationTheme {
                val profileViewModel: ProfileViewModel = viewModel(factory = factory)
                ProfileScreen(viewModel = profileViewModel)
            }
        }
    }
}
