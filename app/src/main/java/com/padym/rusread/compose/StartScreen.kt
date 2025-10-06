package com.padym.rusread.compose

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.StartViewModel
import com.padym.rusread.viewmodels.SyllablePreview
import kotlin.random.Random

@Composable
fun StartScreen(
    onCreateListNavigate: () -> Unit,
    onGameStartNavigate: () -> Unit,
    viewModel: StartViewModel = hiltViewModel()
) {
    val currentGroup by viewModel.currentGroup.collectAsState()
    val actionParams = ActionParameters(
        isPreviousEnabled = currentGroup.isPreviousEnabled,
        isNextEnabled = currentGroup.isNextEnabled,
        onPrevious = { viewModel.selectPreviousGroup() },
        onNext = { viewModel.selectNextGroup() },
        onRandom = { viewModel.generateGroup() },
        onCreate = onCreateListNavigate,
    )
    val params = StartScreenParameters(
        syllables = currentGroup.syllables,
        actionParams = actionParams,
        onGame = {
            viewModel.fixCurrentGroup()
            onGameStartNavigate.invoke()
        }
    )

    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> StartPortraitLayout(params)
        else -> StartLandscapeLayout(params)
    }
}

data class StartScreenParameters(
    val syllables: List<SyllablePreview> = emptyList(),
    val actionParams: ActionParameters = ActionParameters(),
    val onGame: () -> Unit = {},
)

data class ActionParameters(
    val isPreviousEnabled: Boolean = false,
    val isNextEnabled: Boolean = false,
    val onPrevious: () -> Unit = {},
    val onNext: () -> Unit = {},
    val onRandom: () -> Unit = {},
    val onCreate: () -> Unit = {},
)

@Composable
fun StartPortraitLayout(params: StartScreenParameters) {
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
                    ActionButtons(params.actionParams)
                }
                SelectionSyllablesRow(params.syllables)
                Spacer(modifier = Modifier.height(48.dp))
                BottomEmojiRoundButton(text = "🚀", onButtonClick = params.onGame)
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun StartLandscapeLayout(params: StartScreenParameters) {
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
                        ActionButtons(params.actionParams)
                    }
                }
                Box(
                    modifier = Modifier.weight(2f),
                    contentAlignment = Alignment.Center
                ) {
                    SelectionSyllablesColumn(params.syllables)
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    BottomEmojiRoundButton(text = "🚀", onButtonClick = params.onGame)
                }
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}

@Composable
fun ActionButtons(params: ActionParameters) = with(params) {
    EmojiIconButton(text = "👈", isVisible = isPreviousEnabled, onButtonClick = onPrevious)
    EmojiIconButton(text = "👉", isVisible = isNextEnabled, onButtonClick = onNext)
    EmojiIconButton(text = "🖍", onButtonClick = onCreate)
    EmojiIconButton(text = "🎲", onButtonClick = onRandom)
}

@Composable
fun EmojiIconButton(text: String, isVisible: Boolean = true, onButtonClick: () -> Unit) {
    OutlinedButton(
        onClick = onButtonClick,
        contentPadding = PaddingValues(0.dp),
        border = BorderStroke(width = 0.dp, color = Color.Transparent),
        modifier = Modifier
            .clip(CircleShape)
            .size(64.dp)
            .alpha(if (isVisible) 1f else 0f)
    ) {
        Text(text = text, fontSize = 32.sp, textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true)
@Composable
fun StartPortraitLayoutPreview() {
    RusreadTheme {
        StartPortraitLayout(StartPreviewHelper.params)
    }
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun StartLandscapeLayoutPreview() {
    RusreadTheme {
        StartLandscapeLayout(StartPreviewHelper.params)
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait"
)
@Composable
fun StartPortraitLayoutTabletPreview() = StartPortraitLayoutPreview()

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun StartLandscapeLayoutPreviewTabletPreview() = StartLandscapeLayoutPreview()

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
    val actionParams = ActionParameters(isPreviousEnabled = true, isNextEnabled = true)
    val params = StartScreenParameters(syllables = scoredSyllables, actionParams = actionParams)
}