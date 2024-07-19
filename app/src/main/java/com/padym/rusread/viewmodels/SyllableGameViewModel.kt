package com.padym.rusread.viewmodels

import android.app.Application
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import java.util.Locale

const val RIGHT_ANSWER_NUMBER = 40

class SyllableGameViewModel(application: Application) : AndroidViewModel(application) {

    private val _selectedSyllables = mutableStateOf<Set<String>>(emptySet())
    val selectedSyllables: Set<String>
        get() = _selectedSyllables.value

    private val _newSyllable = mutableStateOf("")
    val newSyllable: String
        get() {
            if (_newSyllable.value.isEmpty()) setNewSyllable()
            return _newSyllable.value
        }

    private val _correctAnswers = mutableIntStateOf(0)
    val correctAnswers: Int
        get() = _correctAnswers.intValue

    fun initializeData(data: Set<String>) {
        _selectedSyllables.value = data
    }

    fun setNewSyllable() {
        _newSyllable.value = selectedSyllables.random()
    }

    fun increaseCorrectAnswers() = _correctAnswers.intValue++

    private val context = application
    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale("ru"))
                val voice = textToSpeech.voices?.find { it.name.contains("ru-ru-x-rud-network") }
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