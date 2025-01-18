package com.padym.rusread.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.StartViewModel

@Composable
fun StartScreen(navController: NavHostController) {
    val viewModel: StartViewModel = hiltViewModel()
    viewModel.fetchData()

    Scaffold { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            SelectionActionRow(
                isPreviousSelectionEnabled = !viewModel.isLastGroup,
                isNextSelectionEnabled = !viewModel.isFirstGroup,
                previousSelectionAction = { viewModel.selectPreviousGroup() },
                nextSelectionAction = { viewModel.selectNextGroup() },
                createSelectionAction = { navController.navigate(Screen.ManualList.route) },
                randomSelectionAction = { viewModel.generateGroup() }
            )
            SelectionSyllablesRow(viewModel.currentGroup.list) {}
            EmojiRoundButton(text = "🚀") {
                viewModel.fixCurrentGroup()
                navController.navigate(Screen.Game.route)
            }
        }
    }
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
            .padding(top = 160.dp, start = 24.dp, end = 24.dp, bottom = 16.dp),
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
    val chosenSyllables = listOf("ба", "бо", "бу", "бя", "ша", "фу", "цу", "бы", "би", "бе").toSet()

    RusreadTheme {
        Scaffold { paddingValues ->
            Column(Modifier.padding(paddingValues)) {
                SelectionActionRow(true, true, {}, {}, {}, {})
                SelectionSyllablesRow(chosenSyllables) {}
                EmojiRoundButton(text = "🚀", onButtonClick = {})
            }
        }
    }
}