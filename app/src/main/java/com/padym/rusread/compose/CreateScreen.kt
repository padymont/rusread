package com.padym.rusread.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.padym.rusread.ui.theme.AppColors
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.CreateViewModel
import com.padym.rusread.viewmodels.Syllable
import com.padym.rusread.viewmodels.SyllablePreview
import kotlin.random.Random

@Composable
fun CreateScreen(
    onCloseNavigate: () -> Unit,
    onSaveListNavigate: () -> Unit,
) {
    val viewModel: CreateViewModel = hiltViewModel()
    val syllables by viewModel.syllablePreviewGroup.collectAsState()
    val isSavingEnabled by viewModel.isSavingEnabled.collectAsState()

    CreateLayout(
        onCloseScreen = onCloseNavigate,
        syllables = syllables,
        isSavingEnabled = isSavingEnabled,
        onSaveList = {
            viewModel.saveSyllableList()
            onSaveListNavigate.invoke()
        }
    )
}

@Composable
fun CreateLayout(
    onCloseScreen: () -> Unit = {},
    syllables: List<SyllablePreview> = emptyList(),
    isSavingEnabled: Boolean = true,
    onSaveList: () -> Unit = {},
) {

    Scaffold(
        topBar = {
            Column {
                SimpleCloseTopAppBar { onCloseScreen() }
                HorizontalDivider(thickness = 2.dp, color = AppColors.SoftSand)
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider(thickness = 2.dp, color = AppColors.SoftSand)
                Spacer(modifier = Modifier.height(16.dp))
                val text = if (isSavingEnabled) "👍" else ""
                BottomEmojiRoundButton(text = text, isEnabled = isSavingEnabled) { onSaveList() }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            SelectionSyllablesRow {
                SyllableSelection(syllables)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateLayoutPreview() {
    RusreadTheme {
        CreateLayout(syllables = CreatePreviewHelper.scoredSyllables)
    }
}

private object CreatePreviewHelper {
    val chosenSyllables = Syllable.getAll().map { it.key }.shuffled().take(30).sorted()
    val scoredSyllables = chosenSyllables.map {
        val isSelected = Random.nextBoolean()
        val isEnabled = if (isSelected) {
            true
        } else {
            Random.nextBoolean()
        }
        SyllablePreview(
            text = it,
            isSelected = isSelected,
            isEnabled = isEnabled,
            isStarred = Random.nextBoolean()
        )
    }
}