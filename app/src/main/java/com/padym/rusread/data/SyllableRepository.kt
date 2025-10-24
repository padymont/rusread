package com.padym.rusread.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyllableRepository @Inject constructor(
    private val syllableScoreDao: SyllableScoreDao,
    private val starScoreDao: StarScoreDao
) {

    fun getAllValidSyllables() = Syllable.allSyllablesMap
        .filter { it.value != 0 }
        .keys

    fun getSyllableResourceId(syllable: String) = Syllable.allSyllablesMap[syllable] ?: 0

    fun getRandomSyllableGroup(): Set<String> {
        return Syllable.getPreselectedGroups().random().shuffled().take(10).toSet()
    }

    fun getCurrentScore() = starScoreDao.getScore().map { starScore ->
        starScore?.score ?: INITIAL_STAR_SCORE
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getHighScoreSyllables() = getCurrentScore()
        .flatMapLatest { highScore -> syllableScoreDao.getHighScoreEntries(highScore) }
        .map { list ->
            list.map { it.syllable }
        }

    suspend fun setNewScore(newScore: Int) = starScoreDao.setScore(StarScore(score = newScore))

    suspend fun save(syllable: String) = syllableScoreDao.insert(SyllableScore(syllable))

    suspend fun update(syllable: String, score: Int) {
        syllableScoreDao.updateScore(syllable, score)
    }

    suspend fun lowerScore(syllable: String) {
        val entry = syllableScoreDao.getEntry(syllable)
        if (entry.score > 0) {
            update(entry.syllable, entry.score - 1)
        }
    }

    suspend fun increaseScore(syllable: String) {
        val entry = syllableScoreDao.getEntry(syllable)
        update(entry.syllable, entry.score + 1)
    }

    suspend fun getScores(syllables: Set<String>): List<Pair<String, Int>> {
        return syllableScoreDao.getEntriesScores(syllables).map {
            Pair(it.syllable, it.score)
        }
    }

    suspend fun clearAllEntries() = syllableScoreDao.clearAllEntries()
}