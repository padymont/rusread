package com.padym.rusread.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
            FloatingBigEmoji(onFinish)
        }
    }
}

@Composable
fun FloatingBigEmoji(onFinish: () -> Unit) {
    var visible by remember { mutableStateOf(true) }
    val animationSpec: FiniteAnimationSpec<Float> = tween(durationMillis = 4500)

    AnimatedVisibility(
        visible = visible,
        exit = fadeOut(animationSpec) + scaleOut(animationSpec),
    ) {
        Text(
            text = "🏆",
            fontSize = 160.sp,
        )
    }
    LaunchedEffect(key1 = "FloatingBigEmoji") {
        delay(300)
        visible = false
        delay(1500)
        onFinish.invoke()
    }
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
