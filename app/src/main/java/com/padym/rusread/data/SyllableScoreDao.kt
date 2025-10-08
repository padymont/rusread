package com.padym.rusread.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SyllableScoreDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: SyllableScore)

    @Query("UPDATE syllable_score_table SET score = :score WHERE syllable = :syllable")
    suspend fun updateScore(syllable: String, score: Int)

    @Query("SELECT * FROM syllable_score_table WHERE syllable = :syllable")
    suspend fun getEntry(syllable: String): SyllableScore

    @Query("SELECT * FROM syllable_score_table WHERE score >= :highScore")
    fun getHighScoreEntries(highScore: Int): Flow<List<SyllableScore>>

    @Query("SELECT * FROM syllable_score_table WHERE syllable IN (:syllables)")
    suspend fun getEntriesScores(syllables: Set<String>): List<SyllableScore>

    @Query("DELETE FROM syllable_score_table")
    suspend fun clearAllEntries()
}