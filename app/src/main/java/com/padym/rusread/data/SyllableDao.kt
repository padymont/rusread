package com.padym.rusread.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SyllableDao {
    @Query("SELECT * FROM syllables")
    fun getAll(): Flow<List<Syllable>>

    @Query("SELECT * FROM syllables WHERE `key` = :key")
    suspend fun getSyllable(key: String): Syllable?

    @Upsert
    suspend fun insertAll(syllables: List<Syllable>)

    @Query("SELECT COUNT(*) FROM syllables")
    suspend fun getCount(): Int
}