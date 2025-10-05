package com.olaz.instasprite.ui.screens.postscreen.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.data.model.ISpriteWithMetaData
import com.olaz.instasprite.ui.screens.postscreen.PostAuthor
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun PostDescription(
    author: PostAuthor?,
    post: ISpriteWithMetaData,
    onProfileClick: (String) -> Unit
) {
    // In this case, we don't have a description field, so we'll show creation info
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row {
            Text(
                text = author?.username ?: "Unknown",
                color = CatppuccinUI.TextColorLight,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    author?.let { onProfileClick(it.id) }
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "shared a ${post.sprite.width}x${post.sprite.height} pixel art",
                color = CatppuccinUI.TextColorLight,
                fontSize = 14.sp
            )
        }
    }
}