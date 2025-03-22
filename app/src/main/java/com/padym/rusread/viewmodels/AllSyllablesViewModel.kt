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

@HiltViewModel
class AllSyllablesViewModel @Inject constructor(
    private val listDao: SyllableListDao,
    private val scoreDao: SyllableScoreDao
) : ViewModel() {


    private val chosenSyllables = MutableStateFlow(listOf<String>())

    val syllablePreviewGroup = combine(
        chosenSyllables,
        scoreDao.getHighScoreSyllables()
    ) { chosenSyllables, highScoreList ->
        Syllable.getAll()
            .filter { it.millisOffset != Int.MAX_VALUE }
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
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val isSavingEnabled = chosenSyllables.map {
        it.size >= MIN_SYLLABLES_COUNT
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

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
}