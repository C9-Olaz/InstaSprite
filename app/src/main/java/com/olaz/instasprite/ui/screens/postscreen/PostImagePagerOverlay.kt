package com.olaz.instasprite.ui.screens.postscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun PostImagePagerOverlay(
    sprite: ISpriteData,
    onDismiss: () -> Unit,
    onSaveImage: (ISpriteData, String) -> Unit,
    isOwner: Boolean = false
) {
    var showSaveImageDialog by remember { mutableStateOf(false) }
    var isZoomed by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        // Remove dialog dim
        val view = LocalView.current
        LaunchedEffect(Unit) {
            val dialogWindowProvider = view.parent as? DialogWindowProvider
            dialogWindowProvider?.window?.setDimAmount(0f)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CatppuccinUI.BackgroundColorDarker)
        ) {
            // Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(CatppuccinUI.BackgroundColor)
                    .align(Alignment.TopStart)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = CatppuccinUI.TextColorLight,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = "Image Viewer",
                        color = CatppuccinUI.TextColorLight,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Save button for non-owners
                    if (!isOwner) {
                        IconButton(onClick = { showSaveImageDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Save Image",
                                tint = CatppuccinUI.TextColorLight,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(48.dp))
                    }
                }
            }

            // Image Content
            val bitmapImage = remember(sprite) {
                createBitmapFromSprite(sprite)?.asImageBitmap()
            }

            if (bitmapImage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 56.dp, bottom = 160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = bitmapImage,
                        contentDescription = "Sprite Image",
                        contentScale = ContentScale.Fit,
                        filterQuality = FilterQuality.None,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { isZoomed = true }
                            )
                    )
                }

                // Zoomed overlay
                if (isZoomed) {
                    ZoomedImageOverlay(
                        bitmap = bitmapImage,
                        onDismiss = { isZoomed = false }
                    )
                }
            } else {
                // Fallback when bitmap creation fails
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 56.dp, bottom = 160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = CatppuccinUI.TextColorLight.copy(alpha = 0.6f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Unable to display image",
                            color = CatppuccinUI.TextColorLight.copy(alpha = 0.8f),
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Bottom Info Panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(CatppuccinUI.BackgroundColor)
                    .align(Alignment.BottomStart)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Dimensions",
                                color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${sprite.width} × ${sprite.height} px",
                                color = CatppuccinUI.TextColorLight,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Colors",
                                color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${sprite.colorPalette?.size ?: 0}",
                                color = CatppuccinUI.TextColorLight,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Color Palette Preview
                    sprite.colorPalette?.let { palette ->
                        if (palette.isNotEmpty()) {
                            Text(
                                text = "Color Palette",
                                color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                palette.take(8).forEach { color ->
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(
                                                Color(color),
                                                RoundedCornerShape(4.dp)
                                            )
                                            .border(
                                                1.dp,
                                                CatppuccinUI.TextColorLight.copy(alpha = 0.3f),
                                                RoundedCornerShape(4.dp)
                                            )
                                    )
                                }
                                if (palette.size > 8) {
                                    Text(
                                        text = "+${palette.size - 8}",
                                        color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                                        fontSize = 12.sp,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Save Image Dialog
    if (showSaveImageDialog) {
        SaveImageDialog(
            spriteName = "sprite_${sprite.id}",
            onSave = { fileName ->
                onSaveImage(sprite, fileName)
                showSaveImageDialog = false
            },
            onDismiss = { showSaveImageDialog = false }
        )
    }
}

@Composable
private fun ZoomedImageOverlay(
    bitmap: androidx.compose.ui.graphics.ImageBitmap,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = "Zoomed Sprite",
            contentScale = ContentScale.Fit,
            filterQuality = FilterQuality.None,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = maxOf(1f, scale),
                    scaleY = maxOf(1f, scale),
                    translationX = offsetX,
                    translationY = offsetY
                )
        )

        // Zoom controls
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
                .background(
                    CatppuccinUI.BackgroundColor.copy(alpha = 0.8f),
                    RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = {
                    scale = maxOf(1f, scale - 0.5f)
                    if (scale == 1f) {
                        offsetX = 0f
                        offsetY = 0f
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Zoom Out",
                    tint = CatppuccinUI.TextColorLight
                )
            }

            Text(
                text = "${(scale * 100).toInt()}%",
                color = CatppuccinUI.TextColorLight,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            IconButton(
                onClick = { scale = minOf(5f, scale + 0.5f) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Zoom In",
                    tint = CatppuccinUI.TextColorLight
                )
            }
        }

        // Close button
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = CatppuccinUI.TextColorLight,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun SaveImageDialog(
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

// Helper function to create bitmap from sprite data
private fun createBitmapFromSprite(sprite: ISpriteData): android.graphics.Bitmap? {
    return try {
        val width = sprite.width
        val height = sprite.height
        val pixels = sprite.pixelsData.toIntArray()

        val bitmap = android.graphics.Bitmap.createBitmap(
            width,
            height,
            android.graphics.Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        bitmap
    } catch (e: Exception) {
        Log.e("PostImagePagerOverlay", "Error creating bitmap: ${e.message}", e)
        null
    }
}