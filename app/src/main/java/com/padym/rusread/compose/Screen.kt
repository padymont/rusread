package com.padym.rusread.compose

sealed class Screen(val route: String) {
    data object Start : Screen("start")

    data object Game : Screen("game")

    data object ManualList : Screen("manual_list")
}