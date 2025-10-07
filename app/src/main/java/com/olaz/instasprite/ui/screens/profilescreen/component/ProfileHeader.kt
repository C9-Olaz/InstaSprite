package com.olaz.instasprite.ui.screens.profilescreen.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.ui.theme.CatppuccinUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHeader(
    username: String,
    isOwnProfile: Boolean,
    onBackClick: () -> Unit,
    onShareProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = username,
                color = CatppuccinUI.TextColorLight,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Back",
                    tint = CatppuccinUI.TextColorLight
                )
            }
        },
        actions = {
            if (!isOwnProfile) {
                IconButton(onClick = onShareProfileClick) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Profile",
                        tint = CatppuccinUI.TextColorLight
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CatppuccinUI.BackgroundColorDarker
        ),
        modifier = modifier
    )
}