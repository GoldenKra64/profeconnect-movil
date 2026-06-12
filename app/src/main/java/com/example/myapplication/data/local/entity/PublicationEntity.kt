package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "publications")
data class PublicationEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val content: String,
    val isAnonymous: Boolean,
    val authorName: String?,
    val createdAt: String
)
