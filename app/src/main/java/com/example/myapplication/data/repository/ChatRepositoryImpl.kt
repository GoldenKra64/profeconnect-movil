package com.example.myapplication.data.repository

import com.example.myapplication.data.network.ChatSocketManager
import com.example.myapplication.domain.model.Message
import com.example.myapplication.domain.repository.ChatRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ChatRepositoryImpl(
    private val socketManager: ChatSocketManager = ChatSocketManager()
) : ChatRepository {

    override fun connect() {
        socketManager.connect()
    }

    override fun disconnect() {
        socketManager.disconnect()
    }

    override fun sendMessage(message: String) {
        socketManager.sendMessage(message)
    }

    override fun receiveMessages(): Flow<Message> = callbackFlow {
        socketManager.setOnMessageReceivedListener { text ->
            val msg = Message(text = text, isFromUser = false)
            trySend(msg)
        }
        
        awaitClose { 
            socketManager.setOnMessageReceivedListener { }
        }
    }
}
