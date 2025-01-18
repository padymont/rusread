package com.padym.rusread.viewmodels

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.data.SyllableList
import com.padym.rusread.data.SyllableListDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MIN_SYLLABLES_COUNT = 3
const val MAX_SYLLABLES_COUNT = 10

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

    private var _chosenSyllables = mutableStateOf(setOf<String>())
    val chosenSyllables: Set<String>
        get() = _chosenSyllables.value

    val isSavingListAvailable by derivedStateOf { chosenSyllables.size >= MIN_SYLLABLES_COUNT }
    val isChoosingAvailable by derivedStateOf { chosenSyllables.size < MAX_SYLLABLES_COUNT }

    private val firstLetter = mutableStateOf("")
    private val secondLetter = mutableStateOf("")

    fun processChosenLetter(position: Position, letter: String) {
        when (position) {
            Position.FIRST -> firstLetter.value = letter
            Position.SECOND -> secondLetter.value = letter
        }
    }

    fun saveSyllable() {
        val syllable = "${firstLetter.value}${secondLetter.value}"
        _chosenSyllables.value += syllable
    }

    fun saveSyllableList() {
        viewModelScope.launch {
            dao.save(SyllableList(list = chosenSyllables))
        }
    }
}

enum class Position { FIRST, SECOND }