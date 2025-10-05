package com.padym.rusread.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padym.rusread.ui.theme.AppColors
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.Syllable
import com.padym.rusread.viewmodels.SyllablePreview
import kotlin.random.Random

@Composable
fun RootPortraitBox(
    paddingValues: PaddingValues,
    content: @Composable (BoxScope.() -> Unit)
) = RootBox(
    maxWidth = 500.dp,
    maxHeight = 900.dp,
    paddingValues = paddingValues,
    content = content
)

@Composable
fun RootLandscapeBox(
    paddingValues: PaddingValues,
    content: @Composable (BoxScope.() -> Unit)
) = RootBox(
    maxWidth = 900.dp,
    maxHeight = 500.dp,
    paddingValues = paddingValues,
    content = content
)

@Composable
fun RootBox(
    maxWidth: Dp,
    maxHeight: Dp,
    paddingValues: PaddingValues,
    content: @Composable (BoxScope.() -> Unit)
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .heightIn(max = maxHeight),
            content = content
        )
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
fun EmojiRoundButton(
    text: String = "", isEnabled: Boolean = true, onButtonClick: () -> Unit
) {
    Button(
        onClick = onButtonClick,
        enabled = isEnabled,
        modifier = Modifier
            .clip(CircleShape)
            .size(160.dp)
    ) {
        Text(
            text = text,
            fontSize = 80.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun BottomEmojiRoundButton(
    text: String = "", isEnabled: Boolean = true, onButtonClick: () -> Unit = {}
) = EmojiRoundButton(
    text = text, isEnabled = isEnabled, onButtonClick = onButtonClick
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectionSyllablesRow(syllables: List<SyllablePreview>) = FlowRow(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp)
        .heightIn(min = 268.dp),
    horizontalArrangement = Arrangement.Center,
) {
    syllables.forEach { SyllableFlowRowButton(it) }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectionSyllablesColumn(syllables: List<SyllablePreview>) = FlowRow(
    modifier = Modifier
        .fillMaxHeight(),
//        .widthIn(max = 480.dp),
    horizontalArrangement = Arrangement.Center,
    verticalArrangement = Arrangement.Center
) {
    syllables.forEach { SyllableFlowRowButton(it) }
}

@Composable
fun SyllableFlowRowButton(syllable: SyllablePreview) {
    OutlinedButton(
        enabled = syllable.isEnabled || syllable.isSelected,
        onClick = syllable.onClick,
        contentPadding = PaddingValues(12.dp),
        border = BorderStroke(width = 0.dp, color = Color.Transparent),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            FlowRowButtonText(
                text = syllable.text,
                isSelected = syllable.isSelected,
                isEnabled = syllable.isEnabled
            )
            if (syllable.isStarred) {
                Text(
                    text = "⭐️",
                    fontSize = 20.sp,
                    modifier = Modifier.offset(x = 12.dp, y = (-2).dp)
                )
            }
        }
    }
}

@Composable
fun FlowRowButtonText(
    text: String = "",
    isSelected: Boolean = false,
    isEnabled: Boolean = true,
) {
    val textColor = if (isSelected) {
        AppColors.IndianRed
    } else if (isEnabled) {
        MaterialTheme.colorScheme.onBackground
    } else {
        AppColors.SoftSand
    }
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        color = textColor,
    )
}

@Preview(showBackground = true)
@Composable
fun SelectionSyllablesRowPreview() {
    val chosenSyllables = Syllable.getAll().map { it.key }.shuffled().take(34).sorted()
    val scoredSyllables = chosenSyllables.map {
        val isSelected = Random.nextBoolean()
        val isEnabled = if (isSelected) {
            true
        } else {
            Random.nextBoolean()
        }
        SyllablePreview(
            text = it,
            isSelected = isSelected,
            isEnabled = isEnabled,
            isStarred = Random.nextBoolean()
        )
    }
    RusreadTheme {
        SelectionSyllablesRow(scoredSyllables)
    }
}