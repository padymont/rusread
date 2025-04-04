package com.padym.rusread

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.padym.rusread.viewmodels.Syllable

const val SYLLABLE_LENGTH_MILLIS = 1200L

class SyllableMediaPlayer(context: Context) {

    private val mediaPlayer = MediaPlayer.create(context, R.raw.all_syllables)
    private var currentOffset: Int = 0

    fun speakSyllable(text: String) {
        val offset = Syllable.findOffset(text)
        playAudio(offset)
    }

    private fun playAudio(offset: Int) {
        currentOffset = offset
        mediaPlayer.seekTo(offset)
        mediaPlayer.start()
        Handler(Looper.getMainLooper())
            .postDelayed(
                {
                    if (offset == currentOffset) {
                        mediaPlayer.pause()
                    }
                },
                SYLLABLE_LENGTH_MILLIS
            )
    }
}