package com.padym.rusread

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import com.padym.rusread.compose.RusreadApp
import com.padym.rusread.ui.theme.RusreadTheme

class MainActivity : ComponentActivity() {

    private lateinit var tts: MutableState<TextToSpeech?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RusreadTheme {
                RusreadApp()
            }
        }
    }
}