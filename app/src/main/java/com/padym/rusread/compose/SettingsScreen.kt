package com.padym.rusread.compose

import android.content.res.Configuration
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    onCloseNavigate: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val params = SettingsScreenParameters(
        onClose = onCloseNavigate
    )

    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> SettingsPortraitLayout(params)
        else -> SettingsLandscapeLayout(params)
    }
}

data class SettingsScreenParameters(
    val onClose: () -> Unit = {}
)

@Composable
fun SettingsPortraitLayout(params: SettingsScreenParameters) {
    Scaffold(
        topBar = { SimpleCloseTopAppBar(params.onClose) }
    ) { paddingValues ->
        RootPortraitBox(paddingValues) {
        }
    }
}

@Composable
fun SettingsLandscapeLayout(params: SettingsScreenParameters) {
    Scaffold(
        topBar = { SimpleCloseTopAppBar(params.onClose) },
    ) { paddingValues ->
        RootLandscapeBox(paddingValues) {
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPortraitLayoutPreview() {
    RusreadTheme {
        SettingsPortraitLayout(SettingsPreviewHelper.params)
    }
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun SettingsLandscapeLayoutPreview() {
    RusreadTheme {
        SettingsLandscapeLayout(SettingsPreviewHelper.params)
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait"
)
@Composable
fun SettingsPortraitLayoutTabletPreview() = SettingsPortraitLayoutPreview()

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun SettingsLandscapeLayoutTabletPreview() = SettingsLandscapeLayoutPreview()

private object SettingsPreviewHelper {
    val params = SettingsScreenParameters(
        onClose = {}
    )
}