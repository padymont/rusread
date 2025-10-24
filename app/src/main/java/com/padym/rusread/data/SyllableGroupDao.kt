package com.padym.rusread.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SyllableGroupDao {

    @Insert
    suspend fun insert(entity: SyllableGroup)

    @Query("UPDATE syllable_list_table SET modified_at = :modifiedAt WHERE id = :id")
    suspend fun updateModifiedAt(id: Int, modifiedAt: Long)

    @Query("SELECT * FROM syllable_list_table ORDER BY modified_at DESC")
    fun getEntries(): Flow<List<SyllableGroup>>

    @Query("SELECT * FROM syllable_list_table ORDER BY modified_at DESC LIMIT 1")
    suspend fun getLatestEntry(): SyllableGroup

    @Query("DELETE FROM syllable_list_table WHERE id = (SELECT id FROM syllable_list_table ORDER BY modified_at ASC LIMIT 1)")
    suspend fun deleteOldestEntry()

    @Query("SELECT COUNT(*) FROM syllable_list_table")
    suspend fun getEntryCount(): Int

    @Transaction
    suspend fun save(entity: SyllableGroup) {
        insert(entity)
        val count = getEntryCount()
        if (count > 5) {
            deleteOldestEntry()
        }
    }
}