package com.example.myapplication.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.ChatRepositoryImpl
import com.example.myapplication.domain.model.Message
import com.example.myapplication.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository = ChatRepositoryImpl()
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(
        listOf(Message(text = "Bienvenido al asistente de IA de ProfeConnect, en que te puedo asistir?", isFromUser = false))
    )
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isWaiting = MutableStateFlow(false)
    val isWaiting: StateFlow<Boolean> = _isWaiting.asStateFlow()

    init {
        repository.connect()
        Log.d("Chatbot", "Conectado");
        listenForMessages()
    }

    private fun listenForMessages() {
        viewModelScope.launch {
            repository.receiveMessages()
                .catch { e ->
                        Log.d("Chatbot", "Error: ${e.message}")
                        _isWaiting.value = false
                }
                .collect { message ->
                    _messages.value = _messages.value + message
                    _isWaiting.value = false
                }
        }
    }

    fun sendMessage(text: String) {
        if (text.isNotBlank() && !_isWaiting.value) {
            val userMessage = Message(text = text, isFromUser = true)
            _messages.value = _messages.value + userMessage
            _isWaiting.value = true
            Log.d("Chatbot", "Mensaje enviado: $text")
            repository.sendMessage(text)
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.disconnect()
    }
}
