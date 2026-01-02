package com.olaz.instasprite.domain.dialog

import kotlinx.coroutines.flow.StateFlow

interface DialogController<Dialog> {
    val dialogState: StateFlow<List<Dialog>>
    fun openDialog(dialog: Dialog)
    fun closeTopDialog()
    fun closeAllDialogs()
}