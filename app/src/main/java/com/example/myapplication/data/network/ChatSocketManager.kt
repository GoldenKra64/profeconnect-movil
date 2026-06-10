package com.example.myapplication.data.network

import android.util.Log
import com.example.myapplication.BuildConfig
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException

class ChatSocketManager {

    private var socket: Socket? = null
    private var onMessageReceived: ((String) -> Unit)? = null

    init {
        try {
            val options = IO.Options()
            options.forceNew = true
            options.reconnection = true
            socket = IO.socket(BuildConfig.CHATBOT_URL, options)
        } catch (e: URISyntaxException) {
            Log.e("ChatSocketManager", "Error en URI de Socket.io", e)
        }
    }

    fun setOnMessageReceivedListener(listener: (String) -> Unit) {
        onMessageReceived = listener
    }

    fun connect() {
        socket?.on(Socket.EVENT_CONNECT) {
        }?.on("chat:response") { args ->
            if (args.isNotEmpty()) {
                val msg = args[0] as JSONObject
                val content = msg.getString("message").toString()
                onMessageReceived?.invoke(content)
            }
        }?.on(Socket.EVENT_DISCONNECT) {
        }
        socket?.connect()
    }

    fun disconnect() {
        socket?.disconnect()
        socket?.off()
    }

    fun sendMessage(message: String) {
        val messageObj = JSONObject().apply {
            put("role", "user")
            put("content", message)
        }
        val messagesArray = JSONArray().apply {
            put(messageObj)
        }
        val requestBody = JSONObject().apply {
            put("messages", messagesArray)
        }
        socket?.emit("chat:message", requestBody)
    }
}
