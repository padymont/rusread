package com.padym.rusread.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun RusreadApp() {
    val navController = rememberNavController()
    RusreadNavHost(
        navController = navController
    )
}

@Composable
fun RusreadNavHost(navController: NavHostController) {
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
        composable(Screen.ManualList.route) {
            AllSyllablesScreen(navController)
        }
    }
}