package com.padym.rusread.compose

import android.content.res.Configuration
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.padym.rusread.ui.theme.AppColors
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.GameViewModel
import com.padym.rusread.viewmodels.Result
import kotlinx.coroutines.delay
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun GameScreen(
    onCloseNavigate: () -> Unit,
    onFinishGameNavigate: () -> Unit,
    viewModel: GameViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = viewModel.isAudioLoading) {
        if (!viewModel.isAudioLoading) {
            viewModel.speakSyllable()
        }
    }

    if (!viewModel.isGameOn) {
        LaunchedEffect(Unit) {
            delay(200)
            onFinishGameNavigate()
        }
    }

    val params = GameScreenParameters(
        syllables = viewModel.syllables,
        gameProgress = viewModel.gameProgress,
        onAudio = { if (viewModel.isGameOn) viewModel.speakSyllable() },
        onSyllable = { syllable -> viewModel.processAnswer(syllable) },
        onClose = onCloseNavigate
    )

    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> GamePortraitLayout(params)
        else -> GameLandscapeLayout(params)
    }
}

data class GameScreenParameters(
    val syllables: Set<String> = emptySet(),
    val gameProgress: Float = 0f,
    val onAudio: () -> Unit = {},
    val onSyllable: (String) -> Result = { _ -> Result.CORRECT },
    val onClose: () -> Unit = {}
)

@Composable
fun GamePortraitLayout(params: GameScreenParameters) {
    Scaffold(
        topBar = { SimpleCloseTopAppBar(params.onClose) },
        bottomBar = {
            Box(modifier = Modifier.height(12.dp)) {
                ProgressBottomBar(params.gameProgress)
            }
        }
    ) { paddingValues ->
        RootPortraitBox(paddingValues) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.height(200.dp)
                ) {
                    EmojiRoundButton("🎧") { params.onAudio() }
                }
                ScatteredSyllablesButtons(params.syllables) { syllable ->
                    params.onSyllable(syllable)
                }
            }
        }
    }
}

@Composable
fun GameLandscapeLayout(params: GameScreenParameters) {
    Scaffold(
        topBar = { SimpleCloseTopAppBar(params.onClose) },
        bottomBar = {
            Box(modifier = Modifier.height(12.dp)) {
                ProgressBottomBar(params.gameProgress)
            }
        }
    ) { paddingValues ->
        RootLandscapeBox(paddingValues) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(24.dp))
                EmojiRoundButton("🎧") { params.onAudio() }
                ScatteredSyllablesButtons(params.syllables) { syllable ->
                    params.onSyllable(syllable)
                }
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}

@Composable
fun ScatteredSyllablesButtons(
    selectedSyllables: Set<String>,
    onSyllableClick: (String) -> Result
) {
    Layout(
        modifier = Modifier.padding(8.dp),
        content = {
            selectedSyllables.forEach { syllable ->
                InteractiveSyllableButton(syllable) { onSyllableClick(syllable) }
            }
        }
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val buttonList = mutableListOf<Pair<Float, Float>>()
        placeables.forEach { placeable ->
            val coordinates = generateRandomPosition(
                buttonList,
                constraints.maxWidth,
                constraints.maxHeight,
                placeable.width,
                placeable.height
            )
            buttonList.add(coordinates)
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                val (buttonX, buttonY) = buttonList[index]
                placeable.placeRelative(buttonX.toInt(), buttonY.toInt())
            }
        }
    }
}

@Composable
fun InteractiveSyllableButton(syllable: String, onClick: () -> Result) {
    var showAnimation by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf(Result.WRONG) }
    val animatedY by animateFloatAsState(
        targetValue = if (showAnimation) -20f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow
        ),
        label = "AnimatedEmoji",
    )
    Box(contentAlignment = Alignment.TopEnd) {
        SyllableButton(syllable, enabled = !showAnimation) {
            result = onClick()
            showAnimation = true
        }
        if (showAnimation) {
            when (result) {
                Result.CORRECT -> AnimatedEmoji(animatedY)
                Result.WRONG -> SadEmoji()
            }
            LaunchedEffect(key1 = showAnimation) {
                delay(1000)
                showAnimation = false
            }
        }
    }
}

@Composable
fun SyllableButton(syllable: String, enabled: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        enabled = enabled,
        onClick = onClick,
        border = BorderStroke(width = 0.dp, color = Color.Transparent),
    ) {
        Text(
            text = syllable,
            fontSize = 48.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun AnimatedEmoji(animatedY: Float) = Text(
    text = "🚀",
    fontSize = 36.sp,
    modifier = Modifier.offset(y = animatedY.dp)
)

@Composable
fun SadEmoji() = Text(
    text = "💩",
    fontSize = 32.sp,
    modifier = Modifier.offset(
        x = 10f.dp,
        y = (-10f).dp
    )
)

@Composable
fun ProgressBottomBar(progress: Float) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColors.MetallicGold,
        trackColor = AppColors.Almond,
        strokeCap = StrokeCap.Butt,
        gapSize = 0.dp,
        drawStopIndicator = {}
    )
}

fun generateRandomPosition(
    existingButtons: List<Pair<Float, Float>>,
    screenWidth: Int,
    screenHeight: Int,
    buttonWidth: Int,
    buttonHeight: Int
): Pair<Float, Float> {
    var x: Float
    var y: Float
    do {
        x = Random.nextInt(screenWidth - buttonWidth).toFloat()
        y = Random.nextInt(screenHeight - buttonHeight).toFloat()
    } while (existingButtons.any { (buttonX, buttonY) ->
            overlaps(x, y, buttonX, buttonY, buttonWidth)
        })
    return Pair(x, y)
}

fun overlaps(
    x1: Float,
    y1: Float,
    x2: Float,
    y2: Float,
    size: Int
): Boolean {
    val distance = sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
    return distance < size
}

@Preview(showBackground = true, device = NORMAL_PORTRAIT)
@Composable
fun GamePortraitLayoutPreview() {
    RusreadTheme {
        GamePortraitLayout(GamePreviewHelper.params)
    }
}

//@Preview(showBackground = true, device = SMALL_PORTRAIT)
//@Composable
//fun GamePortraitLayoutSmallPreview() = GamePortraitLayoutPreview()

@Preview(showBackground = true, device = NORMAL_LANDSCAPE)
@Composable
fun GameLandscapeLayoutPreview() {
    RusreadTheme {
        GameLandscapeLayout(GamePreviewHelper.params)
    }
}

//@Preview(showBackground = true, device = SMALL_LANDSCAPE)
//@Composable
//fun GameLandscapeLayoutSmallPreview() = GameLandscapeLayoutPreview()

@Preview(showBackground = true, device = TABLET_PORTRAIT)
@Composable
fun GamePortraitLayoutTabletPreview() = GamePortraitLayoutPreview()

@Preview(showBackground = true, device = TABLET_LANDSCAPE)
@Composable
fun GameLandscapeLayoutTabletPreview() = GameLandscapeLayoutPreview()

private object GamePreviewHelper {
    val selectedSyllables = setOf("до", "ме", "мя", "ко", "ба", "са", "л", "жу")
    val params = GameScreenParameters(
        gameProgress = 0.7f,
        syllables = selectedSyllables
    )
}