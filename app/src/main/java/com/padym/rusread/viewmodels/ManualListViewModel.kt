package com.padym.rusread.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.data.SyllableList
import com.padym.rusread.data.SyllableListDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManualListViewModel @Inject constructor(
    private val dao: SyllableListDao
) : ViewModel() {

    val firstLetterList = listOf(
        "б", "в", "г", "д", "ж", "з", "к", "л", "м", "н", "п", "р",
        "с", "т", "ф", "х", "ц", "ч", "ш", "щ"
    )
    val secondLetterOptions = listOf(
        "а", "е", "и", "о", "у", "ы", "э", "ю", "я"
    )

    private val firstLetter = mutableStateOf("")
    private val secondLetter = mutableStateOf("")
    private val chosenSyllables = mutableStateOf(setOf<String>())

    fun processChosenLetter(position: Position, letter: String) {
        when (position) {
            Position.FIRST -> firstLetter.value = letter
            Position.SECOND -> secondLetter.value = letter
        }
    }

    fun saveSyllable() {
        val syllable = "${firstLetter.value}${secondLetter.value}"
        chosenSyllables.value += syllable
    }

    fun saveSyllableList() {
        viewModelScope.launch {
            dao.save(SyllableList(list = chosenSyllables.value))
        }
    }
}

enum class Position { FIRST, SECOND }