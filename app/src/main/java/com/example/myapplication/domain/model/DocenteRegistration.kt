package com.example.myapplication.domain.model

data class DocenteRegistration(
    val firstName: String,
    val lastName: String,
    val institutionalEmail: String,
    val area: String? = null,
    val description: String? = null
)
