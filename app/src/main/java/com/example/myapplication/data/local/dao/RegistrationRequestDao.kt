package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.local.entity.RegistrationRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistrationRequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RegistrationRequestEntity)

    @Query("SELECT * FROM solicitudes_registro ORDER BY created_at DESC")
    fun observarTodas(): Flow<List<RegistrationRequestEntity>>

    @Query("SELECT * FROM solicitudes_registro WHERE status = :status ORDER BY created_at DESC")
    fun observarPorEstado(status: String): Flow<List<RegistrationRequestEntity>>
}
