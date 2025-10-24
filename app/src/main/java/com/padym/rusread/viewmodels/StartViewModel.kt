package com.padym.rusread.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.SyllableMediaPlayer
import com.padym.rusread.data.Syllable
import com.padym.rusread.data.SyllableList
import com.padym.rusread.data.SyllableListDao
import com.padym.rusread.data.SyllableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MIN_INDEX_VALUE = 0

@HiltViewModel
class StartViewModel @Inject constructor(
    private val listDao: SyllableListDao,
    private val syllableRepository: SyllableRepository,
    private val mediaPlayer: SyllableMediaPlayer,
) : ViewModel() {

    private val currentIndex = MutableStateFlow(0)
    private var maxIndexValue = MIN_INDEX_VALUE

    val currentGroup = listDao.getEntries()
        .map {
            currentIndex.value = MIN_INDEX_VALUE
            it.ifEmpty {
                SyllableList(list = Syllable.firstTimeGroup)
                    .run {
                        listDao.save(this)
                        listOf(this)
                    }
            }
        }.map { list ->
            maxIndexValue = list.lastIndex
            list.sortedByDescending { it.modifiedAt }
        }.combine(syllableRepository.getHighScoreSyllables()) { groups, scores ->
            groups.mapIndexed { index, group ->
                PreviewGroup(
                    id = group.id,
                    isPreviousEnabled = index < groups.lastIndex,
                    isNextEnabled = index > 0,
                    syllables = group.list.sorted().map {
                        SyllablePreview(
                            text = it,
                            isStarred = it in scores,
                            onClick = { mediaPlayer.speakSyllable(it) }
                        )
                    }
                )
            }
        }.combine(currentIndex) { groups, index ->
            groups[index]
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PreviewGroup()
        )

    fun generateGroup() = setNewGroup(
        syllableRepository.getRandomSyllableGroup()
    )

    fun selectPreviousGroup() {
        currentIndex.value = (currentIndex.value + 1).coerceAtMost(maxIndexValue)
    }

    fun selectNextGroup() {
        currentIndex.value = (currentIndex.value - 1).coerceAtLeast(MIN_INDEX_VALUE)
    }

    fun fixCurrentGroup() {
        viewModelScope.launch {
            listDao.update(currentGroup.value.id)
        }
    }

    private fun setNewGroup(group: Set<String>) = viewModelScope.launch {
        listDao.save(SyllableList(list = group))
    }
}