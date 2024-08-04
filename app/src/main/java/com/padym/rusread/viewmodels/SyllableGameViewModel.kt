package com.padym.rusread.viewmodels

import android.app.Application
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import java.util.Locale

const val RIGHT_ANSWER_NUMBER = 10
const val PROGRESS_OFFSET = 0.3f

class SyllableGameViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application
    private lateinit var textToSpeech: TextToSpeech

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale("ru"))
                val voice =
                    textToSpeech.voices?.find { it.name.contains("ru-ru-x-rud-network") }
                textToSpeech.setVoice(voice)
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Toast.makeText(
                        context,
                        "Russian language not supported",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Toast.makeText(context, "TTS initialization failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val _syllables = mutableStateOf<Set<String>>(emptySet())
    val syllables: Set<String>
        get() = _syllables.value

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
        _syllables.value = data
        _spokenSyllable.value = syllables.random()
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
    }

    fun speakText(text: String) = textToSpeech.speak(
        text,
        TextToSpeech.QUEUE_FLUSH,
        null,
        null
    )

    override fun onCleared() {
        super.onCleared()
        textToSpeech.shutdown()
    }
}

enum class Result { CORRECT, WRONG }