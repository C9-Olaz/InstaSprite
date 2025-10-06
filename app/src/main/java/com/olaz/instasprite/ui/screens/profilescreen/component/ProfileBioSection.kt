package com.olaz.instasprite.ui.screens.profilescreen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun ProfileBioSection(
    displayName: String,
    bio: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = displayName,
            color = CatppuccinUI.TextColorLight,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        if (bio.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = bio,
                color = CatppuccinUI.TextColorLight.copy(alpha = 0.8f),
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
        }
    }
}