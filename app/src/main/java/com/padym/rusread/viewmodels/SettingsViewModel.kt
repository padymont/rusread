package com.padym.rusread.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.data.StarScoreDao
import com.padym.rusread.data.SyllableScoreDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val starScoreDao: StarScoreDao,
    private val scoreDao: SyllableScoreDao
) : ViewModel() {

    val currentStarScore = starScoreDao.getCurrentScore().stateIn(initialValue = 0)

    fun setStarScore(newScore: Int) = viewModelScope.launch {
        starScoreDao.setNewScore(newScore)
    }

    private fun <T> Flow<T>.stateIn(initialValue: T) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialValue
    )
}