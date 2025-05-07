package com.padym.rusread.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.StartViewModel
import com.padym.rusread.viewmodels.SyllablePreview
import kotlin.random.Random

@Composable
fun StartScreen(
    onCreateListNavigate: () -> Unit,
    onGameStartNavigate: () -> Unit,
) {
    val viewModel: StartViewModel = hiltViewModel()
    val currentGroup by viewModel.currentGroup.collectAsState()

    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            StartPortraitLayout(
                isPreviousSelectionEnabled = currentGroup.isPreviousEnabled,
                isNextSelectionEnabled = currentGroup.isNextEnabled,
                previousSelectionAction = { viewModel.selectPreviousGroup() },
                nextSelectionAction = { viewModel.selectNextGroup() },
                createSelectionAction = onCreateListNavigate,
                randomSelectionAction = { viewModel.generateGroup() },
                syllables = currentGroup.syllables,
                startGameAction = {
                    viewModel.fixCurrentGroup()
                    onGameStartNavigate.invoke()
                }
            )
        }

        else -> {
            StartLandscapeLayout(
                isPreviousSelectionEnabled = currentGroup.isPreviousEnabled,
                isNextSelectionEnabled = currentGroup.isNextEnabled,
                previousSelectionAction = { viewModel.selectPreviousGroup() },
                nextSelectionAction = { viewModel.selectNextGroup() },
                createSelectionAction = onCreateListNavigate,
                randomSelectionAction = { viewModel.generateGroup() },
                syllables = currentGroup.syllables,
                startGameAction = {
                    viewModel.fixCurrentGroup()
                    onGameStartNavigate.invoke()
                }
            )
        }
    }
}

@Composable
fun StartPortraitLayout(
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
        RootPortraitBox(paddingValues) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    SelectionAction(
                        isPreviousSelectionEnabled = isPreviousSelectionEnabled,
                        isNextSelectionEnabled = isNextSelectionEnabled,
                        previousSelectionAction = previousSelectionAction,
                        nextSelectionAction = nextSelectionAction,
                        createSelectionAction = createSelectionAction,
                        randomSelectionAction = randomSelectionAction
                    )
                }
                SelectionSyllablesRow(syllables)
                Spacer(modifier = Modifier.height(48.dp))
                BottomEmojiRoundButton(text = "🚀", onButtonClick = startGameAction)
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun StartLandscapeLayout(
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
        RootLandscapeBox(paddingValues) {
            Row(
                modifier = Modifier
                    .padding(vertical = 48.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Box(
                    modifier = Modifier.weight(0.5f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SelectionAction(
                            isPreviousSelectionEnabled = isPreviousSelectionEnabled,
                            isNextSelectionEnabled = isNextSelectionEnabled,
                            previousSelectionAction = previousSelectionAction,
                            nextSelectionAction = nextSelectionAction,
                            createSelectionAction = createSelectionAction,
                            randomSelectionAction = randomSelectionAction
                        )
                    }
                }
                Box(
                    modifier = Modifier.weight(2f),
                    contentAlignment = Alignment.Center
                ) {
                    SelectionSyllablesColumn(syllables)
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    BottomEmojiRoundButton(text = "🚀", onButtonClick = startGameAction)
                }
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}

@Composable
fun SelectionAction(
    isPreviousSelectionEnabled: Boolean,
    isNextSelectionEnabled: Boolean,
    previousSelectionAction: () -> Unit,
    nextSelectionAction: () -> Unit,
    createSelectionAction: () -> Unit,
    randomSelectionAction: () -> Unit
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

@Preview(showBackground = true)
@Composable
fun StartPortraitLayoutPreview() {
    RusreadTheme {
        StartPortraitLayout(
            isPreviousSelectionEnabled = true,
            isNextSelectionEnabled = true,
            previousSelectionAction = {},
            nextSelectionAction = {},
            createSelectionAction = {},
            randomSelectionAction = {},
            syllables = StartPreviewHelper.scoredSyllables,
            startGameAction = {}
        )
    }
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun StartLandscapeLayoutPreview() {
    RusreadTheme {
        StartLandscapeLayout(
            isPreviousSelectionEnabled = true,
            isNextSelectionEnabled = true,
            previousSelectionAction = {},
            nextSelectionAction = {},
            createSelectionAction = {},
            randomSelectionAction = {},
            syllables = StartPreviewHelper.scoredSyllables,
            startGameAction = {}
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait"
)
@Composable
fun StartPortraitLayoutTabletPreview() {
    StartPortraitLayoutPreview()
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun StartLandscapeLayoutPreviewTabletPreview() {
    StartLandscapeLayoutPreview()
}

private object StartPreviewHelper {
    val chosenSyllables = listOf("ба", "бо", "бу", "бя", "ша", "фу", "цу", "бы", "би", "бе")
    val scoredSyllables = chosenSyllables.map {
        SyllablePreview(
            text = it,
            isSelected = false,
            isEnabled = true,
            isStarred = Random.nextBoolean()
        )
    }
}