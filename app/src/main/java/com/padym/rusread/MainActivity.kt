package com.padym.rusread

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padym.rusread.ui.theme.RusreadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RusreadTheme {
                setContent {
                    SyllableSelectionScreen()
                }
            }
        }
    }
}

data class SyllableGroup(val syllables: List<String>)

fun buildRussianSyllableGroups(): List<SyllableGroup> {
    val consonants = "бвгджзклмнпрстфхцчшщ"
    val vowels = "аеёиоуыэюя"
    val special = "йъ"

    return consonants.map { letter ->
        val syllables = vowels.map { vowel -> letter.toString() + vowel } +
                (letter.toString() + "ь") +
                letter.toString()
        SyllableGroup(syllables)
    } + SyllableGroup(vowels.map { it.toString() }) +
            SyllableGroup(special.map { it.toString() })
}

@Composable
fun SyllableSelectionScreen(initialSelectedCount: Int = 0) {
    var selectedSyllablesCount by remember { mutableIntStateOf(initialSelectedCount) }
    val syllableGroups = buildRussianSyllableGroups()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Choose at least 3 syllables",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(syllableGroups) { group ->
                SyllableGroupItem(group) { syllable ->
                    if (syllable.isSelected) {
                        selectedSyllablesCount++
                    } else {
                        selectedSyllablesCount--
                    }
                }
            }
        }

        if (selectedSyllablesCount >= 3) {
            Button(
                onClick = { /* Handle next button click */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Next")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SyllableGroupItem(group: SyllableGroup, onSyllableSelected: (Syllable) -> Unit) {
    var selectedSyllables by remember { mutableStateOf(emptySet<String>()) }
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        FlowRow(modifier = Modifier.padding(8.dp)) {
            group.syllables.forEach { syllable ->
                SyllableItem(
                    syllable = syllable,
                    isSelected = syllable in selectedSyllables,
                    onToggle = { isSelected ->
                        if (isSelected) {
                            selectedSyllables = selectedSyllables + syllable
                        } else {
                            selectedSyllables = selectedSyllables - syllable
                        }
                        onSyllableSelected(Syllable(syllable, isSelected))
                    }
                )
            }
        }
    }
}

data class Syllable(val text: String, val isSelected: Boolean)

@Composable
fun SyllableItem(syllable: String, isSelected: Boolean, onToggle: (Boolean) -> Unit) {
    OutlinedButton(
        onClick = { onToggle(!isSelected) },
        modifier = Modifier.padding(end = 6.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
        )
    ) {
        Text(text = syllable)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SyllableSelectionScreen(initialSelectedCount = 3)
}