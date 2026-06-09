package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitudes_registro")
data class RegistrationRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val institutionalEmail: String,
    val firstName: String,
    val lastName: String,
    val area: String? = null,
    val description: String? = null,
    val status: String = "PENDIENTE",
    val createdAt: Long = System.currentTimeMillis()
)
