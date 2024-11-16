package com.padym.rusread.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SyllableListDao {

    @Insert
    suspend fun insert(entity: SyllableList)

    @Query("SELECT * FROM syllable_list_table ORDER BY id DESC")
    suspend fun getEntries(): List<SyllableList>?

    @Query("SELECT * FROM syllable_list_table ORDER BY id ASC LIMIT 1")
    suspend fun getOldestEntry(): SyllableList?

    @Query("DELETE FROM syllable_list_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM syllable_list_table")
    suspend fun getEntryCount(): Int

    @Transaction
    suspend fun save(entity: SyllableList) {
        insert(entity)
        val count = getEntryCount()
        if (count > 5) {
            val oldestEntry = getOldestEntry()
            oldestEntry?.let { deleteById(it.id) }
        }
    }
}