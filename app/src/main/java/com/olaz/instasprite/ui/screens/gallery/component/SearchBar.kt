package com.olaz.instasprite.ui.screens.gallery.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.screens.gallery.contract.SearchBarContract
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun SearchBar(
    onSearchBarEvent: (SearchBarContract) -> Unit,
    searchQuery: String,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var isFocused by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler(enabled = isFocused) {
        if (searchQuery.isEmpty()) {
            onSearchBarEvent(SearchBarContract.ToggleSearchBar)
        }
        focusManager.clearFocus()
    }

    TextField(
        value = searchQuery,
        onValueChange = {
            onSearchBarEvent(
                SearchBarContract.UpdateSearchQuery(
                    it
                )
            )
        },
        placeholder = { Text(text = "Search sprites", color = CatppuccinUI.Subtext0Color) },
        singleLine = true,
        trailingIcon = {
            IconButton(
                onClick = {
                    if (searchQuery.isEmpty()) {
                        onSearchBarEvent(SearchBarContract.ToggleSearchBar)
                    }
                    onSearchBarEvent(SearchBarContract.UpdateSearchQuery(""))
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = CatppuccinUI.DismissButtonColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CatppuccinUI.BackgroundColor,
            disabledContainerColor = CatppuccinUI.BackgroundColor,
            unfocusedContainerColor = CatppuccinUI.BackgroundColor,
            focusedTextColor = CatppuccinUI.TextColorLight,
            unfocusedTextColor = CatppuccinUI.TextColorLight,
            cursorColor = CatppuccinUI.TextColorLight,
            focusedBorderColor = CatppuccinUI.CurrentPalette.Peach,
            unfocusedBorderColor = CatppuccinUI.CurrentPalette.Peach,
            unfocusedPlaceholderColor = CatppuccinUI.CurrentPalette.Peach,
            focusedPlaceholderColor = CatppuccinUI.Subtext0Color
        ),
    )

}