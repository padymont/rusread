package com.padym.rusread.viewmodels

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.R
import com.padym.rusread.data.SyllableListDao
import com.padym.rusread.data.SyllableScoreDao
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

const val RIGHT_ANSWER_NUMBER = 10
const val PROGRESS_OFFSET = 0.3f
const val SYLLABLE_LENGTH_MILLIS = 1200L

@HiltViewModel
class GameViewModel @Inject constructor(
    private val listDao: SyllableListDao,
    private val scoreDao: SyllableScoreDao,
    @ApplicationContext private val context: Context
) : ViewModel() {
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

    fun initializeData() = viewModelScope.launch {
        _syllables = listDao.getLatestEntry().list
        syllables.forEach { scoreDao.save(it) }
        if (spokenSyllable.isEmpty()) {
            _spokenSyllable.value = syllables.random()
        }
    }

    fun processAnswer(syllable: String): Result {
        val isAnswerCorrect = syllable == spokenSyllable
        if (isGameOn) setNextSpokenSyllable()
        return when (isAnswerCorrect) {
            true -> {
                increaseSyllableScore(syllable)
                increaseCorrectAnswers()
                Result.CORRECT
            }

            false -> {
                lowerSyllableScore(syllable)
                decreaseCorrectAnswers()
                Result.WRONG
            }
        }
    }

    private fun increaseCorrectAnswers() = _correctAnswers.intValue++

    private fun decreaseCorrectAnswers() {
        if (correctAnswers > 0) _correctAnswers.intValue--
    }

    private fun increaseSyllableScore(syllable: String) = viewModelScope.launch {
        scoreDao.increaseScore(syllable)
    }

    private fun lowerSyllableScore(syllable: String) = viewModelScope.launch {
        scoreDao.lowerScore(syllable)
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