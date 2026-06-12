package com.example.myapplication.presentation.feed

import com.example.myapplication.domain.model.Publication

data class FeedUiState(
    val isLoading: Boolean = true, // Para el spinner inicial de Room
    val isRefreshing: Boolean = false, // Para el pull-to-refresh de Retrofit
    val publications: List<Publication> = emptyList(),
    val selectedTagId: Int? = null, // Guarda la categoría seleccionada (null = Todas)
    val error: String? = null
)
