package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Publication
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    // El tagId permite filtrar por categoría
    fun getFeedStream(tagId: Int? = null): Flow<List<Publication>>

    suspend fun refreshFeed(tagId: Int? = null): Result<Unit>

    suspend fun createPublication(
        title: String,
        content: String,
        isAnonymous: Boolean,
        tagIds: List<Int>,
        files: List<java.io.File> // Archivos físicos para adjuntos
    ): Result<Unit>
}