package com.padym.rusread.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.SyllableMediaPlayer
import com.padym.rusread.data.SyllableList
import com.padym.rusread.data.SyllableListDao
import com.padym.rusread.data.SyllableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MIN_SYLLABLES_COUNT = 3
const val MAX_SYLLABLES_COUNT = 10
const val CHOSEN_SYLLABLES_KEY = "chosenSyllables"

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val listDao: SyllableListDao,
    syllableRepository: SyllableRepository,
    private val mediaPlayer: SyllableMediaPlayer,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val chosenSyllables = MutableStateFlow(
        savedStateHandle[CHOSEN_SYLLABLES_KEY] ?: listOf<String>()
    )

    private val _isPreviewOn = mutableStateOf(true)
    val isPreviewOn: Boolean
        get() = _isPreviewOn.value


    val syllablePreviewGroup = combine(
        chosenSyllables,
        syllableRepository.getHighScoreSyllables()
    ) { chosenSyllables, highScoreList ->
        Syllable.getAllSyllablesMap()
            .filter { it.value != 0 }
            .keys
            .sorted()
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

    val isSaveEnabled = chosenSyllables.map {
        it.size >= MIN_SYLLABLES_COUNT
    }.stateIn(initialValue = false)

    private fun processSyllable(syllable: String) {
        if (syllable in chosenSyllables.value) {
            chosenSyllables.value -= syllable
        } else {
            mediaPlayer.speakSyllable(syllable)
            chosenSyllables.value += syllable
        }
        savedStateHandle[CHOSEN_SYLLABLES_KEY] = chosenSyllables.value
    }

    fun finishPreview() {
        _isPreviewOn.value = false
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