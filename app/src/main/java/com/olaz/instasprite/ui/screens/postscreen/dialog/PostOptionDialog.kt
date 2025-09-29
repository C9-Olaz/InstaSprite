package com.olaz.instasprite.ui.screens.postscreen.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun PostOptionDialog(
    isOwnPost: Boolean,
    onDismiss: () -> Unit,
    onDelete: (String) -> Unit,
    onShare: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Post Options") },
        text = {
            Column {
                if (isOwnPost) {
                        Text("Delete Post")
                }

                Text("Curren User")

            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}