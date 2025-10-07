package com.padym.rusread.compose

sealed class Screen(val route: String) {
    data object Start : Screen("start")
    data object Game : Screen("game")
    data object Create : Screen("create")
    data object CongratulationsScreen : Screen("congratulations")

    data object Settings : Screen("settings")
}