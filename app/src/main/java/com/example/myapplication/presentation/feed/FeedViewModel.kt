package com.example.myapplication.presentation.feed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myapplication.domain.repository.FeedRepository
import com.example.myapplication.domain.usecase.GetFeedUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(
    private val getFeedUseCase: GetFeedUseCase,
    private val repository: FeedRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG_FILTER_KEY = "selected_tag_id"

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private var feedJob: Job? = null

    init {
        val savedTagId = savedStateHandle.get<Int>(TAG_FILTER_KEY)
        if (savedTagId != null) {
            _uiState.update { it.copy(selectedTagId = savedTagId) }
        }

        observeFeed(savedTagId)
        refreshFeed()
    }

    fun setTagFilter(tagId: Int?) {
        savedStateHandle[TAG_FILTER_KEY] = tagId
        _uiState.update { it.copy(selectedTagId = tagId) }
        observeFeed(tagId)
    }

    private fun observeFeed(tagId: Int?) {
        feedJob?.cancel()
        feedJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getFeedUseCase(tagId)
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { pubs ->
                    _uiState.update { it.copy(isLoading = false, publications = pubs) }
                }
        }
    }

    fun refreshFeed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }

            val result = repository.refreshFeed(_uiState.value.selectedTagId)

            result.onFailure { e ->
                _uiState.update { it.copy(error = e.message) }
            }

            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    // Patrón Factory nativo (Igual que en LoginViewModel)
    class Factory(
        private val repository: FeedRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return FeedViewModel(
                getFeedUseCase = GetFeedUseCase(repository),
                repository = repository,
                savedStateHandle = extras.createSavedStateHandle()
            ) as T
        }
    }
}