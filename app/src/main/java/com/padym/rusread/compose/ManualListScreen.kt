package com.padym.rusread.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.padym.rusread.ui.theme.AppColors
import com.padym.rusread.ui.theme.RusreadTheme
import com.padym.rusread.viewmodels.ManualListViewModel
import com.padym.rusread.viewmodels.Position

@Composable
fun ManualListScreen(navController: NavHostController) {
    val viewModel: ManualListViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            SimpleCloseTopAppBar() { navController.popBackStack() }
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            SyllableCreator(
                firstLetterList = viewModel.firstLetterList,
                secondLetterList = viewModel.secondLetterOptions,
                onFirstLetterSelected = { _, item ->
                    viewModel.processChosenLetter(Position.FIRST, item)
                },
                onSecondLetterSelected = { _, item ->
                    viewModel.processChosenLetter(Position.SECOND, item)
                },
                onSaveSyllable = viewModel::saveSyllable
            )
            SelectionSyllablesRow(viewModel.chosenSyllables) {}
            EmojiRoundButton(text = "👍") {
                viewModel.saveSyllableList()
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun SyllableCreator(
    firstLetterList: List<String>,
    secondLetterList: List<String>,
    onFirstLetterSelected: (index: Int, item: String) -> Unit = { _, _ -> },
    onSecondLetterSelected: (index: Int, item: String) -> Unit = { _, _ -> },
    onSaveSyllable: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 16.dp)
            .background(AppColors.Almond),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    )
    {
        StyledScrollPicker(firstLetterList, onFirstLetterSelected)
        StyledScrollPicker(secondLetterList, onSecondLetterSelected)
        Box(modifier = Modifier.padding(horizontal = 16.dp))
        EmojiIconButton(text = "👌", onButtonClick = onSaveSyllable)
    }
}

@Composable
fun StyledScrollPicker(
    items: List<String>,
    onItemSelected: (index: Int, item: String) -> Unit = { _, _ -> }
) {
    ScrollPicker(
        width = 48.dp,
        itemHeight = 48.dp,
        items = items,
        textStyle = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold),
        textColor = AppColors.Linen,
        selectedTextColor = MaterialTheme.colorScheme.onBackground,
        onItemSelected = onItemSelected
    )
}

@Composable
fun ScrollPicker(
    width: Dp,
    itemHeight: Dp,
    numberOfDisplayedItems: Int = 3,
    items: List<String>,
    textStyle: TextStyle,
    textColor: Color,
    selectedTextColor: Color,
    onItemSelected: (index: Int, item: String) -> Unit = { _, _ -> }
) {
    val pickerItems = listOf("") + items + listOf("")
    val itemHalfHeight = LocalDensity.current.run { itemHeight.toPx() / 2f }
    val scrollState = rememberLazyListState(0)
    var lastSelectedIndex by remember { mutableIntStateOf(0) }
    LazyColumn(
        modifier = Modifier
            .width(width)
            .height(itemHeight * numberOfDisplayedItems),
        state = scrollState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = scrollState
        )
    ) {
        items(
            count = pickerItems.size,
            itemContent = { i ->
                val item = pickerItems[i]
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            val y = coordinates.positionInParent().y - itemHalfHeight
                            val parentHalfHeight =
                                (coordinates.parentCoordinates?.size?.height ?: 0) / 2f
                            val isSelected =
                                (y > parentHalfHeight - itemHalfHeight && y < parentHalfHeight + itemHalfHeight)
                            if (isSelected && lastSelectedIndex != i) {
                                onItemSelected(i, item)
                                lastSelectedIndex = i
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        style = textStyle,
                        color = if (lastSelectedIndex == i) selectedTextColor else textColor,
                        fontSize = textStyle.fontSize
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ManualListScreenPreview() {
    val chosenSyllables = setOf("ба", "бо", "бу", "бя", "ша", "фу", "бе")
    val firstLetterList = listOf("ж", "в", "г", "д", "з", "к", "л", "м", "н", "п", "р")
    val secondLetterList = listOf("а", "ы", "е", "и", "о", "у", "э", "ю", "я")

    RusreadTheme {
        Scaffold(
            topBar = {
                SimpleCloseTopAppBar { }
            }
        ) { paddingValues ->
            Column(Modifier.padding(paddingValues)) {
                SyllableCreator(firstLetterList, secondLetterList)
                SelectionSyllablesRow(chosenSyllables) {}
                EmojiRoundButton(text = "👍") {}
            }
        }
    }
}