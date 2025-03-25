package com.padym.rusread.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.data.SyllableList
import com.padym.rusread.data.SyllableListDao
import com.padym.rusread.data.SyllableScoreDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PRELOAD_COUNT = 60

@HiltViewModel
class AllSyllablesViewModel @Inject constructor(
    private val listDao: SyllableListDao,
    scoreDao: SyllableScoreDao
) : ViewModel() {

    private val chosenSyllables = MutableStateFlow(listOf<String>())
    private val isPreload = MutableStateFlow(true)

    val syllablePreviewGroup = combine(
        isPreload,
        chosenSyllables,
        scoreDao.getHighScoreSyllables()
    ) { isPreload, chosenSyllables, highScoreList ->
        Syllable.getAll()
            .filter { it.millisOffset != Int.MAX_VALUE }
            .sortedBy { it.key }
            .take(if (isPreload) PRELOAD_COUNT else Int.MAX_VALUE)
            .map { it.key }
            .map { syllable ->
                SyllablePreview(
                    text = syllable,
                    isSelected = syllable in chosenSyllables,
                    isEnabled = chosenSyllables.size < MAX_SYLLABLES_COUNT,
                    isStarred = syllable in highScoreList,
                    onClick = { processSyllable(syllable) }
                )
            }
    }.stateIn(initialValue = emptyList())

    val isSavingEnabled = chosenSyllables.map {
        it.size >= MIN_SYLLABLES_COUNT
    }.stateIn(initialValue = false)

    init {
        viewModelScope.launch {
            delay(500)
            isPreload.value = false
        }
    }

    private fun processSyllable(syllable: String) {
        if (syllable in chosenSyllables.value) {
            chosenSyllables.value -= syllable
        } else {
            chosenSyllables.value += syllable
        }
    }

    fun saveSyllableList() {
        viewModelScope.launch {
            listDao.save(SyllableList(list = chosenSyllables.value.toSet()))
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialValue
    )
}