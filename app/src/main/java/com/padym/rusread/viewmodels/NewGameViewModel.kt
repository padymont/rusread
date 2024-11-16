package com.padym.rusread.viewmodels

import android.app.Application
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.data.AppDatabase
import com.padym.rusread.data.SyllableList
import kotlinx.coroutines.launch

const val MIN_INDEX_VALUE = 0

class NewGameViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application.applicationContext)
    private val dao = db.syllableListDao()

    private val groups = mutableStateOf(listOf(SyllableList.empty()))
    private val currentIndex = mutableIntStateOf(0)
    private val maxIndexValue = derivedStateOf { groups.value.size - 1 }

    val currentGroup by derivedStateOf { groups.value[currentIndex.intValue] }
    val isFirstGroup by derivedStateOf { currentIndex.intValue == MIN_INDEX_VALUE }
    val isLastGroup by derivedStateOf { currentIndex.intValue == maxIndexValue.value }

//    private fun getRandomSyllableSelection() = listOf(
//        setOf("ба", "бо", "во", "вы", "га", "ги", "да", "до", "ду", "дь"),
//        setOf("бе", "би", "бу", "бы", "ва", "ве", "ви", "де", "ди", "дь")
//    ).random()

    fun fetchData() {
        viewModelScope.launch {
            if (dao.getEntryCount() == 0) generateGroup()
            val result = dao.getEntries()!!
            groups.value = result
        }
    }

    fun generateGroup() {
        viewModelScope.launch {
            val syllableList = getRandomSyllableSelection()
            dao.save(SyllableList(list = syllableList))
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