package com.padym.rusread.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController

@Composable
fun RusreadApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Start.route
    ) {
        composable(Screen.Start.route) {
            StartScreen(navController)
        }
        composable(Screen.Game.route) {
            GameScreen(navController)
        }
        composable(Screen.AllSyllables.route) {
            AllSyllablesScreen(navController)
        }
        dialog(Screen.GameOverDialog.route) {
            GameOverDialog(navController)
        }
    }
}