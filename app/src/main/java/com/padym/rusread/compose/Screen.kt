package com.padym.rusread.compose

sealed class Screen(val route: String) {
    data object Start : Screen("start")
    data object Game : Screen("game")
    data object AllSyllables : Screen("all_syllables")
    object GameOverDialog : Screen("gameOverDialog")
}