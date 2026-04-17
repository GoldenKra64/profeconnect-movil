package com.example.myapplication.data

sealed class RegistroState {
    object Idle : RegistroState()
    data class Success(val message: Asistente, val mensaje: String) : RegistroState()
    data class Error(val message: String) : RegistroState()
}