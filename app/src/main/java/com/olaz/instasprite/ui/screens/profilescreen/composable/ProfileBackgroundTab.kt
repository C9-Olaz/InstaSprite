package com.olaz.instasprite.ui.screens.profilescreen.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.screens.profilescreen.UserProfile
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun ProfileBackgroundTab(
    userProfile: UserProfile,
    onEditProfileClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {
                if (userProfile.isOwnProfile) {
                    onEditProfileClick()
                }
            }
    ) {
        when {
            // Custom image background
            userProfile.backgroundImageRes != null -> {
                Image(
                    painter = painterResource(id = userProfile.backgroundImageRes),
                    contentDescription = "Profile Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Overlay for better text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    CatppuccinUI.BackgroundColorDarker.copy(alpha = 0.3f)
                                )
                            )
                        )
                )
            }

            // Custom gradient background
            userProfile.backgroundGradient != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = userProfile.backgroundGradient
                            )
                        )
                )
            }

            // Default background with subtle gradient
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    CatppuccinUI.CanvasChecker1Color.copy(alpha = 0.3f),
                                    CatppuccinUI.CanvasChecker2Color.copy(alpha = 0.8f)
                                )
                            )
                        )
                )
            }
        }

        // Add background edit hint for own profile
        if (userProfile.isOwnProfile && userProfile.backgroundImageUri == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Background",
                    tint = CatppuccinUI.TextColorLight.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}