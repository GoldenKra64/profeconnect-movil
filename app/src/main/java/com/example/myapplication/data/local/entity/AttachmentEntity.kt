package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attachments",
    foreignKeys = [
        ForeignKey(
            entity = PublicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("postId")]
)
data class AttachmentEntity(
    @PrimaryKey val id: Int,
    val postId: Int,
    val filename: String,
    val mimeType: String,
    val path: String,
    val type: String // "IMAGE" o "DOCUMENT"
)
