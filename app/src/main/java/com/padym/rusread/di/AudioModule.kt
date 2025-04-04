package com.padym.rusread.di

import android.content.Context
import android.media.MediaPlayer
import com.padym.rusread.R
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
    fun provideMediaPlayer(@ApplicationContext context: Context): MediaPlayer {
        return MediaPlayer.create(context, R.raw.all_syllables)
    }
}