package com.padym.rusread.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.ManualListViewModel

@Composable
fun ManualListScreen(navController: NavHostController) {
    val viewModel: ManualListViewModel = hiltViewModel()
    val chosenSyllables = listOf("ба", "бо", "бу", "бя", "ша", "фу", "цу", "бы", "би", "бе").toSet()

    Scaffold(
        topBar = {
            SimpleCloseTopAppBar() { navController.popBackStack() }
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            SelectionSyllablesRow(chosenSyllables) {}
            EmojiRoundButton(text = "👍") {
                navController.popBackStack()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManualListScreenPreview() {
    val chosenSyllables = listOf("ба", "бо", "бу", "бя", "ша", "фу", "цу", "бы", "би", "бе").toSet()

    RusreadTheme {
        Scaffold(
            topBar = {
                SimpleCloseTopAppBar() {  }
            }
        ) { paddingValues ->
            Column(Modifier.padding(paddingValues)) {
                SelectionSyllablesRow(chosenSyllables) {}
                EmojiRoundButton(text = "👍") {}
            }
        }
    }
}