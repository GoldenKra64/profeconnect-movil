package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey
    val id: Int,
    val institutionalEmail: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val profileImage: String?
)
