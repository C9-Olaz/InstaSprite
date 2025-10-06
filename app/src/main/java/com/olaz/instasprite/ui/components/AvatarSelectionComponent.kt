package com.olaz.instasprite.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun AvatarSelectionComponent(
    selectedImageUri: Uri?,
    onImageSelected: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showImagePicker by remember { mutableStateOf(false) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri)
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Image was captured successfully
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile Picture",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = CatppuccinUI.TextColorLight,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Avatar display
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(
                    width = 3.dp,
                    color = CatppuccinUI.BottomBarColor,
                    shape = CircleShape
                )
                .background(CatppuccinUI.BackgroundColorDarker)
                .clickable { showImagePicker = true },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(selectedImageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Selected Avatar",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Avatar",
                    modifier = Modifier.size(48.dp),
                    tint = CatppuccinUI.TextColorLight.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Gallery button
            OutlinedButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CatppuccinUI.TextColorLight
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    CatppuccinUI.BottomBarColor
                )
            ) {
                Icon(
                    imageVector = Icons.Default.UploadFile,
                    contentDescription = "Gallery",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Gallery")
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Camera button
            OutlinedButton(
                onClick = { 
                    // For now, just open gallery. Camera implementation would need more setup
                    imagePickerLauncher.launch("image/*")
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CatppuccinUI.TextColorLight
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    CatppuccinUI.BottomBarColor
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Camera")
            }
        }

        // Remove image button (only show if image is selected)
        if (selectedImageUri != null) {
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                onClick = { onImageSelected(null) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = CatppuccinUI.TextColorLight.copy(alpha = 0.7f)
                )
            ) {
                Text("Remove Image")
            }
        }
    }
}
