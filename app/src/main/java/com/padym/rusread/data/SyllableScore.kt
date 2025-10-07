package com.padym.rusread.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "syllable_score_table")
data class SyllableScore(
    @PrimaryKey val syllable: String,
    val score: Int = 0
)