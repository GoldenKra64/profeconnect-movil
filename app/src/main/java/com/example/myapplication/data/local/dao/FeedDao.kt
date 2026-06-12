package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.data.local.entity.AttachmentEntity
import com.example.myapplication.data.local.entity.PublicationEntity
import com.example.myapplication.data.local.entity.PublicationTagCrossRef
import com.example.myapplication.data.local.entity.TagEntity
import com.example.myapplication.data.local.relation.PublicationWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedDao {
    @Transaction
    @Query("SELECT * FROM publications ORDER BY createdAt DESC")
    fun getPublicationsStream(): Flow<List<PublicationWithDetails>>

    // Consulta definitiva para filtrar (El INNER JOIN ahora sí funcionará)
    @Transaction
    @Query("""
        SELECT p.* FROM publications p 
        INNER JOIN publication_tags pt ON p.id = pt.postId 
        WHERE pt.tagId = :tagId 
        ORDER BY p.createdAt DESC
    """)
    fun getPublicationsByTagStream(tagId: Int): Flow<List<PublicationWithDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPublications(publications: List<PublicationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachments(attachments: List<AttachmentEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTags(tags: List<TagEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPublicationTagCrossRefs(crossRefs: List<PublicationTagCrossRef>)
}