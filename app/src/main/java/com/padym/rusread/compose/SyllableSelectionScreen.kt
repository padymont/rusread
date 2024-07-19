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
                SyllableGroupItem(
                    group.syllables.map { syllable ->
                        Pair(syllable, syllable in selectedSyllables)
                    },
                    viewModel.isSelectionEnabled
                ) { syllable -> viewModel.changeSyllableSelection(syllable) }
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
fun SyllableGroupItem(
    syllables: List<Pair<String, Boolean>>,
    isSelectionEnabled: Boolean,
    onSyllableSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        FlowRow(modifier = Modifier.padding(8.dp)) {
            syllables.forEach { (syllable, isSelected) ->
                SyllableItem(
                    syllable = syllable,
                    isSelected = isSelected,
                    isEnabled = isSelectionEnabled || isSelected,
                    onToggle = { onSyllableSelected(syllable) }
                )
            }
        }
    }
}

@Composable
fun SyllableItem(
    syllable: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    OutlinedButton(
        onClick = { onToggle(!isSelected) },
        enabled = isEnabled,
        modifier = Modifier.padding(end = 6.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
        )
    ) {
        Text(text = syllable)
    }
}