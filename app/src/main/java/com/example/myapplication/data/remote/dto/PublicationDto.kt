package com.example.myapplication.data.remote.dto

data class PublicationDto(
    val id: Int,
    val title: String,
    val content: String,
    val isAnonymous: Boolean,
    val authorName: String, // Asegúrate de que el backend envíe esto
    val createdAt: String, // o String si el backend manda ISO 8601
    val attachments: List<AttachmentDto> = emptyList(),
    val tags: List<TagDto> = emptyList(),
    val reactionCount: Int = 0
)

data class AttachmentDto(
    val id: Int,
    val filename: String,
    val mimeType: String,
    val url: String, // Mapeado del 'path' de tu backend
    val type: String
)

data class TagDto(
    val id: Int,
    val name: String
)

data class PaginatedPublicationsDto(
    val items: List<PublicationDto>,
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int
)
