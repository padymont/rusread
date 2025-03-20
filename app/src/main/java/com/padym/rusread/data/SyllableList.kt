package com.padym.rusread.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "syllable_list_table")
@TypeConverters(StringSetConverter::class)
data class SyllableList(
    @PrimaryKey val list: Set<String> = setOf(),
    @ColumnInfo(name = "modified_at")
    val modifiedAt:Long = System.currentTimeMillis()
)