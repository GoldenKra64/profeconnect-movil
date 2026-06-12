package com.example.myapplication.domain.model

data class Publication(
    val id: Int,
    val title: String,
    val content: String,
    val isAnonymous: Boolean,
    val authorName: String,
    val attachments: List<PostAttachment>,
    val tags: List<Tag>,
    val createdAt: String,
    val reactionCount: Int
)
