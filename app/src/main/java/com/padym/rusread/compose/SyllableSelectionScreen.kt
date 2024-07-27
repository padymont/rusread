package com.padym.rusread.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.padym.rusread.viewmodels.MAX_CHOSEN_SYLLABLES
import com.padym.rusread.viewmodels.SyllableListViewModel

@Composable
fun SyllableSelectionScreen(navController: NavHostController) {
    val viewModel: SyllableListViewModel = viewModel()
    val selectedSyllables = viewModel.selectedSyllables

    Scaffold(
        topBar = {
            ClearSelectionTopAppBar(viewModel.isClearSelectionEnabled) {
                viewModel.clearChosenSyllables()
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ProgressBarButton(
                progress = viewModel.selectedSyllablesCount,
                maxProgress = MAX_CHOSEN_SYLLABLES,
                isEnabled = viewModel.isEnoughSyllablesSelected
            ) {
                navController.navigate(Screen.SyllableGame.passChosenSyllables(selectedSyllables))
                viewModel.clearChosenSyllables()
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(viewModel.getGroupedSyllables()) { group ->
                SyllableGroupItem(
                    group.syllables.map { syllable ->
                        Pair(syllable, syllable in selectedSyllables)
                    }, viewModel.isSelectionEnabled
                ) { syllable -> viewModel.changeSyllableSelection(syllable) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClearSelectionTopAppBar(
    isVisible: Boolean,
    onAction: () -> Unit = {}
) {
    TopAppBar(
        title = { Text("") },
        modifier = Modifier.padding(8.dp),
        actions = {
            if (isVisible) {
                IconButton(onClick = onAction) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Clear",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        },
    )
}

@Composable
fun ProgressBarButton(progress: Int, maxProgress: Int, isEnabled: Boolean, onClick: () -> Unit) {
    val radius = 4.dp
    Button(onClick = onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(radius),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(16.dp)
            .drawBehind {
                val progressFraction = progress.toFloat() / maxProgress.toFloat()
                val width = size.width * progressFraction
                val cornerRadius = CornerRadius(radius.toPx())
                drawRoundRect(
                    color = Color.White,
                    cornerRadius = cornerRadius
                )
                drawRoundRect(
                    color = Color.Yellow,
                    size = Size(width, size.height),
                    cornerRadius = cornerRadius
                )
            }) {
        Text("Начать")
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
                SyllableItem(syllable = syllable,
                    isSelected = isSelected,
                    isEnabled = isSelectionEnabled || isSelected,
                    onToggle = { onSyllableSelected(syllable) })
            }
        }
    }
}

@Composable
fun SyllableItem(
    syllable: String, isSelected: Boolean, isEnabled: Boolean, onToggle: (Boolean) -> Unit
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