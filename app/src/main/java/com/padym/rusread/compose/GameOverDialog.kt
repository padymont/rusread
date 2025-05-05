package com.padym.rusread.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindowProvider
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun GameOverDialog(navController: NavHostController) {
    GameOverDialog2(
        onFinish = { navController.popBackStack() })
}

@Composable
fun GameOverDialog2(onFinish: () -> Unit) {
    (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0f)
    FloatingBigEmoji(onFinish)
}

@Composable
fun FloatingBigEmoji(onFinish: () -> Unit) {
    var visible by remember { mutableStateOf(true) }
    val animationSpec: FiniteAnimationSpec<Float>  = tween(durationMillis = 4500)

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
        delay(1000)
        visible = false
        delay(5500)
        onFinish.invoke()
    }
}

@Preview(showBackground = true)
@Composable
fun GameOverDialogPreview() {
    GameOverDialog2(onFinish = {})
}