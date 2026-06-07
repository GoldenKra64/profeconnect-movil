package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun connect()
    fun disconnect()
    fun sendMessage(message: String)
    fun receiveMessages(): Flow<Message>
}
