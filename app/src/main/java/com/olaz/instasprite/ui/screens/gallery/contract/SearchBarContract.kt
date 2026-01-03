package com.olaz.instasprite.ui.screens.gallery.contract

sealed interface SearchBarContract {
    data object ToggleSearchBar : SearchBarContract
    data class UpdateSearchQuery(val query: String) : SearchBarContract
}
