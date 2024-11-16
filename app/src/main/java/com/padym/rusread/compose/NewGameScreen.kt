package com.padym.rusread.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.NewGameViewModel

@Composable
fun NewGameScreen(navController: NavHostController) {
    val viewModel: NewGameViewModel = viewModel()
    viewModel.fetchData()
    val selectedSyllables = viewModel.selectedSyllables

    Scaffold { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            SelectionActionRow({}, {}, {}, {})
            SelectionSyllablesRow(selectedSyllables) {}
            EmojiRoundButton(text = "🚀") {
                navController.navigate(Screen.SyllableGame.passChosenSyllables(selectedSyllables))
            }
        }
    }
}

@Composable
fun EmojiIconButton(text: String, onButtonClick: () -> Unit) {
    OutlinedButton(
        onClick = onButtonClick,
        contentPadding = PaddingValues(0.dp),
        border = BorderStroke(width = 0.dp, color = Color.Transparent),
        modifier = Modifier
            .clip(CircleShape)
            .size(64.dp)
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SingleSyllableClickable(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = PaddingValues(12.dp),
        border = BorderStroke(width = 0.dp, color = Color.Transparent),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
fun SelectionActionRow(
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
        EmojiIconButton(text = "👈", onButtonClick = previousSelectionAction)
        EmojiIconButton(text = "👉", onButtonClick = nextSelectionAction)
        EmojiIconButton(text = "🖍", onButtonClick = createSelectionAction)
        EmojiIconButton(text = "🎲", onButtonClick = randomSelectionAction)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectionSyllablesRow(selection: Set<String>, onClick: () -> Unit) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .heightIn(min = 268.dp)
            .padding(bottom = 48.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        selection.forEach { syllable ->
            SingleSyllableClickable(text = syllable, onClick = onClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewGameScreenPreview() {
    val chosenSyllables = listOf("ба", "бо", "бу", "бя", "ша", "фу", "цу", "бы", "би", "бе").toSet()

    RusreadTheme {
        Scaffold { paddingValues ->
            Column(Modifier.padding(paddingValues)) {
                SelectionActionRow({}, {}, {}, {})
                SelectionSyllablesRow(chosenSyllables) {}
                EmojiRoundButton(text = "🚀", onButtonClick = {})
            }
        }
    }
}