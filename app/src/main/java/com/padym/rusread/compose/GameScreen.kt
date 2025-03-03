package com.padym.rusread.compose

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.padym.rusread.ui.theme.AppColors
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.GameViewModel
import com.padym.rusread.viewmodels.RIGHT_ANSWER_NUMBER
import com.padym.rusread.viewmodels.Result
import kotlinx.coroutines.delay
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun GameScreen(navController: NavHostController) {
    val viewModel: GameViewModel = hiltViewModel()

    LaunchedEffect(key1 = viewModel.isAudioLoading) {
        if (!viewModel.isAudioLoading) {
            viewModel.speakSyllable(viewModel.spokenSyllable)
        }
    }
    GameScreen2(
        onCloseClick = { navController.popBackStack() },
        gameProgress = viewModel.gameProgress,
        spokenSyllable = viewModel.spokenSyllable,
        onSpokenSyllableClick = { syllable -> viewModel.speakSyllable(syllable) },
        onSyllableClick = { syllable -> viewModel.processAnswer(syllable) },
        isGameOn = viewModel.isGameOn,
        syllables = viewModel.syllables,
        scores = viewModel.scores
    )
}

@Composable
fun GameScreen2(
    onCloseClick: () -> Unit,
    gameProgress: Float,
    spokenSyllable: String,
    onSpokenSyllableClick: (String) -> Unit,
    onSyllableClick: (String) -> Result,
    isGameOn: Boolean,
    syllables: Set<String>,
    scores: List<Pair<String, Int>>
) {
    Scaffold(
        topBar = { SimpleCloseTopAppBar(onCloseClick) },
        bottomBar = {
            Box(modifier = Modifier.height(12.dp)) {
                if (isGameOn) ProgressBottomBar(gameProgress)
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Box(
                modifier = Modifier.height(200.dp)
            ) {
                if (isGameOn) {
//                DebugSyllablesAudioOffsets { offset -> viewModel.speakSyllable(offset) }
                    EmojiRoundButton("🎧") {
//                EmojiRoundButton(spokenSyllable) {
                        onSpokenSyllableClick(spokenSyllable)
                    }
                } else {
                    EndGameEmoji()
                }
            }
            ScatteredSyllablesButtons(
                isGameOn = isGameOn,
                selectedSyllables = syllables,
                scores = scores,
            ) { syllable -> onSyllableClick(syllable) }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleCloseTopAppBar(onClose: () -> Unit = {}) {
    TopAppBar(
        title = { Text("") },
        modifier = Modifier.padding(8.dp),
        actions = {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(48.dp)
                )
            }
        },
    )
}

@Composable
fun ScatteredSyllablesButtons(
    isGameOn: Boolean,
    selectedSyllables: Set<String>,
    scores: List<Pair<String, Int>>,
    onSyllableClick: (String) -> Result
) {
    Layout(
        modifier = Modifier.padding(8.dp),
        content = {
            selectedSyllables.forEach { syllable ->
                InteractiveSyllableButton(
                    isGameOn,
                    syllable,
                    scores.find { it.first == syllable }?.second ?: 0
                ) { onSyllableClick(syllable) }
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
fun InteractiveSyllableButton(
    isGameOn: Boolean,
    syllable: String,
    score: Int,
    onClick: () -> Result
) {
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
        if (!isGameOn) {
            LinearProgressIndicator(
                progress = { score.toFloat() / RIGHT_ANSWER_NUMBER },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(64.dp)
                    .height(4.dp),
                color = AppColors.MetallicGold,
                trackColor = AppColors.Almond,
                strokeCap = StrokeCap.Butt,
                gapSize = 0.dp,
                drawStopIndicator = {}
            )
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

@Composable
fun EndGameEmoji() {
    Text(
        text = "🏆",
        fontSize = 160.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun DebugSyllablesAudioOffsets(action: (Int) -> Unit) {
    var text by remember { mutableStateOf("") }
    var intValue by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            singleLine = true,
            onValueChange = {
                text = it
                intValue = it.toIntOrNull() ?: 0
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(top = 64.dp)
        )
        Button(
            onClick = { action(intValue) },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(text = "Test the offset")
        }
    }
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

@Preview(showBackground = true)
@Composable
fun SyllableGameContentPreview() {
    RusreadTheme {
        GameScreen2(
            onCloseClick = { },
            gameProgress = 0.7f,
            spokenSyllable = PreviewHelper.selectedSyllables.random(),
            onSpokenSyllableClick = { },
            onSyllableClick = { _ -> Result.entries.random() },
            isGameOn = true,
            syllables = PreviewHelper.selectedSyllables,
            scores = PreviewHelper.scores
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SyllableEndGamePreview() {
    RusreadTheme {
        GameScreen2(
            onCloseClick = { },
            gameProgress = 0.7f,
            spokenSyllable = PreviewHelper.selectedSyllables.random(),
            onSpokenSyllableClick = { },
            onSyllableClick = { _ -> Result.entries.random() },
            isGameOn = false,
            syllables = PreviewHelper.selectedSyllables,
            scores = PreviewHelper.scores
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DebugSyllablesAudioOffsetsPreview() {
    RusreadTheme {
        Scaffold(
            topBar = { SimpleCloseTopAppBar { } },
            bottomBar = { ProgressBottomBar(0.7f) }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                DebugSyllablesAudioOffsets { }
            }
        }
    }
}

object PreviewHelper {
    val selectedSyllables = setOf("до", "ме", "мя", "ко", "ба", "са", "л", "жу")
    val scores = listOf(
        "до" to 1,
        "ме" to 2,
        "мя" to 3,
        "ко" to 4,
        "ба" to 5,
        "са" to 7,
        "л" to 8,
        "жу" to 10
    )
}