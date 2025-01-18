package com.padym.rusread.viewmodels

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.data.SyllableList
import com.padym.rusread.data.SyllableListDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MIN_INDEX_VALUE = 0

@HiltViewModel
class StartViewModel @Inject constructor(
    private val dao: SyllableListDao
) : ViewModel() {
    private val groups = mutableStateOf(listOf(SyllableList.empty()))
    private val currentIndex = mutableIntStateOf(0)
    private val maxIndexValue = derivedStateOf { groups.value.size - 1 }

    val currentGroup by derivedStateOf { groups.value[currentIndex.intValue] }
    val isFirstGroup by derivedStateOf { currentIndex.intValue == MIN_INDEX_VALUE }
    val isLastGroup by derivedStateOf { currentIndex.intValue == maxIndexValue.value }

    fun fetchData() {
        viewModelScope.launch {
            if (dao.getEntryCount() == 0) {
                generateGroup()
            } else {
                groups.value = dao.getEntries()!!
            }
        }
    }

    fun generateGroup() {
        viewModelScope.launch {
            val syllableList = getRandomSyllableSelection()
            dao.save(SyllableList(list = syllableList))
            currentIndex.intValue = MIN_INDEX_VALUE
            groups.value = dao.getEntries()!!
        }
    }

    fun selectPreviousGroup() {
        currentIndex.intValue = (currentIndex.intValue + 1).coerceAtMost(maxIndexValue.value)
    }

    fun selectNextGroup() {
        currentIndex.intValue = (currentIndex.intValue - 1).coerceAtLeast(MIN_INDEX_VALUE)
    }

    fun fixCurrentGroup() {
        viewModelScope.launch {
            dao.update(currentGroup)
            currentIndex.intValue = MIN_INDEX_VALUE
        }
    }

    private fun getRandomSyllableSelection() = Syllable
        .getAll()
        .map { it.key }
        .filter { it.length > 1 }
        .shuffled()
        .slice(0..9)
        .sorted()
        .toSet()
}