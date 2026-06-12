package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.local.dao.FeedDao
import com.example.myapplication.data.local.entity.PublicationTagCrossRef // NUEVO IMPORT
import com.example.myapplication.data.mapper.toDomain
import com.example.myapplication.data.mapper.toEntity
import com.example.myapplication.data.remote.api.FeedApi
import com.example.myapplication.domain.model.Publication
import com.example.myapplication.domain.repository.FeedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class FeedRepositoryImpl(
    private val api: FeedApi,
    private val dao: FeedDao,
    private val context: Context // Para procesar URIs de archivos si es necesario
) : FeedRepository {

    override fun getFeedStream(tagId: Int?): Flow<List<Publication>> {
        val flow = if (tagId != null) {
            dao.getPublicationsByTagStream(tagId)
        } else {
            dao.getPublicationsStream()
        }

        return flow.map { entities ->
            entities.map { it.toDomain() } // Mapper de PublicationWithDetails a Publication
        }
    }

    override suspend fun refreshFeed(tagId: Int?): Result<Unit> {
        return try {
            val response = api.getFeed(tagId)
            val publications = response.data?.items ?: emptyList()

            // Guardar transaccionalmente en Room
            dao.insertPublications(publications.map { it.toEntity() })

            publications.forEach { pub ->
                // 1. Guardar los adjuntos
                dao.insertAttachments(pub.attachments.map { it.toEntity(pub.id) })

                // 2. Extraer y mapear las etiquetas (NUEVO)
                val tags = pub.tags.map { it.toEntity() }

                // 3. Crear las relaciones en la tabla puente (NUEVO)
                val crossRefs = pub.tags.map {
                    PublicationTagCrossRef(postId = pub.id, tagId = it.id)
                }

                // 4. Guardar etiquetas y relaciones en Room (NUEVO)
                dao.insertTags(tags)
                dao.insertPublicationTagCrossRefs(crossRefs)
            }
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createPublication(
        title: String, content: String, isAnonymous: Boolean, tagIds: List<Int>, files: List<File>
    ): Result<Unit> {
        return try {
            val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val contentPart = content.toRequestBody("text/plain".toMediaTypeOrNull())
            val anonPart = isAnonymous.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val tagsPart = tagIds.joinToString(",").toRequestBody("text/plain".toMediaTypeOrNull())

            // Convertir archivos físicos a MultipartBody.Part para Retrofit
            val fileParts = files.map { file ->
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull()) // Ajustar MimeType dinámicamente
                MultipartBody.Part.createFormData("files", file.name, requestFile)
            }

            api.createPublication(titlePart, contentPart, anonPart, tagsPart, fileParts)
            // Después de crear exitosamente, forzamos la actualización de la base de datos local
            refreshFeed()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}