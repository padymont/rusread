package com.padym.rusread.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SyllableList::class], version = 1, exportSchema = false)
@TypeConverters(StringSetConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun syllableListDao(): SyllableListDao

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rus_read_db"
                ).build()
                INSTANCE = instance
                return instance
            }}
    }
}