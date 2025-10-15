package com.padym.rusread.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
interface StarScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setScore(starScore: StarScore)

    @Query("SELECT * FROM star_score_table WHERE id = 0")
    fun getScore(): Flow<StarScore?>

    suspend fun setNewScore(newScore: Int) = setScore(StarScore(score = newScore))

    fun getCurrentScore() = getScore().map { starScore ->
        starScore?.score ?: INITIAL_STAR_SCORE
    }
}