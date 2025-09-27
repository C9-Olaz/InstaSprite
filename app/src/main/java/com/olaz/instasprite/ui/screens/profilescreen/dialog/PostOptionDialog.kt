package com.olaz.instasprite.ui.screens.profilescreen.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun PostOptionsDialog(
    postId: String,
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
                    TextButton(
                        onClick = {
                            onDelete(postId)
                            onDismiss()
                        }
                    ) {
                        Text("Delete Post")
                    }
                }
                TextButton(
                    onClick = {
                        onShare(postId)
                        onDismiss()
                    }
                ) {
                    Text("Share Post")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}