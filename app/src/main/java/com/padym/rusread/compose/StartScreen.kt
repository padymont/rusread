package com.padym.rusread.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    Scaffold { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            SelectionActionRow(
                isPreviousSelectionEnabled = currentGroup.isPreviousEnabled,
                isNextSelectionEnabled = currentGroup.isNextEnabled,
                previousSelectionAction = { viewModel.selectPreviousGroup() },
                nextSelectionAction = { viewModel.selectNextGroup() },
                createSelectionAction = { navController.navigate(Screen.ManualList.route) },
                randomSelectionAction = { viewModel.generateGroup() }
            )
            SelectedPreview(currentGroup.syllables)
            Spacer(modifier = Modifier.weight(1f))
            BottomEmojiRoundButton(text = "🚀") {
                viewModel.fixCurrentGroup()
                navController.navigate(Screen.Game.route)
            }
        }
    }
}

@Composable
fun SelectedPreview(syllables: List<SyllablePreview>) = SelectionSyllablesRow {
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
        Scaffold { paddingValues ->
            Column(Modifier.padding(paddingValues)) {
                SelectionActionRow(true, true, {}, {}, {}, {})
                SelectedPreview(scoredSyllables)
                Spacer(modifier = Modifier.weight(1f))
                BottomEmojiRoundButton(text = "🚀", onButtonClick = {})
            }
        }
    }
}