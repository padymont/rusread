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
        val consonants = "бвгджзклмнпрстфхцчшщ"
        val vowels = "аеёиоуыюя"

        val consonantSyllableGroupsList = consonants.map { char ->
            val letter = char.toString()
            val syllables = vowels.map { letter + it } + (letter + "ь") + letter
            SyllableGroup(syllables)
        }
        val vowelSyllableGroup = SyllableGroup(vowels.map { it.toString() })
        val specialSyllableGroup = SyllableGroup(listOf("й", "ъ", "э"))

        return consonantSyllableGroupsList + vowelSyllableGroup + specialSyllableGroup
    }
}