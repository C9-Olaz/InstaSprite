package com.olaz.instasprite.ui.screens.postscreen.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun SaveImageDialog(
    spriteName: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var fileName by remember { mutableStateOf(spriteName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Save Image",
                color = CatppuccinUI.TextColorLight
            )
        },
        text = {
            Column {
                Text(
                    "Enter filename for the image:",
                    color = CatppuccinUI.TextColorLight
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("Filename") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CatppuccinUI.TextColorLight,
                        unfocusedTextColor = CatppuccinUI.TextColorLight,
                        focusedBorderColor = CatppuccinUI.BottomBarColor,
                        unfocusedBorderColor = CatppuccinUI.TextColorLight.copy(alpha = 0.3f),
                        focusedLabelColor = CatppuccinUI.BottomBarColor,
                        unfocusedLabelColor = CatppuccinUI.TextColorLight.copy(alpha = 0.7f)
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (fileName.isNotBlank()) {
                        onSave(fileName.trim())
                    }
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = CatppuccinUI.BottomBarColor
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = CatppuccinUI.TextColorLight.copy(alpha = 0.7f)
                )
            ) {
                Text("Cancel")
            }
        },
        containerColor = CatppuccinUI.BackgroundColor,
        textContentColor = CatppuccinUI.TextColorLight
    )
}
