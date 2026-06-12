package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "publication_tags",
    primaryKeys = ["postId", "tagId"],
    indices = [Index("tagId")] // Índice para acelerar las búsquedas por etiqueta
)
data class PublicationTagCrossRef(
    val postId: Int,
    val tagId: Int
)