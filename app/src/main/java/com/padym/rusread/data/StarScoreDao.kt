package com.padym.rusread.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StarScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setScore(starScore: StarScore)

    @Query("SELECT * FROM star_score_table WHERE id = 0")
    fun getScore(): Flow<StarScore?>
}