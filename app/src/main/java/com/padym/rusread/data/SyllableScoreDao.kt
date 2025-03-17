package com.padym.rusread.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val HIGH_SCORE = 3

@Dao
interface SyllableScoreDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: SyllableScore)

    @Query("UPDATE syllable_score_table SET score = :score, modified_at = :modifiedAt WHERE syllable = :syllable")
    suspend fun updateScore(syllable: String, score: Int, modifiedAt: Long)

    @Query("SELECT * FROM syllable_score_table WHERE syllable = :syllable")
    suspend fun getEntry(syllable: String): SyllableScore

    @Query("SELECT * FROM syllable_score_table WHERE score > $HIGH_SCORE - 1")
    fun getHighScoreEntriesOld(): List<SyllableScore>

    @Query("SELECT * FROM syllable_score_table WHERE score > $HIGH_SCORE - 1")
    fun getHighScoreEntries(): Flow<List<SyllableScore>>

    @Query("SELECT * FROM syllable_score_table WHERE syllable IN (:syllables)")
    suspend fun getEntriesScores(syllables: Set<String>): List<SyllableScore>

    suspend fun save(syllable: String) = insert(SyllableScore(syllable))

    suspend fun update(syllable: String, score: Int) {
        updateScore(syllable, score, System.currentTimeMillis())
    }

    suspend fun lowerScore(syllable: String) {
        val entry = getEntry(syllable)
        if (entry.score > 0) {
            update(entry.syllable, entry.score - 1)
        }
    }

    suspend fun increaseScore(syllable: String) {
        val entry = getEntry(syllable)
        update(entry.syllable, entry.score + 1)
    }

    suspend fun getScores(syllables: Set<String>) = getEntriesScores(syllables).map {
        Pair(it.syllable, it.score)
    }

    suspend fun getHighScoreSyllablesOld() = getHighScoreEntriesOld()?.map { it.syllable }

    fun getHighScoreSyllables() = getHighScoreEntries().map {
        list -> list.map { it.syllable }
    }
}