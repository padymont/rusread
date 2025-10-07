package com.padym.rusread.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.padym.rusread.data.AppDatabase
import com.padym.rusread.data.INITIAL_STAR_SCORE
import com.padym.rusread.data.StarScore
import com.padym.rusread.data.StarScoreDao
import com.padym.rusread.data.SyllableListDao
import com.padym.rusread.data.SyllableScoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        starScoreDaoProvider: Provider<StarScoreDao>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "my_database"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        starScoreDaoProvider.get()
                            .setScore(StarScore(id = 0, score = INITIAL_STAR_SCORE))
                    }
                }
            })
            .build()
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
}