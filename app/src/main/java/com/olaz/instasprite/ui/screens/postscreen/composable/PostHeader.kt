package com.olaz.instasprite.ui.screens.postscreen.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.R
import com.olaz.instasprite.data.model.ISpriteWithMetaData
import com.olaz.instasprite.ui.screens.postscreen.PostAuthor
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun PostHeader(
    author: PostAuthor?,
    post: ISpriteWithMetaData,
    isOwnPost: Boolean,
    onProfileClick: (String) -> Unit,
    onFollowClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Image
        Image(
            painter = painterResource(id = author?.profileImageRes ?: R.drawable.ic_launcher),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable { author?.let { onProfileClick(it.id) } },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Username and timestamp
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                    text = formatTimeAgo(post.meta?.createdAt ?: 0L),
                    color = CatppuccinUI.TextColorLight.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
            Text(
                text = post.meta?.spriteName ?: "Untitled",
                color = CatppuccinUI.TextColorLight.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }

        // Follow/Edit button
        if (true) {
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = CatppuccinUI.TextColorLight,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            Button(
                onClick = onFollowClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (author?.isFollowing == true)
                        CatppuccinUI.BackgroundColor
                    else CatppuccinUI.BottomBarColor
                ),
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (author?.isFollowing == true) "Following" else "Follow",
                    color = CatppuccinUI.TextColorLight,
                    fontSize = 12.sp
                )
            }
        }
    }
}
