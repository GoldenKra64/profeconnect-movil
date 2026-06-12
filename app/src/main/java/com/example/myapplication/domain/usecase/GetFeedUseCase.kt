package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.Publication
import com.example.myapplication.domain.repository.FeedRepository
import kotlinx.coroutines.flow.Flow

class GetFeedUseCase(
    private val repository: FeedRepository
) {
    operator fun invoke(tagId: Int? = null): Flow<List<Publication>> {
        return repository.getFeedStream(tagId)
    }
}