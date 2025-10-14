package com.padym.rusread.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.padym.rusread.ui.theme.RusreadTheme

@Composable
fun CongratulationsScreen(onFinishNavigate: () -> Unit) {
    CongratulationsLayout(onFinish = onFinishNavigate)
}

@Composable
fun CongratulationsLayout(onFinish: () -> Unit) {
    Scaffold(
        topBar = { SimpleCloseTopAppBar(onFinish) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedWinEmoji(onFinish)
        }
    }
}

@Composable
fun AnimatedWinEmoji(onFinish: () -> Unit) = AnimatedEmoji(
    emoji = "🏆",
    fontSize = 160.sp,
    durationMillis = 1500,
    onFinish = onFinish
)

@Preview(showBackground = true, device = NORMAL_PORTRAIT, showSystemUi = true)
@Composable
fun CongratulationsPortraitLayoutPreview() {
    RusreadTheme {
        CongratulationsLayout {}
    }
}

@Preview(showBackground = true, device = SMALL_PORTRAIT, showSystemUi = true)
@Composable
fun CongratulationsLayoutSmallPreview() = CongratulationsPortraitLayoutPreview()

@Preview(showBackground = true, device = NORMAL_LANDSCAPE, showSystemUi = true)
@Composable
fun CongratulationsLandscapeLayoutPreview() {
    RusreadTheme {
        CongratulationsLayout {}
    }
}

@Preview(showBackground = true, device = SMALL_LANDSCAPE, showSystemUi = true)
@Composable
fun CongratulationsLandscapeLayoutSmallPreview() = CongratulationsLandscapeLayoutPreview()

@Preview(showBackground = true, device = TABLET_PORTRAIT, showSystemUi = true)
@Composable
fun CongratulationsLayoutTabletPreview() = CongratulationsPortraitLayoutPreview()

@Preview(showBackground = true, device = TABLET_LANDSCAPE, showSystemUi = true)
@Composable
fun CongratulationsLandscapeLayoutTabletPreview() = CongratulationsLandscapeLayoutPreview()
