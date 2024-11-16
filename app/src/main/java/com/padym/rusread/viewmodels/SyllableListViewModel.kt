package com.padym.rusread.viewmodels

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

const val MIN_CHOSEN_SYLLABLES = 3
const val MAX_CHOSEN_SYLLABLES = 10
const val VOWELS = "аеёиоуыэюя"

class SyllableListViewModel : ViewModel() {

    //    private val _selectedSyllables = mutableStateOf<Set<String>>(emptySet())
    private val _selectedSyllables = mutableStateOf(getRandomSet())
    private fun getRandomSet() = listOf(
        setOf("ба", "бо", "во", "вы", "га", "ги", "да", "до", "ду", "дь"),
        setOf("бе", "би", "бу", "бы", "ва", "ве", "ви", "де", "ди", "дь")
    ).random()

    val selectedSyllables: Set<String>
        get() = _selectedSyllables.value

    val selectedSyllablesCount by derivedStateOf { selectedSyllables.size }
    val isEnoughSyllablesSelected by derivedStateOf { selectedSyllables.size >= MIN_CHOSEN_SYLLABLES }
    val isSelectionEnabled by derivedStateOf { selectedSyllables.size < MAX_CHOSEN_SYLLABLES }
    val isClearSelectionEnabled by derivedStateOf { selectedSyllables.isNotEmpty() }

    fun changeSyllableSelection(syllable: String) {
        if (syllable in _selectedSyllables.value) {
            _selectedSyllables.value -= syllable
        } else {
            _selectedSyllables.value += syllable
        }
    }

    fun clearChosenSyllables() {
        _selectedSyllables.value = emptySet()
    }

    data class SyllableGroup(val syllables: List<String>)

    fun getGroupedSyllables(): List<SyllableGroup> {
        val allSyllables = Syllable.getAll()
        val syllableGroupsList = allSyllables.map { it.key }
            .filter { it.length > 1 }
            .groupBy { it.first() }
            .map { entry ->
                SyllableGroup(syllables = entry.value)
            }
        val vowelSyllableGroup = SyllableGroup(
            allSyllables.map { it.key }
                .filter { it in VOWELS }
        )
        val consonantSyllableGroup = SyllableGroup(
            allSyllables.map { it.key }
                .filter { it.length == 1 }
                .filter { it !in VOWELS }
        )
        return syllableGroupsList + vowelSyllableGroup + consonantSyllableGroup
    }
}
