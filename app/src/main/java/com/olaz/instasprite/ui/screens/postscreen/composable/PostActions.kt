package com.olaz.instasprite.ui.screens.postscreen.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun PostActions(
    isLiked: Boolean,
    isBookmarked: Boolean,
    likesCount: Int,
    commentsCount: Int,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                IconButton(onClick = onLikeClick) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else CatppuccinUI.TextColorLight
                    )
                }
                IconButton(onClick = onCommentClick) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Comment",
                        tint = CatppuccinUI.TextColorLight
                    )
                }
                IconButton(onClick = onShareClick) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = CatppuccinUI.TextColorLight
                    )
                }
            }

            IconButton(onClick = onBookmarkClick) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = "Bookmark",
                    tint = CatppuccinUI.TextColorLight
                )
            }
        }

        // Likes and comments count
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (likesCount > 0) {
                Text(
                    text = "$likesCount ${if (likesCount == 1) "like" else "likes"}",
                    color = CatppuccinUI.TextColorLight,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (likesCount > 0 && commentsCount > 0) {
                Spacer(modifier = Modifier.width(16.dp))
            }
            if (commentsCount > 0) {
                Text(
                    text = "$commentsCount ${if (commentsCount == 1) "comment" else "comments"}",
                    color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }
    }
}