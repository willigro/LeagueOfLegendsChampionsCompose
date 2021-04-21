package com.rittmann.leagueoflegendschamps.screens.comum

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.rittmann.leagueoflegendschamps.themes.PatternNormalPadding
import com.rittmann.leagueoflegendschamps.themes.SelectedItemColor
import com.rittmann.leagueoflegendschamps.themes.allSmallPaddings
import kotlinx.coroutines.launch

@Composable
fun DropDownList(
    requestToOpen: Boolean = false,
    list: List<String>,
    request: (Boolean) -> Unit,
    selectedString: (String) -> Unit
) {
    DropdownMenu(
        modifier = Modifier.fillMaxWidth(),
        expanded = requestToOpen,
        onDismissRequest = { request(false) },
    ) {
        list.forEach {
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    request(false)
                    selectedString(it)
                }
            ) {
                Text(it)
            }
        }
    }
}

@Composable
fun DropDownListHorizontal(
    requestToOpen: Boolean = false,
    list: List<String>,
    request: (Boolean) -> Unit,
    currentItem: String,
    selectedString: (String) -> Unit
) {
    if (requestToOpen) {
        val coroutineScope = rememberCoroutineScope()
        val state = rememberLazyListState()
        LazyRow(
            state = state,
            modifier = Modifier
                .padding(PatternNormalPadding)
        ) {
            items(list) { item ->
                Column {
                    Text(
                        modifier = Modifier
                            .allSmallPaddings()
                            .clickable {
                                request(false)
                                selectedString(item)
                            },
                        text = item,
                        style = MaterialTheme.typography.body1.copy(
                            color = if (item == currentItem) MaterialTheme.colors.secondary else MaterialTheme.colors.onSecondary
                        )
                    )
                }
            }

            coroutineScope.launch {
                state.scrollToItem(list.indexOf(currentItem))
            }
        }
    }
}