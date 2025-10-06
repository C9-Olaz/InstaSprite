package com.olaz.instasprite.ui.screens.profilescreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun ProfileStatsSection(
    postsCount: Int,
    followersCount: Int,
    followingCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(
            count = postsCount,
            label = "Posts"
        )
        StatItem(
            count = followersCount,
            label = "Followers"
        )
        StatItem(
            count = followingCount,
            label = "Following"
        )
    }
}

@Composable
private fun StatItem(
    count: Int,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatCount(count),
            color = CatppuccinUI.TextColorLight,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

// Helper function to format counts
private fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> "${(count / 1_000_000)}M"
        count >= 1_000 -> "${(count / 1_000)}K"
        else -> count.toString()
    }
}