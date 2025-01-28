package com.padym.rusread.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padym.rusread.viewmodels.SyllablePreview

@Composable
fun EmojiRoundButton(text: String, paddingBottom: Int = 0, onButtonClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = paddingBottom.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onButtonClick,
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
}

@Composable
fun BottomEmojiRoundButton(text: String, onButtonClick: () -> Unit) =
    EmojiRoundButton(text, paddingBottom = 80, onButtonClick)

@Composable
fun EmojiIconButton(text: String, isVisible: Boolean = true, onButtonClick: () -> Unit) {
    OutlinedButton(
        onClick = onButtonClick,
        contentPadding = PaddingValues(0.dp),
        border = BorderStroke(width = 0.dp, color = Color.Transparent),
        modifier = Modifier
            .clip(CircleShape)
            .size(64.dp)
            .alpha(if (isVisible) 1f else 0f)
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectionSyllablesRow(content: @Composable() (RowScope.() -> Unit)) = FlowRow(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp)
        .heightIn(min = 268.dp)
        .padding(bottom = 48.dp),
    horizontalArrangement = Arrangement.Center,
    content = content
)

@Composable
fun SyllableSelection(syllables: List<SyllablePreview>) {
    return syllables.forEach { SyllableFlowRowButton(it) }
}

@Composable
fun SyllableFlowRowButton(syllable: SyllablePreview) {
    FlowRowButton(onClick = syllable.onClick) {
        Box(contentAlignment = Alignment.TopEnd) {
            FlowRowButtonText(syllable.text)
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
fun ActionFlowRowButton(text: String, onClick: () -> Unit) {
    FlowRowButton(onClick = onClick) {
        Box(contentAlignment = Alignment.TopEnd) {
            FlowRowButtonText(text)
        }
    }
}

@Composable
fun FlowRowButton(onClick: () -> Unit = {}, content: @Composable() (RowScope.() -> Unit)) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = PaddingValues(12.dp),
        border = BorderStroke(width = 0.dp, color = Color.Transparent),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
        content = content
    )
}

@Composable
fun FlowRowButtonText(text: String) = Text(
    text = text,
    fontWeight = FontWeight.Bold,
    fontSize = 40.sp,
    color = MaterialTheme.colorScheme.onBackground,
)