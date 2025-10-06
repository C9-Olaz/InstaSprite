package com.olaz.instasprite.ui.screens.postscreen.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.R
import com.olaz.instasprite.ui.screens.postscreen.Comment
import com.olaz.instasprite.ui.theme.CatppuccinUI
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CommentsHeader(
    commentsCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Comments ($commentsCount)",
            color = CatppuccinUI.TextColorLight,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CommentItem(
    comment: Comment,
    onProfileClick: (String) -> Unit,
    onLikeClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Profile Image
        Image(
            painter = painterResource(id = comment.profileImageRes),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .clickable { onProfileClick(comment.userId) },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Comment content
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.username,
                    color = CatppuccinUI.TextColorLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onProfileClick(comment.userId) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatTimeAgo(comment.createdAt),
                    color = CatppuccinUI.TextColorLight.copy(alpha = 0.5f),
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = comment.content,
                color = CatppuccinUI.TextColorLight,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )

            // Comment actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                TextButton(
                    onClick = { onLikeClick(comment.id) },
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(
                        text = if (comment.isLiked) "Liked" else "Like",
                        color = if (comment.isLiked) Color.Red else CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

                if (comment.likesCount > 0) {
                    Text(
                        text = "${comment.likesCount}",
                        color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

                if (comment.isOwnComment) {
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = { onDeleteClick(comment.id) },
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text(
                            text = "Delete",
                            color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommentInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        colors = CardDefaults.cardColors(
            containerColor = CatppuccinUI.BackgroundColor
        ),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "Your Profile",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = {
                    Text(
                        text = "Add a comment...",
                        color = CatppuccinUI.TextColorLight.copy(alpha = 0.5f)
                    )
                },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CatppuccinUI.TextColorLight,
                    unfocusedTextColor = CatppuccinUI.TextColorLight,
                    focusedBorderColor = CatppuccinUI.BottomBarColor,
                    unfocusedBorderColor = CatppuccinUI.TextColorLight.copy(alpha = 0.3f)
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSendClick() }),
                maxLines = 3
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onSendClick,
                enabled = text.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (text.isNotBlank()) CatppuccinUI.BottomBarColor else CatppuccinUI.TextColorLight.copy(alpha = 0.3f)
                )
            }
        }
    }
}

fun formatTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60_000 -> "now" // Less than 1 minute
        diff < 3600_000 -> "${diff / 60_000}m" // Less than 1 hour
        diff < 86400_000 -> "${diff / 3600_000}h" // Less than 1 day
        diff < 604800_000 -> "${diff / 86400_000}d" // Less than 1 week
        else -> {
            val formatter = SimpleDateFormat("MMM d", Locale.getDefault())
            formatter.format(Date(timestamp))
        }
    }
}