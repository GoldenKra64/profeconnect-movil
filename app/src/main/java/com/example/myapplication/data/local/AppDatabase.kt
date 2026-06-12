package com.example.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.local.dao.FeedDao
import com.example.myapplication.data.local.entity.AttachmentEntity
import com.example.myapplication.data.local.entity.PublicationEntity
import com.example.myapplication.data.local.entity.PublicationTagCrossRef
import com.example.myapplication.data.local.entity.TagEntity
import com.example.myapplication.data.local.dao.ProfileDao
import com.example.myapplication.data.local.entity.ProfileEntity

@Database(
    entities = [
        // Tus tablas exclusivas para el Feed:
        PublicationEntity::class,
        AttachmentEntity::class,
        TagEntity::class,
        PublicationTagCrossRef::class,
        ProfileEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Exponemos tu DAO
    abstract fun feedDao(): FeedDao
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "profeconnect_test.db" // Base de datos temporal para tus pruebas
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}