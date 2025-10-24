package com.padym.rusread.di

import android.content.Context
import androidx.room.Room
import com.padym.rusread.data.AppDatabase
import com.padym.rusread.data.SyllableGroupDao
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
    fun provideSyllableGroupDao(appDatabase: AppDatabase): SyllableGroupDao {
        return appDatabase.syllableGroupDao()
    }

    @Provides
    fun provideSyllableScoreDao(appDatabase: AppDatabase): SyllableScoreDao {
        return appDatabase.syllableScoreDao()
    }
}