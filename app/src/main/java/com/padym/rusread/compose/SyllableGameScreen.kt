package com.padym.rusread.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.padym.rusread.viewmodels.RIGHT_ANSWER_NUMBER
import com.padym.rusread.viewmodels.SyllableGameViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SyllableGameScreen(navController: NavHostController, chosenSyllables: Set<String>) {
    val viewModel: SyllableGameViewModel = viewModel()
    viewModel.initializeData(chosenSyllables)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") }, // Empty title
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                })
        }
    ) { paddingValues ->
        val currentSyllable = viewModel.newSyllable
        if (viewModel.correctAnswers < RIGHT_ANSWER_NUMBER) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = currentSyllable,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .clickable {
                            viewModel.speakText(currentSyllable)
                        }
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    viewModel.selectedSyllables.forEach { syllable ->
                        Button(onClick = {
                            if (syllable == viewModel.newSyllable) {
                                viewModel.increaseCorrectAnswers()
                            }
                            viewModel.setNewSyllable()
                        }) {
                            Text(syllable)
                        }
                    }
                }
            }
        } else {
            // Game finished
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Great job!", fontSize = 32.sp)
            }
        }
    }
}