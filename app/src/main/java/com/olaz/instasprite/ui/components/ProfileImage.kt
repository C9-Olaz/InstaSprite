package com.olaz.instasprite.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import android.util.Log
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.olaz.instasprite.ui.screens.homescreen.HomeScreenViewModel
import com.olaz.instasprite.ui.screens.homescreen.ProfileImageState
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun ProfileImage(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 100.dp,
    key: Any? = null
) {
    val profileImageState by viewModel.profileImageState.collectAsState()
    val context = LocalContext.current

    // Debug logging
    Log.d("ProfileImage", "Composable created/updated - key: $key, state: isLoading=${profileImageState.isLoading}, imageUrl=${profileImageState.imageUrl}")

    // Trigger load when composable is created or when key changes
    LaunchedEffect(key) {
        Log.d("ProfileImage", "LaunchedEffect triggered with key: $key")
        viewModel.loadProfileImage()
    }

    when {
        profileImageState.isLoading -> {
            CircularProgressIndicator(
                modifier = modifier.size(size),
                color = CatppuccinUI.TextColorLight
            )
        }
        profileImageState.imageUrl != null -> {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(profileImageState.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Image",
                modifier = modifier
                    .size(size)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        else -> {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Default Profile",
                modifier = modifier.size(size),
                tint = CatppuccinUI.TextColorLight
            )
        }
    }
}
