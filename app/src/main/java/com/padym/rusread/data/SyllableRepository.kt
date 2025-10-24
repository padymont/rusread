package com.padym.rusread.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val INITIAL_STAR_SCORE = 10

@Singleton
class SyllableRepository @Inject constructor(
    private val syllableGroupDao: SyllableGroupDao,
    private val syllableScoreDao: SyllableScoreDao,
    private val starScoreStorage: StarScoreStorage
) {

    fun getAllValidSyllables() = Syllable.allSyllablesMap
        .filter { it.value != 0 }
        .keys

    fun getSyllableResourceId(syllable: String) = Syllable.allSyllablesMap[syllable] ?: 0

    fun getRandomSyllableGroup(): Set<String> {
        return Syllable.getPreselectedGroups().random().shuffled().take(10).toSet()
    }

    fun getSavedSyllableGroups() = syllableGroupDao.getEntries()

    suspend fun getLatestSavedSyllableGroup() = syllableGroupDao.getLatestEntry()

    suspend fun saveSyllableGroup(group: SyllableGroup) = syllableGroupDao.save(group)

    suspend fun updateSavedSyllableGroup(entityId: Int) {
        syllableGroupDao.updateModifiedAt(entityId, System.currentTimeMillis())
    }

    fun getCurrentStarScore() = starScoreStorage.getScore()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getStarScoreSyllables() = getCurrentStarScore()
        .flatMapLatest { highScore -> syllableScoreDao.getHighScoreEntries(highScore) }
        .map { list ->
            list.map { it.syllable }
        }

    fun setNewStarScore(newScore: Int) = starScoreStorage.setScore(newScore)

    suspend fun saveSyllableScore(syllable: String) {
        syllableScoreDao.insert(SyllableScore(syllable))
    }

    suspend fun updateSyllableScore(syllable: String, score: Int) {
        syllableScoreDao.updateScore(syllable, score)
    }

    suspend fun lowerSyllableScore(syllable: String) {
        val entry = syllableScoreDao.getEntry(syllable)
        if (entry.score > 0) {
            updateSyllableScore(entry.syllable, entry.score - 1)
        }
    }

    suspend fun increaseSyllableScore(syllable: String) {
        val entry = syllableScoreDao.getEntry(syllable)
        updateSyllableScore(entry.syllable, entry.score + 1)
    }

    suspend fun clearAllSyllablesScores() = syllableScoreDao.clearAllEntries()
}