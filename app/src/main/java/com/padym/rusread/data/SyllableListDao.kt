package com.padym.rusread.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SyllableListDao {

    @Insert
    suspend fun insert(entity: SyllableList)

    @Query("UPDATE syllable_list_table SET modified_at = :modifiedAt WHERE list = :list")
    suspend fun updateModifiedAt(list: Set<String>, modifiedAt: Long)

    @Query("SELECT * FROM syllable_list_table ORDER BY modified_at DESC")
    fun getEntries(): Flow<List<SyllableList>>

    @Query("SELECT * FROM syllable_list_table ORDER BY modified_at DESC LIMIT 1")
    suspend fun getLatestEntry(): SyllableList

    @Query("DELETE FROM syllable_list_table WHERE list = (SELECT list FROM syllable_list_table ORDER BY modified_at ASC LIMIT 1)")
    suspend fun deleteOldestEntry()

    @Query("SELECT COUNT(*) FROM syllable_list_table")
    suspend fun getEntryCount(): Int

    @Transaction
    suspend fun save(entity: SyllableList) {
        insert(entity)
        val count = getEntryCount()
        if (count > 5) {
            deleteOldestEntry()
        }
    }

    suspend fun update(list: Set<String>) {
        updateModifiedAt(list, System.currentTimeMillis())
    }
}