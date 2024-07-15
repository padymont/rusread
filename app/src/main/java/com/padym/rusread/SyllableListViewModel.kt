package com.padym.rusread

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SyllableListViewModel : ViewModel() {

    private val _selectedSyllables = mutableStateOf<Set<String>>(emptySet())
    val selectedSyllables: Set<String>
        get() = _selectedSyllables.value

    fun addSyllable(syllable: String) {
        _selectedSyllables.value += syllable
    }

    fun removeSyllable(syllable: String) {
        _selectedSyllables.value -= syllable
    }

    fun clearChosenSyllables() {
        _selectedSyllables.value = emptySet()
    }
}