package com.padym.rusread.viewmodels

import androidx.lifecycle.ViewModel
import com.padym.rusread.SyllableMediaPlayer
import com.padym.rusread.data.SyllableListDao
import com.padym.rusread.data.SyllableScoreDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val listDao: SyllableListDao,
    private val scoreDao: SyllableScoreDao,
    private val mediaPlayer: SyllableMediaPlayer,
) : ViewModel() {
}