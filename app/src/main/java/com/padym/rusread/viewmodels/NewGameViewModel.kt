package com.padym.rusread.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class NewGameViewModel(application: Application) : AndroidViewModel(application)  {

    private val _selectedSyllables = mutableStateOf(getRandomSyllableSelection())
    val selectedSyllables: Set<String>
        get() = _selectedSyllables.value

    private fun getRandomSyllableSelection() = Syllable
        .getAll()
        .map { it.key }
        .filter { it.length > 1 }
        .shuffled()
        .slice(0..9)
        .sorted()
        .toSet()
}