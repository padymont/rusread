package com.padym.rusread

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SyllableGameViewModel : ViewModel() {

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
}