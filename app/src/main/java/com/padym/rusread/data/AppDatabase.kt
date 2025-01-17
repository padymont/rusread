package com.padym.rusread.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SyllableList::class, SyllableScore::class], version = 1, exportSchema = false)
@TypeConverters(StringSetConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun syllableListDao(): SyllableListDao

    abstract fun syllableScoreDao(): SyllableScoreDao
}