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
const val EMPTY_SIGN = "🎈"

@HiltViewModel
class ManualListViewModel @Inject constructor(
    private val dao: SyllableListDao
) : ViewModel() {

    val firstLetterList = listOf(
        "б", "в", "г", "д", "ж", "з", "й", "к", "л", "м", "н", "п", "р",
        "с", "т", "ф", "х", "ц", "ч", "ш", "щ", EMPTY_SIGN
    )
    val secondLetterOptions = listOf(
        "а", "е", "ё", "и", "о", "у", "ы", "ь", "ъ", "э", "ю", "я", EMPTY_SIGN
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
        val unfilteredString = "${firstLetter.value}${secondLetter.value}"
        val syllable = unfilteredString.replace(EMPTY_SIGN, "")
        if (syllable.isNotEmpty()) {
            _chosenSyllables.value += syllable
        }
    }

    fun saveSyllableList() {
        viewModelScope.launch {
            dao.save(SyllableList(list = chosenSyllables))
        }
    }
}

enum class Position { FIRST, SECOND }