package com.padym.rusread.di

import android.content.Context
import com.padym.rusread.SyllableMediaPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioModule {

    @Provides
    @Singleton
    fun provideMediaPlayer(@ApplicationContext context: Context): SyllableMediaPlayer {
        return SyllableMediaPlayer(context)
    }
}