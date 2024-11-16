package com.padym.rusread.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.padym.rusread.data.AppDatabase
import com.padym.rusread.data.SyllableList
import kotlinx.coroutines.launch

class NewGameViewModel(application: Application) : AndroidViewModel(application)  {

    private val _selectedSyllables = mutableStateOf(setOf<String>())
    val selectedSyllables: Set<String>
        get() = _selectedSyllables.value

    private val db = AppDatabase.getDatabase(application.applicationContext)
    private val dao = db.syllableListDao()

//    private fun getRandomSyllableSelection() = listOf(
//        setOf("ба", "бо", "во", "вы", "га", "ги", "да", "до", "ду", "дь"),
//        setOf("бе", "би", "бу", "бы", "ва", "ве", "ви", "де", "ди", "дь")
//    ).random()

    fun fetchData() {
        viewModelScope.launch {
            if (dao.getEntryCount() == 0) {
                val syllableList = setOf("бе", "би", "бу", "бы", "ва", "ве", "ви", "дe", "ди", "дь")
                dao.save(SyllableList(list = syllableList))
            }
            val result = dao.getEntries()?.first()?.list!!
            _selectedSyllables.value = result
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