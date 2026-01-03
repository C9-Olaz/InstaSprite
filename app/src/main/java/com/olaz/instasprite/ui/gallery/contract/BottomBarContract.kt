package com.olaz.instasprite.ui.gallery.contract

sealed interface BottomBarEvent {
    data object ToggleSearchBar : BottomBarEvent
    data object OpenSelectSortOption : BottomBarEvent
}