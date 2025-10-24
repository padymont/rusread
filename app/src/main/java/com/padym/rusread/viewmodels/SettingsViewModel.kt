package com.padym.rusread.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.data.SyllableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val syllableRepository: SyllableRepository,
) : ViewModel() {

    val currentStarScore = syllableRepository.getCurrentScore().stateIn(initialValue = 0)

    private var _isTooltipOn = mutableStateOf(false)
    val isTooltipOn: Boolean
        get() = _isTooltipOn.value

    fun setStarScore(newScore: Int) = viewModelScope.launch {
        syllableRepository.setNewScore(newScore)
    }

    fun clearProgress() = viewModelScope.launch {
        syllableRepository.clearAllEntries()
    }

    fun showTooltip() {
        _isTooltipOn.value = true
    }

    fun hideTooltip() {
        _isTooltipOn.value = false
    }

    private fun <T> Flow<T>.stateIn(initialValue: T) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialValue
    )
}