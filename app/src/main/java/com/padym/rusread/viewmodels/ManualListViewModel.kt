package com.padym.rusread.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.data.SyllableList
import com.padym.rusread.data.SyllableListDao
import com.padym.rusread.data.SyllableScoreDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MIN_SYLLABLES_COUNT = 3
const val MAX_SYLLABLES_COUNT = 10
const val EMPTY_SIGN = "🎈"

@HiltViewModel
class ManualListViewModel @Inject constructor(
    private val listDao: SyllableListDao,
    private val scoreDao: SyllableScoreDao
) : ViewModel() {

    val firstLetterList = listOf(
        "б", "в", "г", "д", "ж", "з", "й", "к", "л", "м", "н", "п", "р",
        "с", "т", "ф", "х", "ц", "ч", "ш", "щ", EMPTY_SIGN
    )
    val secondLetterOptions = listOf(
        "а", "е", "ё", "и", "о", "у", "ы", "ь", "ъ", "э", "ю", "я", EMPTY_SIGN
    )

    private val chosenSyllables = MutableStateFlow(listOf<String>())

    val syllablePreviewGroup = combine(
        chosenSyllables,
        scoreDao.getHighScoreSyllables()
    ) { list, highScoreList ->
        list.map {
            SyllablePreview(it, it in highScoreList)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val isSavingListAvailable = chosenSyllables.map {
        it.size >= MIN_SYLLABLES_COUNT
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val isChoosingAvailable = chosenSyllables.map {
        it.size < MAX_SYLLABLES_COUNT
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    private var firstLetter = ""
    private var secondLetter = ""

    fun processChosenLetter(position: Position, letter: String) {
        when (position) {
            Position.FIRST -> firstLetter = letter
            Position.SECOND -> secondLetter = letter
        }
    }

    fun saveSyllable() {
        val unfilteredString = "$firstLetter$secondLetter"
        val syllable = unfilteredString.replace(EMPTY_SIGN, "")
        if (syllable.isNotEmpty() && syllable !in chosenSyllables.value) {
            chosenSyllables.value += syllable
        }
    }

    fun saveSyllableList() {
        viewModelScope.launch {
            listDao.save(SyllableList(list = chosenSyllables.value.toSet()))
        }
    }

    fun deleteSyllable() {
        chosenSyllables.value -= chosenSyllables.value.last()
    }
}

enum class Position { FIRST, SECOND }