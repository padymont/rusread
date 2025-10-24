package com.padym.rusread.di

import android.content.Context
import com.padym.rusread.SyllableMediaPlayer
import com.padym.rusread.data.SyllableRepository
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
    fun provideMediaPlayer(
        @ApplicationContext context: Context,
        syllableRepository: SyllableRepository
    ): SyllableMediaPlayer {
        return SyllableMediaPlayer(context, syllableRepository)
    }
}