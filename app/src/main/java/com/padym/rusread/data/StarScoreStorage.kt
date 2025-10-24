package com.padym.rusread.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val STAR_SCORE_PREFS = "star_score_prefs"
private const val STAR_SCORE_KEY = "star_score_key"

@Singleton
class StarScoreStorage @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STAR_SCORE_PREFS, Context.MODE_PRIVATE)

    fun getScore(): Flow<Int> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == STAR_SCORE_KEY) {
                trySend(prefs.getInt(STAR_SCORE_KEY, INITIAL_STAR_SCORE))
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)

        trySend(prefs.getInt(STAR_SCORE_KEY, INITIAL_STAR_SCORE))

        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun setScore(score: Int) {
        prefs.edit { putInt(STAR_SCORE_KEY, score) }
    }
}