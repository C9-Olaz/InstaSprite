package com.olaz.instasprite.ui.screens.profilescreen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.R
import com.olaz.instasprite.ui.screens.profilescreen.UserProfile
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun ProfileInfoSection(
    userProfile: UserProfile,
    onEditProfileClick: () -> Unit,
    onFollowClick: () -> Unit,
    onBackgroundTabClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Background section
        ProfileBackgroundTab(
            userProfile = userProfile,
            onEditProfileClick = onEditProfileClick
        )

        // Profile Image overlapping background
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomStart)
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .offset(y = 75.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CatppuccinUI.BackgroundColorDarker, CircleShape)
                        .padding(4.dp)
                ) {
                    Image(
                        painter = painterResource(
                            id = userProfile.profileImageRes ?: R.drawable.ic_launcher
                        ),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }
    }
}