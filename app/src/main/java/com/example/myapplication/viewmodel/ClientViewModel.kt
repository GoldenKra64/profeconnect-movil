package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Client
import com.example.myapplication.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClientViewModel : ViewModel() {

    private val _client = MutableStateFlow<Client?>(null)
    val client: StateFlow<Client?> = _client.asStateFlow()

    fun changeName(newName: String) {
        _client.value = _client.value?.copy(
            name = newName
        )
    }

    fun changeAge(newAge: String) {
        val age = newAge.toIntOrNull() ?: 0

        _client.value = _client.value?.copy(
            age = age
        )
    }

    fun changeEmail(newEmail: String) {
        _client.value = _client.value?.copy(
            email = newEmail
        )
    }

    fun saveClient() {
        println("Cliente guardado: ${_client.value}")
    }
}