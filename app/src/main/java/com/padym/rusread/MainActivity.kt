package com.padym.rusread

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.padym.rusread.compose.RusreadApp
import com.padym.rusread.ui.theme.RusreadTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RusreadTheme {
                RusreadApp()
            }
        }
    }
}