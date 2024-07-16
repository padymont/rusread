package com.padym.rusread

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.padym.rusread.ui.theme.RusreadTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RusreadTheme {
                setContent {
                    navController = rememberNavController()
                    setupNavGraph(navController)
                }
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun setupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.SyllableSelection.route
    ) {
        composable(Screen.SyllableSelection.route) {
            SyllableSelectionScreen(navController)
        }
        composable(Screen.SyllableGame.route) { backStackEntry ->
            val chosenSyllables = Screen.SyllableGame.parseChosenSyllables(backStackEntry)
            SyllableGameScreen(navController, chosenSyllables)
        }
    }
}

@Composable
fun SyllableSelectionScreen(navController: NavHostController) {
    val viewModel: SyllableListViewModel = viewModel()
    val selectedSyllables = viewModel.selectedSyllables

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
            items(viewModel.getGroupedSyllables()) { group ->
                SyllableGroupItem(group.syllables) { syllable ->
                    if (syllable.isSelected) {
                        viewModel.addSyllable(syllable.text)
                    } else {
                        viewModel.removeSyllable(syllable.text)
                    }
                }
            }
        }

        if (selectedSyllables.size >= 3) {
            Button(
                onClick = {
                    navController.navigate(
                        Screen.SyllableGame.passChosenSyllables(selectedSyllables)
                    )
                    viewModel.clearChosenSyllables()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Next")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SyllableGameScreen(navController: NavHostController, chosenSyllables: Set<String>) {
    val context = LocalContext.current
    val tts = remember { mutableStateOf<TextToSpeech?>(null) }

    // Initialize TTS
    LaunchedEffect(key1 = Unit) {
        tts.value = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.value?.setLanguage(Locale("ru"))
                val voice = tts.value?.voices?.find { it.name.contains("ru-ru-x-rud-network") }
                tts.value?.setVoice(voice)
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Toast.makeText(context, "Russian language not supported", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(context, "TTS initialization failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val viewModel: SyllableGameViewModel = viewModel()
    viewModel.initializeData(chosenSyllables)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") }, // Empty title
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                })
        }
    ) { paddingValues ->
        val currentSyllable = viewModel.newSyllable
        if (viewModel.correctAnswers < 40) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = currentSyllable,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .clickable {
                            tts.value?.speak(currentSyllable, TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    viewModel.selectedSyllables.forEach { syllable ->
                        Button(onClick = {
                            if (syllable == viewModel.newSyllable) {
                                viewModel.increaseCorrectAnswers()
                            }
                            viewModel.setNewSyllable()
                        }) {
                            Text(syllable)
                        }
                    }
                }
            }
        } else {
            // Game finished
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Great job!", fontSize = 32.sp)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SyllableGroupItem(syllables: List<String>, onSyllableSelected: (SyllableBlock) -> Unit) {
    var selectedSyllables by remember { mutableStateOf(emptySet<String>()) }
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        FlowRow(modifier = Modifier.padding(8.dp)) {
            syllables.forEach { syllable ->
                SyllableItem(
                    syllable = syllable,
                    isSelected = syllable in selectedSyllables,
                    onToggle = { isSelected ->
                        if (isSelected) {
                            selectedSyllables = selectedSyllables + syllable
                        } else {
                            selectedSyllables = selectedSyllables - syllable
                        }
                        onSyllableSelected(SyllableBlock(syllable, isSelected))
                    }
                )
            }
        }
    }
}

data class SyllableBlock(val text: String, val isSelected: Boolean)

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

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    SyllableSelectionScreen(initialSelectedCount = 3)
//}