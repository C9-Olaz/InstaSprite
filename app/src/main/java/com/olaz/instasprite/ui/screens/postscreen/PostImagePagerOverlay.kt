package com.olaz.instasprite.ui.screens.postscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.ui.screens.postscreen.component.ZoomedImageOverlay
import com.olaz.instasprite.ui.screens.postscreen.dialog.SaveImageDialog
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