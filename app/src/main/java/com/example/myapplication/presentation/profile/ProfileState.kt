package com.example.myapplication.presentation.profile

import com.example.myapplication.domain.model.Profile

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val error: String? = null
)
