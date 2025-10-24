package com.padym.rusread.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Syllable::class, SyllableList::class, SyllableScore::class, StarScore::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringSetConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun syllableDao(): SyllableDao

    abstract fun syllableListDao(): SyllableListDao

    abstract fun syllableScoreDao(): SyllableScoreDao

    abstract fun starScoreDao(): StarScoreDao
}