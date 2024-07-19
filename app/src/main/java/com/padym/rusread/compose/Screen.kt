package com.padym.rusread.compose

import androidx.navigation.NavBackStackEntry

const val CHOSEN_SYLLABLES_KEY = "chosenSyllables"
const val STRING_DELIMITER = ", "

sealed class Screen(val route: String) {
    data object SyllableSelection : Screen("syllable_selection")

    data object SyllableGame : Screen("syllable_game/{$CHOSEN_SYLLABLES_KEY}") {

        fun passChosenSyllables(selectedSyllables: Set<String>): String {
            val string = selectedSyllables.joinToString(separator = STRING_DELIMITER)
            return this.route.replace(oldValue = "{$CHOSEN_SYLLABLES_KEY}", newValue = string)
        }

        fun parseChosenSyllables(backStackEntry: NavBackStackEntry): Set<String> {
            return backStackEntry.arguments
                ?.getString(CHOSEN_SYLLABLES_KEY)
                ?.split(STRING_DELIMITER)?.toSet()
                ?: emptySet()
        }
    }
}