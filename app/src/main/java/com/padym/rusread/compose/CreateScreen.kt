package com.padym.rusread.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.padym.rusread.ui.theme.AppColors
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.CreateViewModel
import com.padym.rusread.viewmodels.Syllable
import com.padym.rusread.viewmodels.SyllablePreview
import kotlin.random.Random

@Composable
fun CreateScreen(
    onCloseNavigate: () -> Unit,
    onSaveNavigate: () -> Unit,
    viewModel: CreateViewModel = hiltViewModel()
) {
    val syllables by viewModel.syllablePreviewGroup.collectAsState()
    val isSaveEnabled by viewModel.isSaveEnabled.collectAsState()
    val params = CreateScreenParameters(
        syllables = syllables,
        isSaveEnabled = isSaveEnabled,
        onClose = onCloseNavigate,
        onSave = {
            viewModel.saveSyllableList()
            onSaveNavigate.invoke()
        }
    )

    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> CreatePortraitLayout(params)
        else -> CreateLandscapeLayout(params)
    }
}

data class CreateScreenParameters(
    val syllables: List<SyllablePreview> = emptyList(),
    val isSaveEnabled: Boolean = false,
    val onClose: () -> Unit = {},
    val onSave: () -> Unit = {},
)

@Composable
fun CreatePortraitLayout(params: CreateScreenParameters) {
    Scaffold(
        topBar = {
            Column {
                SimpleCloseTopAppBar { params.onClose() }
                HorizontalDivider(thickness = 2.dp, color = AppColors.SoftSand)
            }
        },
        bottomBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(thickness = 2.dp, color = AppColors.SoftSand)
                Spacer(modifier = Modifier.height(16.dp))
                val text = if (params.isSaveEnabled) "👍" else ""
                BottomEmojiRoundButton(
                    text = text,
                    isEnabled = params.isSaveEnabled
                ) { params.onSave() }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    ) { paddingValues ->
        RootPortraitBox(paddingValues) {
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                SelectionSyllablesRow(params.syllables)
            }
        }
    }
}

@Composable
fun CreateLandscapeLayout(params: CreateScreenParameters) {
    Scaffold(
        topBar = {
            Column {
                SimpleCloseTopAppBar { params.onClose() }
                HorizontalDivider(thickness = 2.dp, color = AppColors.SoftSand)
            }
        },
        bottomBar = { HorizontalDivider(thickness = 2.dp, color = AppColors.SoftSand) }
    ) { paddingValues ->
        RootLandscapeBox(paddingValues) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(24.dp))
                Box(
                    modifier = Modifier.weight(3f),
                    contentAlignment = Alignment.Center
                ) {
                    val scrollState = rememberScrollState()
                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        SelectionSyllablesColumn(params.syllables)
                        Spacer(modifier = Modifier.height(36.dp))
                    }
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    val text = if (params.isSaveEnabled) "👍" else ""
                    BottomEmojiRoundButton(
                        text = text,
                        isEnabled = params.isSaveEnabled
                    ) { params.onSave() }
                }
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePortraitLayoutPreview() {
    RusreadTheme {
        CreatePortraitLayout(params = CreatePreviewHelper.params)
    }
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun CreateLandscapeLayoutPreview() {
    RusreadTheme {
        CreateLandscapeLayout(params = CreatePreviewHelper.params)
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait"
)
@Composable
fun CreatePortraitLayoutTabletPreview() {
    CreatePortraitLayoutPreview()
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun CreateLandscapeLayoutTabletPreview() {
    CreateLandscapeLayoutPreview()
}

private object CreatePreviewHelper {
    val chosenSyllables = Syllable.getAll().map { it.key }.shuffled().take(30).sorted()
    val scoredSyllables = chosenSyllables.map {
        val isSelected = Random.nextBoolean()
        val isEnabled = if (isSelected) true else Random.nextBoolean()
        SyllablePreview(
            text = it,
            isSelected = isSelected,
            isEnabled = isEnabled,
            isStarred = Random.nextBoolean()
        )
    }
    val params = CreateScreenParameters(syllables = scoredSyllables)
}