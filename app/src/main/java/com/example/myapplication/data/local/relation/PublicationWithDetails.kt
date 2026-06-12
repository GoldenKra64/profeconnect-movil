package com.example.myapplication.data.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.myapplication.data.local.entity.AttachmentEntity
import com.example.myapplication.data.local.entity.PublicationEntity
import com.example.myapplication.data.local.entity.PublicationTagCrossRef
import com.example.myapplication.data.local.entity.TagEntity

data class PublicationWithDetails(
    @Embedded val publication: PublicationEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "postId"
    )
    val attachments: List<AttachmentEntity>,

    // Nueva relación Muchos a Muchos
    @Relation(
        parentColumn = "id", // ID de PublicationEntity
        entityColumn = "id", // ID de TagEntity
        associateBy = Junction(
            value = PublicationTagCrossRef::class,
            parentColumn = "postId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagEntity>
)
