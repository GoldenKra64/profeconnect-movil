package com.example.myapplication.domain.model

data class PostAttachment(
    val id: Int,
    val filename: String,
    val mimeType: String,
    val url: String, // Mapeado desde 'path' en la BD
    val type: AttachmentType
)
