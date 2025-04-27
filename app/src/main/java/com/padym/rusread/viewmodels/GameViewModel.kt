package com.padym.rusread.viewmodels

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.SyllableMediaPlayer
import com.padym.rusread.data.SyllableListDao
import com.padym.rusread.data.SyllableScoreDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val RIGHT_ANSWER_NUMBER = 10
const val PROGRESS_OFFSET = 0.3f

@HiltViewModel
class GameViewModel @Inject constructor(
    private val listDao: SyllableListDao,
    private val scoreDao: SyllableScoreDao,
    private val mediaPlayer: SyllableMediaPlayer,
) : ViewModel() {

    private var _syllables = mutableStateOf(emptySet<String>())
    val syllables: Set<String>
        get() = _syllables.value

    private val spokenSyllable = mutableStateOf("")
    private val _correctAnswers = mutableIntStateOf(0)

    val correctAnswers: Int
        get() = _correctAnswers.intValue

    val isAudioLoading by derivedStateOf {
        spokenSyllable.value.isEmpty()
    }

    val gameProgress by derivedStateOf {
        (correctAnswers + PROGRESS_OFFSET) / (RIGHT_ANSWER_NUMBER + PROGRESS_OFFSET)
    }

    val isGameOn by derivedStateOf {
        correctAnswers < RIGHT_ANSWER_NUMBER
    }

    init {
        viewModelScope.launch {
            _syllables.value = listDao.getLatestEntry().list
            syllables.forEach { scoreDao.save(it) }
            if (spokenSyllable.value.isEmpty()) {
                spokenSyllable.value = syllables.random()
            }
        }
    }

    fun processAnswer(syllable: String): Result {
        val result: Result
        val isAnswerCorrect = syllable == spokenSyllable.value
        when (isAnswerCorrect) {
            true -> {
                increaseSyllableScore(syllable)
                increaseCorrectAnswers()
                result = Result.CORRECT
            }

            false -> {
                lowerSyllableScore(syllable)
                decreaseCorrectAnswers()
                result = Result.WRONG
            }
        }
        if (isGameOn) {
            setNextSpokenSyllable()
        }
        return result
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
        val tempSet = syllables.minus(spokenSyllable.value)
        spokenSyllable.value = tempSet.random()
        speakSyllable()
    }

    fun speakSyllable() {
        mediaPlayer.speakSyllable(spokenSyllable.value)
    }
}

enum class Result { CORRECT, WRONG }