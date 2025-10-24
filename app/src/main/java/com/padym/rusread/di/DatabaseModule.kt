package com.padym.rusread.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.padym.rusread.data.AppDatabase
import com.padym.rusread.data.StarScoreDao
import com.padym.rusread.data.Syllable
import com.padym.rusread.data.SyllableDao
import com.padym.rusread.data.SyllableListDao
import com.padym.rusread.data.SyllableScoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "my_database"
        ).build()
    }

    @Provides
    fun provideSyllableDao(appDatabase: AppDatabase): SyllableDao {
        return appDatabase.syllableDao()
    }

    @Provides
    fun provideSyllableListDao(appDatabase: AppDatabase): SyllableListDao {
        return appDatabase.syllableListDao()
    }

    @Provides
    fun provideSyllableScoreDao(appDatabase: AppDatabase): SyllableScoreDao {
        return appDatabase.syllableScoreDao()
    }

    @Provides
    fun provideStarScoreDao(appDatabase: AppDatabase): StarScoreDao {
        return appDatabase.starScoreDao()
    }

    private suspend fun prePopulateSyllables(context: Context, syllableDao: SyllableDao) {

        if (syllableDao.getCount() > 0) return

        data class SyllableDto(val key: String, val resId: String)

        val jsonString = context.assets
            .open("syllables.json")
            .bufferedReader()
            .use { it.readText() }
        val syllableDtos = Gson().fromJson<List<SyllableDto>>(
            jsonString,
            object : TypeToken<List<SyllableDto>>() {}.type
        )
        val syllables = syllableDtos.map { dto ->
            Syllable(key = dto.key, resId = dto.resId)
        }
        syllableDao.insertAll(syllables)
    }
}