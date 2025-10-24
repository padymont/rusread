package com.padym.rusread.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [SyllableGroup::class, SyllableScore::class, StarScore::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringSetConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun syllableGroupDao(): SyllableGroupDao

    abstract fun syllableScoreDao(): SyllableScoreDao

    abstract fun starScoreDao(): StarScoreDao
}