package com.padym.rusread.viewmodels

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.padym.rusread.R

const val RIGHT_ANSWER_NUMBER = 10
const val PROGRESS_OFFSET = 0.3f
const val SYLLABLE_LENGTH_MILLIS = 1200L

class SyllableGameViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application
    private val mediaPlayer = MediaPlayer.create(context, R.raw.all_syllables)

    private var _syllables = emptySet<String>()
    val syllables: Set<String>
        get() = _syllables

    private val _spokenSyllable = mutableStateOf("")
    val spokenSyllable: String
        get() = _spokenSyllable.value

    private val _correctAnswers = mutableIntStateOf(0)

    val correctAnswers: Int
        get() = _correctAnswers.intValue

    val gameProgress by derivedStateOf {
        (correctAnswers + PROGRESS_OFFSET) / (RIGHT_ANSWER_NUMBER + PROGRESS_OFFSET)
    }

    val isGameOn by derivedStateOf {
        correctAnswers < RIGHT_ANSWER_NUMBER
    }

    fun initializeData(data: Set<String>) {
        _syllables = data
        if (spokenSyllable.isEmpty()) {
            _spokenSyllable.value = syllables.random()
        }
    }

    fun processAnswer(syllable: String): Result {
        val isAnswerCorrect = syllable == spokenSyllable
        if (isAnswerCorrect) increaseCorrectAnswers() else decreaseCorrectAnswers()
        setNextSpokenSyllable()
        return if (isAnswerCorrect) Result.CORRECT else Result.WRONG
    }

    private fun increaseCorrectAnswers() = _correctAnswers.intValue++

    private fun decreaseCorrectAnswers() {
        if (correctAnswers > 0) _correctAnswers.intValue--
    }

    private fun setNextSpokenSyllable() {
        val tempSet = syllables.minus(spokenSyllable)
        _spokenSyllable.value = tempSet.random()
        speakSyllable(spokenSyllable)
    }

    fun speakSyllable(text: String) {
        val offset = Syllable.findOffset(text)
        playAudio(offset)
    }

    private fun playAudio(offset: Int) {
        mediaPlayer.seekTo(offset)
        mediaPlayer.start()
        Handler(Looper.getMainLooper())
            .postDelayed(
                { mediaPlayer.pause() },
                SYLLABLE_LENGTH_MILLIS
            )
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }
}

enum class Result { CORRECT, WRONG }