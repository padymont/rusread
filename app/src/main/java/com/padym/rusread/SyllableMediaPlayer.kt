package com.padym.rusread

import android.content.Context
import android.media.MediaPlayer
import com.padym.rusread.data.SyllableRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyllableMediaPlayer(
    private val context: Context,
    private val repository: SyllableRepository
) {

    private var mediaPlayer: MediaPlayer? = null

    fun speakSyllable(text: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val syllable = repository.getSyllable(text)
            releasePlayer()
            if (syllable != null &&  syllable.resId.isNotBlank()) {
                val resourceId = context.resources.getIdentifier(
                    syllable.resId,
                    "raw",
                    context.packageName
                )
                if (resourceId != 0) {
                    playAudio(resourceId)
                }
            }
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