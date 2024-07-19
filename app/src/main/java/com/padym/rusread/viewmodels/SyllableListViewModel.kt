package com.padym.rusread.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

const val MIN_CHOSEN_SYLLABLES = 3
const val MAX_CHOSEN_SYLLABLES = 5

class SyllableListViewModel : ViewModel() {

    private val _selectedSyllables = mutableStateOf<Set<String>>(emptySet())
    val selectedSyllables: Set<String>
        get() = _selectedSyllables.value

    private val _isSelectionEnabled = mutableStateOf(true)
    val isSelectionEnabled: Boolean
        get() = _isSelectionEnabled.value

    fun changeSyllableSelection(syllable: String) {
        if (syllable in _selectedSyllables.value) {
            _selectedSyllables.value -= syllable
        } else {
            _selectedSyllables.value += syllable
        }
        _isSelectionEnabled.value = selectedSyllables.size < MAX_CHOSEN_SYLLABLES
    }

    fun clearChosenSyllables() {
        _selectedSyllables.value = emptySet()
    }

    data class SyllableGroup(val syllables: List<String>)

    fun getGroupedSyllables(): List<SyllableGroup> {
        val allSyllables = getSyllables()
        val vowels = "аеёиоуыэюя"

        val syllableGroupsList = allSyllables.map { it.key }
            .filter { it.length > 1 }
            .groupBy { it.first() }
            .map { entry ->
                SyllableGroup(syllables = entry.value)
            }

        val vowelSyllableGroup = SyllableGroup(
            allSyllables.map { it.key }
                .filter { it in vowels }
        )
        val consonantSyllableGroup = SyllableGroup(
            allSyllables.map { it.key }
                .filter { it.length == 1 }
                .filter { it !in vowels }
        )

        return syllableGroupsList + vowelSyllableGroup + consonantSyllableGroup
    }
}
