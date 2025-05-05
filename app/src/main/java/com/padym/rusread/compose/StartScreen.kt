package com.padym.rusread.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.StartViewModel
import com.padym.rusread.viewmodels.SyllablePreview
import kotlin.random.Random

@Composable
fun StartScreen(navController: NavHostController) {
    val viewModel: StartViewModel = hiltViewModel()
    val currentGroup by viewModel.currentGroup.collectAsState()

    StartScreen2(
        isPreviousSelectionEnabled = currentGroup.isPreviousEnabled,
        isNextSelectionEnabled = currentGroup.isNextEnabled,
        previousSelectionAction = { viewModel.selectPreviousGroup() },
        nextSelectionAction = { viewModel.selectNextGroup() },
        createSelectionAction = { navController.navigate(Screen.AllSyllables.route) },
        randomSelectionAction = { viewModel.generateGroup() },
        syllables = currentGroup.syllables,
        startGameAction = {
            viewModel.fixCurrentGroup()
            navController.navigate(Screen.Game.route)
        }
    )
}

@Composable
fun StartScreen2(
    isPreviousSelectionEnabled: Boolean,
    isNextSelectionEnabled: Boolean,
    previousSelectionAction: () -> Unit,
    nextSelectionAction: () -> Unit,
    createSelectionAction: () -> Unit,
    randomSelectionAction: () -> Unit,
    syllables: List<SyllablePreview>,
    startGameAction: () -> Unit,
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column {
                SelectionActionRow(
                    isPreviousSelectionEnabled = isPreviousSelectionEnabled,
                    isNextSelectionEnabled = isNextSelectionEnabled,
                    previousSelectionAction = previousSelectionAction,
                    nextSelectionAction = nextSelectionAction,
                    createSelectionAction = createSelectionAction,
                    randomSelectionAction = randomSelectionAction
                )
                SelectedPreview(syllables)
                Spacer(modifier = Modifier.weight(1f))
                BottomEmojiRoundButton(text = "🚀", onButtonClick = startGameAction)
            }
        }
    }
}

@Composable
fun SelectedPreview(syllables: List<SyllablePreview>) =
    SelectionSyllablesRow(paddingBottom = 48.dp) {
        SyllableSelection(syllables)
    }

@Composable
fun SelectionActionRow(
    isPreviousSelectionEnabled: Boolean,
    isNextSelectionEnabled: Boolean,
    previousSelectionAction: () -> Unit,
    nextSelectionAction: () -> Unit,
    createSelectionAction: () -> Unit,
    randomSelectionAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 200.dp, start = 24.dp, end = 24.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        EmojiIconButton(
            text = "👈",
            isVisible = isPreviousSelectionEnabled,
            onButtonClick = previousSelectionAction
        )
        EmojiIconButton(
            text = "👉",
            isVisible = isNextSelectionEnabled,
            onButtonClick = nextSelectionAction
        )
        EmojiIconButton(text = "🖍", onButtonClick = createSelectionAction)
        EmojiIconButton(text = "🎲", onButtonClick = randomSelectionAction)
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    val chosenSyllables = listOf("ба", "бо", "бу", "бя", "ша", "фу", "цу", "бы", "би", "бе")
    val scoredSyllables = chosenSyllables.map {
        SyllablePreview(it, Random.nextBoolean())
    }

    RusreadTheme {
        StartScreen2(
            isPreviousSelectionEnabled = true,
            isNextSelectionEnabled = true,
            previousSelectionAction = {},
            nextSelectionAction = {},
            createSelectionAction = {},
            randomSelectionAction = {},
            syllables = scoredSyllables,
            startGameAction = {}
        )
    }
}