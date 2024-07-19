package com.padym.rusread.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.padym.rusread.viewmodels.MIN_CHOSEN_SYLLABLES
import com.padym.rusread.viewmodels.SyllableListViewModel

@Composable
fun SyllableSelectionScreen(navController: NavHostController) {
    val viewModel: SyllableListViewModel = viewModel()
    val selectedSyllables = viewModel.selectedSyllables

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Choose at least 3 syllables",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(viewModel.getGroupedSyllables()) { group ->
                SyllableGroupItem(group.syllables) { syllable ->
                    if (syllable.isSelected) {
                        viewModel.addSyllable(syllable.text)
                    } else {
                        viewModel.removeSyllable(syllable.text)
                    }
                }
            }
        }

        if (selectedSyllables.size >= MIN_CHOSEN_SYLLABLES) {
            Button(
                onClick = {
                    navController.navigate(
                        Screen.SyllableGame.passChosenSyllables(selectedSyllables)
                    )
                    viewModel.clearChosenSyllables()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Next")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SyllableGroupItem(syllables: List<String>, onSyllableSelected: (SyllableBlock) -> Unit) {
    var selectedSyllables by remember { mutableStateOf(emptySet<String>()) }
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        FlowRow(modifier = Modifier.padding(8.dp)) {
            syllables.forEach { syllable ->
                SyllableItem(
                    syllable = syllable,
                    isSelected = syllable in selectedSyllables,
                    onToggle = { isSelected ->
                        if (isSelected) {
                            selectedSyllables = selectedSyllables + syllable
                        } else {
                            selectedSyllables = selectedSyllables - syllable
                        }
                        onSyllableSelected(SyllableBlock(syllable, isSelected))
                    }
                )
            }
        }
    }
}

data class SyllableBlock(val text: String, val isSelected: Boolean)

@Composable
fun SyllableItem(syllable: String, isSelected: Boolean, onToggle: (Boolean) -> Unit) {
    OutlinedButton(
        onClick = { onToggle(!isSelected) },
        modifier = Modifier.padding(end = 6.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
        )
    ) {
        Text(text = syllable)
    }
}