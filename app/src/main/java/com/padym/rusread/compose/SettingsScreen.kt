package com.padym.rusread.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.padym.rusread.R
import com.padym.rusread.ui.theme.AppColors
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.SettingsViewModel
import kotlinx.coroutines.delay

const val TOOLTIP_BOTTOM_OFFSET = 48

@Composable
fun SettingsScreen(
    onCloseNavigate: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val currentStarScore by viewModel.currentStarScore.collectAsState()

    val params = SettingsScreenParameters(
        currentStarScore = currentStarScore,
        isTooltipOn = viewModel.isTooltipOn,
        onStarScoreChange = { newScore ->
            viewModel.setStarScore(newScore)
            viewModel.showTooltip()
        },
        onClearProgress = {
            viewModel.clearProgress()
            viewModel.showTooltip()
        },
        onTooltipDismiss = { viewModel.hideTooltip() },
        onClose = onCloseNavigate
    )

    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> SettingsPortraitLayout(params)
        else -> SettingsLandscapeLayout(params)
    }
}

data class SettingsScreenParameters(
    val currentStarScore: Int = 0,
    val isTooltipOn: Boolean = false,
    val onStarScoreChange: (Int) -> Unit = {},
    val onClearProgress: () -> Unit = {},
    val onTooltipDismiss: () -> Unit = {},
    val onClose: () -> Unit = {}
)

@Composable
fun SettingsPortraitLayout(params: SettingsScreenParameters) {
    Scaffold(
        topBar = { SimpleCloseTopAppBar(params.onClose) }
    ) { paddingValues ->
        RootPortraitBox(paddingValues) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                StarScore(
                    currentScore = params.currentStarScore,
                    onScoreChange = params.onStarScoreChange
                )
                Spacer(modifier = Modifier.height(80.dp))
                DeleteProgress(onClick = params.onClearProgress)
                if (params.isTooltipOn) {
                    Tooltip(onDismissRequest = params.onTooltipDismiss)
                }
            }
        }
    }
}

@Composable
fun SettingsLandscapeLayout(params: SettingsScreenParameters) {
    Scaffold(
        topBar = { SimpleCloseTopAppBar(params.onClose) },
    ) { paddingValues ->
        RootLandscapeBox(paddingValues) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    StarScore(
                        currentScore = params.currentStarScore,
                        onScoreChange = params.onStarScoreChange
                    )
                }
                Column(
                    modifier = Modifier.weight(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DeleteProgress(onClick = params.onClearProgress)
                }
            }
            if (params.isTooltipOn) {
                Tooltip(onDismissRequest = params.onTooltipDismiss)
            }
        }
    }
}

@Composable
fun StarScore(currentScore: Int, onScoreChange: (Int) -> Unit) {
    val startValue = 5
    val steps = 15
    val currentValue = currentScore
    Text(
        text = stringResource(R.string.right_answers_as_done, currentValue),
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(24.dp))
    Slider(
        modifier = Modifier.padding(horizontal = 24.dp),
        value = currentValue.toFloat(),
        onValueChange = { newValue -> onScoreChange(newValue.toInt()) },
        valueRange = startValue.toFloat()..(startValue + steps).toFloat(),
        steps = steps - 1,
        colors = SliderDefaults.colors(
            thumbColor = AppColors.IndianRed,
            activeTrackColor = AppColors.IndianRed,
            inactiveTrackColor = AppColors.Linen,
            activeTickColor = AppColors.Linen,
            inactiveTickColor = AppColors.IndianRed,
        )
    )
}

@Composable
fun DeleteProgress(onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.delete_progress),
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    EmojiRoundIconButton(
        text = "💣",
        size = 100.dp,
        fontSize = 60.sp,
        onButtonClick = onClick,
    )
}

@Composable
fun Tooltip(onDismissRequest: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500L)
        onDismissRequest()
    }

    val density = LocalDensity.current
    Popup(
        alignment = Alignment.BottomCenter,
        offset = with(density) { IntOffset(0, -TOOLTIP_BOTTOM_OFFSET.dp.roundToPx()) },
        onDismissRequest = onDismissRequest
    ) {
        TooltipContent()
    }
}

@Composable
fun TooltipContent() {
    Box(
        modifier = Modifier
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .background(
                color = AppColors.Almond,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = stringResource(R.string.tooltip_done),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            fontSize = 24.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PreviewTooltip() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = TOOLTIP_BOTTOM_OFFSET.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        TooltipContent()
    }
}

@Preview(showBackground = true, device = NORMAL_PORTRAIT, showSystemUi = true)
@Composable
fun SettingsPortraitLayoutPreview() {
    RusreadTheme {
        SettingsPortraitLayout(SettingsPreviewHelper.params)
        PreviewTooltip()
    }
}

@Preview(showBackground = true, device = SMALL_PORTRAIT, showSystemUi = true)
@Composable
fun SettingsPortraitLayoutSmallPreview() = SettingsPortraitLayoutPreview()

@Preview(showBackground = true, device = NORMAL_LANDSCAPE, showSystemUi = true)
@Composable
fun SettingsLandscapeLayoutPreview() {
    RusreadTheme {
        SettingsLandscapeLayout(SettingsPreviewHelper.params)
        PreviewTooltip()
    }
}

@Preview(showBackground = true, device = SMALL_LANDSCAPE, showSystemUi = true)
@Composable
fun SettingsLandscapeLayoutSmallPreview() = SettingsLandscapeLayoutPreview()

@Preview(showBackground = true, device = TABLET_PORTRAIT, showSystemUi = true)
@Composable
fun SettingsPortraitLayoutTabletPreview() = SettingsPortraitLayoutPreview()

@Preview(showBackground = true, device = TABLET_LANDSCAPE, showSystemUi = true)
@Composable
fun SettingsLandscapeLayoutTabletPreview() = SettingsLandscapeLayoutPreview()

private object SettingsPreviewHelper {
    val params = SettingsScreenParameters(
        currentStarScore = 10,
        isTooltipOn = false,
        onClose = {}
    )
}