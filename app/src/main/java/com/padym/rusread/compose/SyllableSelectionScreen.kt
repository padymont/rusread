package com.padym.rusread.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.padym.rusread.ui.theme.AppColors
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.MAX_CHOSEN_SYLLABLES
import com.padym.rusread.viewmodels.SyllableListViewModel
import com.padym.rusread.viewmodels.SyllableListViewModel.SyllableGroup

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
                    color = AppColors.Almond,
                    cornerRadius = cornerRadius
                )
                drawRoundRect(
                    color = AppColors.IndianRed,
                    size = Size(width, size.height),
                    cornerRadius = cornerRadius
                )
            }) {
        Text(
            text = "Начать",
            fontSize = 36.sp,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SyllableGroupItem(
    syllables: List<Pair<String, Boolean>>,
    isSelectionEnabled: Boolean,
    onSyllableSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 8.dp)
            .fillMaxWidth()
    ) {
        FlowRow(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        ) {
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
        enabled = isEnabled,
        onClick = { onToggle(!isSelected) },
        contentPadding = PaddingValues(8.dp),
        border = BorderStroke(width = 0.dp, color = Color.Transparent),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) AppColors.Almond else Color.Transparent
        ),
        modifier = Modifier
            .padding(end = 8.dp)
            .widthIn(min = 48.dp)
    ) {
        Text(
            text = syllable,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SyllableSelectionScreenPreview() {
    val groupedSyllables = listOf(
        SyllableGroup(listOf("ба", "бо", "бу", "бя", "бы", "би", "бе")),
        SyllableGroup(listOf("т", "е", "в", "я", "й", "ж", "з", "ч", "ф")),
        SyllableGroup(listOf("ба", "бо", "бу", "бы", "би", "бе")),
        SyllableGroup(listOf("ба", "же", "жо", "жа", "бе")),
        SyllableGroup(listOf("ба", "бо", "бу", "бя", "бы", "би", "бе")),
        SyllableGroup(listOf("ко", "на", "са", "ку", "ла", "ле", "ли", "ло", "лу")),
        SyllableGroup(listOf("л", "жу")),
        SyllableGroup(listOf("ба", "бо", "бу", "бя", "бы", "би", "бе")),
    )
    val selectedSyllables = groupedSyllables.flatMap { it.syllables }
        .shuffled()
        .take(10)
        .toSet()

    RusreadTheme {
        Scaffold(
            topBar = { ClearSelectionTopAppBar(true) {} },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                ProgressBarButton(
                    progress = MAX_CHOSEN_SYLLABLES - 2,
                    maxProgress = MAX_CHOSEN_SYLLABLES,
                    isEnabled = true
                ) {}
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(groupedSyllables) { group ->
                    SyllableGroupItem(
                        group.syllables.map { syllable ->
                            Pair(syllable, syllable in selectedSyllables)
                        }, true
                    ) { }
                }
            }
        }
    }
}