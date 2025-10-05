//package com.olaz.instasprite.ui.screens.postscreen.dialog
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.width
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Share
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun ShareDialog(
//    onDismiss: () -> Unit,
//    onShare: (String) -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Share Post") },
//        text = {
//            Column {
//                TextButton(
//                    onClick = { onShare("copy_link") },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(Icons.Default.Share, contentDescription = null)
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text("Copy Link")
//                    }
//                }
//                TextButton(
//                    onClick = { onShare("external_app") },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(Icons.Default.Share, contentDescription = null)
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text("Share to...")
//                    }
//                }
//            }
//        },
//        confirmButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        }
//    )
//}
