package com.olaz.instasprite.ui.screens.gallery.contract

sealed interface BottomBarEvent {
    data object ToggleSearchBar : BottomBarEvent
    data object OpenSelectSortOption : BottomBarEvent
}