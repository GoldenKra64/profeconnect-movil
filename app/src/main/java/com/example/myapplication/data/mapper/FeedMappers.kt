package com.example.myapplication.data.mapper

import com.example.myapplication.data.local.entity.AttachmentEntity
import com.example.myapplication.data.local.entity.PublicationEntity
import com.example.myapplication.data.local.entity.TagEntity
import com.example.myapplication.data.local.relation.PublicationWithDetails
import com.example.myapplication.data.remote.dto.AttachmentDto
import com.example.myapplication.data.remote.dto.PublicationDto
import com.example.myapplication.domain.model.AttachmentType
import com.example.myapplication.domain.model.PostAttachment
import com.example.myapplication.domain.model.Publication
import com.example.myapplication.data.remote.dto.TagDto
import com.example.myapplication.domain.model.Tag

// DTO -> Entity (De Internet a Base de Datos)
fun PublicationDto.toEntity(): PublicationEntity {
    return PublicationEntity(
        id = id,
        title = title,
        content = content,
        isAnonymous = isAnonymous,
        authorName = authorName,
        createdAt = createdAt
    )
}

fun AttachmentDto.toEntity(postId: Int): AttachmentEntity {
    return AttachmentEntity(
        id = id,
        postId = postId,
        filename = filename,
        mimeType = mimeType,
        path = url,
        type = type
    )
}

fun TagDto.toEntity(): TagEntity = TagEntity(id = id, name = name)

fun TagEntity.toDomain(): Tag = Tag(id = id, name = name)


// Entity -> Domain (De Base de Datos a la UI)
fun PublicationWithDetails.toDomain(): Publication {
    return Publication(
        id = publication.id,
        title = publication.title,
        content = publication.content,
        isAnonymous = publication.isAnonymous,
        authorName = publication.authorName ?: "Anónimo",
        attachments = attachments.map {
            PostAttachment(
                id = it.id,
                filename = it.filename,
                mimeType = it.mimeType,
                url = it.path,
                type = AttachmentType.valueOf(it.type)
            )
        },
        tags = tags.map { it.toDomain() },
        createdAt = publication.createdAt,
        reactionCount = 0
    )
}