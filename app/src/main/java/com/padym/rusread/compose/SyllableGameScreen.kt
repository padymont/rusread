package com.padym.rusread.compose

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.padym.rusread.ui.theme.AppColors
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.Result
import com.padym.rusread.viewmodels.SyllableGameViewModel
import kotlinx.coroutines.delay
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun SyllableGameScreen(navController: NavHostController, chosenSyllables: Set<String>) {
    val viewModel: SyllableGameViewModel = viewModel()
    viewModel.initializeData(chosenSyllables)

    Scaffold(
        topBar = {
            SimpleCloseTopAppBar() { navController.popBackStack() }
        },
        bottomBar = { ProgressBottomBar(viewModel.gameProgress) }
    ) { paddingValues ->
        if (viewModel.isGameOn) {
            Column(modifier = Modifier.padding(paddingValues)) {
                SpeakSyllableButton(viewModel.spokenSyllable) {
                    viewModel.speakText(viewModel.spokenSyllable)
                }
                ScatteredSyllablesButtons(viewModel.syllables) { syllable ->
                    viewModel.processAnswer(syllable)
                }
            }
        } else {
            EndGameMessage()
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
fun SpeakSyllableButton(syllable: String, onButtonClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onButtonClick,
            modifier = Modifier
                .clip(CircleShape)
                .size(160.dp)
        ) {
            Text(
                text = "🎧",
//              text = syllable,
                fontSize = 80.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ScatteredSyllablesButtons(selectedSyllables: Set<String>, onSyllableClick: (String) -> Result) {
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
            .height(12.dp),
        color = AppColors.MetallicGold,
        trackColor = AppColors.Almond
    )
}

@Composable
fun EndGameMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Great job!", fontSize = 32.sp)
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
    val selectedSyllables = setOf("до", "ме", "мя", "ко", "ба", "са", "л", "жу")
    RusreadTheme {
        Scaffold(
            topBar = { SimpleCloseTopAppBar { } },
            bottomBar = { ProgressBottomBar(0.7f) }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                SpeakSyllableButton("жа") {}
                ScatteredSyllablesButtons(selectedSyllables) { _ -> Result.entries.random() }
            }
        }
    }
}