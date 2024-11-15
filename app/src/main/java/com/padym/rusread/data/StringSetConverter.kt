package com.padym.rusread.data

import androidx.room.TypeConverter

class StringSetConverter {

    @TypeConverter
    fun fromString(value: String?): Set<String>? {
        return value?.split(",")?.toSet()
    }

    @TypeConverter
    fun toString(set: Set<String>?): String? {
        return set?.joinToString(",")
    }
}