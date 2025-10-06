package com.padym.rusread.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.padym.rusread.ui.theme.RusreadTheme
import kotlinx.coroutines.delay

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
fun AnimatedWinEmoji(onFinish: () -> Unit) {
    val scale = remember { Animatable(1f) }
    LaunchedEffect(key1 = Unit) {
        scale.animateTo(
            targetValue = 1.1f,
            animationSpec = InfiniteRepeatableSpec(
                animation = tween(durationMillis = 300),
                repeatMode = RepeatMode.Reverse
            )
        )
    }
    LaunchedEffect(key1 = Unit) {
        delay(1500)
        onFinish.invoke()
    }

    Text(
        modifier = Modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        },
        text = "🏆",
        fontSize = 160.sp,
        textAlign = TextAlign.Center,
    )
}

@Preview(showBackground = true)
@Composable
fun CongratulationsPortraitLayoutPreview() {
    RusreadTheme {
        CongratulationsLayout {}
    }
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun CongratulationsLandscapeLayoutPreview() {
    RusreadTheme {
        CongratulationsLayout {}
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait"
)
@Composable
fun CongratulationsLayoutTabletPreview() = CongratulationsPortraitLayoutPreview()

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun CongratulationsLandscapeLayoutTabletPreview() = CongratulationsLandscapeLayoutPreview()
