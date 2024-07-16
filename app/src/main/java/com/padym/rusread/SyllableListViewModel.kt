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
