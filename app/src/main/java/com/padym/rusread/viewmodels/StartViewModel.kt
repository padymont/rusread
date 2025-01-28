package com.padym.rusread.viewmodels

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.data.SyllableList
import com.padym.rusread.data.SyllableListDao
import com.padym.rusread.data.SyllableScoreDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MIN_INDEX_VALUE = 0

@HiltViewModel
class StartViewModel @Inject constructor(
    private val listDao: SyllableListDao,
    private val scoreDao: SyllableScoreDao
) : ViewModel() {
    private val groups = mutableStateOf(listOf(SyllableList.empty()))
    private val currentIndex = mutableIntStateOf(0)
    private val maxIndexValue = derivedStateOf { groups.value.size - 1 }
    private val currentGroup by derivedStateOf { groups.value[currentIndex.intValue] }

    val syllablePreviewGroup by derivedStateOf {
        currentGroup.list.map {
            SyllablePreview(it, it in highScoreSyllables)
        }
    }
    val isFirstGroup by derivedStateOf { currentIndex.intValue == MIN_INDEX_VALUE }
    val isLastGroup by derivedStateOf { currentIndex.intValue == maxIndexValue.value }

    private lateinit var highScoreSyllables: List<String>

    init {
        viewModelScope.launch {
            highScoreSyllables = scoreDao.getHighScoreSyllables() ?: emptyList()
        }
    }

    fun fetchData() {
        viewModelScope.launch {
            if (listDao.getEntryCount() == 0) {
                setNewGroup(Syllable.getFirstTimeGroup())
            } else {
                groups.value = listDao.getEntries()!!
            }
        }
    }

    fun generateGroup() = setNewGroup(getRandomGroup())

    fun selectPreviousGroup() {
        currentIndex.intValue = (currentIndex.intValue + 1).coerceAtMost(maxIndexValue.value)
    }

    fun selectNextGroup() {
        currentIndex.intValue = (currentIndex.intValue - 1).coerceAtLeast(MIN_INDEX_VALUE)
    }

    fun fixCurrentGroup() {
        viewModelScope.launch {
            listDao.update(currentGroup)
            currentIndex.intValue = MIN_INDEX_VALUE
        }
    }

    private fun setNewGroup(group: Set<String>) = viewModelScope.launch {
        listDao.save(SyllableList(list = group))
        currentIndex.intValue = MIN_INDEX_VALUE
        groups.value = listDao.getEntries()!!
    }

    private fun getRandomGroup(): Set<String> {
        return Syllable.getPreselectedGroups().random().shuffled().take(10).toSet()
    }
}