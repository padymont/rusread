package com.padym.rusread.data

import androidx.room.Entity
import androidx.room.PrimaryKey

const val INITIAL_STAR_SCORE = 10

@Entity(tableName = "star_score_table")
data class StarScore(
    @PrimaryKey
    val id: Int = 0,
    val score: Int
)