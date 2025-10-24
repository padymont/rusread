package com.padym.rusread

import android.content.Context
import android.media.MediaPlayer
import com.padym.rusread.data.SyllableRepository

class SyllableMediaPlayer(
    private val context: Context,
    private val syllableRepository: SyllableRepository
) {

    private var mediaPlayer: MediaPlayer? = null

    fun speakSyllable(text: String) {
        releasePlayer()
        val resourceId = syllableRepository.getSyllableResourceId(text)

        if (resourceId != 0) {
            playAudio(resourceId)
        }
    }

    private fun playAudio(resourceId: Int) {
        mediaPlayer = MediaPlayer.create(context, resourceId)
        mediaPlayer?.setOnCompletionListener {
            releasePlayer()
        }
        mediaPlayer?.start()
    }

    fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}