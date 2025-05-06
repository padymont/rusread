package com.padym.rusread.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController

@Composable
fun RusreadApp() {
    val navController = rememberNavController()
    RusreadAppNavHost(navController = navController)
}

@Composable
fun RusreadAppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Start.route
    ) {
        composable(Screen.Start.route) {
            StartScreen(
                onCreateListNavigate = {
                    navController.navigate(Screen.AllSyllables.route)
                },
                onGameStartNavigate = {
                    navController.navigate(Screen.Game.route)
                },
            )
        }
        composable(Screen.Game.route) {
            GameScreen(
                onCloseNavigate = { navController.navigateUp() },
                onFinishGameNavigate = {
                    navController.navigate(Screen.GameOverDialog.route) {
                        popUpTo(Screen.Game.route) {
                            inclusive = true
                        }
                    }
                })
        }
        composable(Screen.AllSyllables.route) {
            AllSyllablesScreen(
                onCloseNavigate = { navController.navigateUp() },
                onSaveListNavigate = { navController.navigateUp() }
            )
        }
        dialog(Screen.GameOverDialog.route) {
            GameOverDialog(
                onCloseNavigate = { navController.navigateUp() }
            )
        }
    }
}