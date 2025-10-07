package com.olaz.instasprite.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.olaz.instasprite.R
import com.olaz.instasprite.ui.theme.CatppuccinUI

data class Post(
    val imageRes: Int,
    val likeCount: Int,
    val commentCount: Int,
    val username: String
)

class PostWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            PostWidgetContent()
        }
    }

    @SuppressLint("RestrictedApi")
    @Composable
    private fun PostWidgetContent() {
        // Dummy post data
        val mostLikedPost = Post(
            imageRes = R.drawable.ic_launcher, // Replace with your actual drawable
            likeCount = 12845,
            commentCount = 342,
            username = "photography_pro"
        )

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(CatppuccinUI.BackgroundColor)
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
            ) {
                // Header
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        text = "Most Liked",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(CatppuccinUI.TextColorLight)
                        )
                    )

                    Spacer(modifier = GlanceModifier.height(12.dp))
                }

                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .defaultWeight(),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    // Post Image
                    Image(
                        provider = ImageProvider(mostLikedPost.imageRes),
                        contentDescription = "Most liked post",
                        modifier = GlanceModifier
                            .fillMaxSize()
                    )

                    Box(
                        modifier = GlanceModifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd,
                    ) {
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalAlignment = Alignment.End
                        ) {
                            // Like Count
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Image(
                                    provider = ImageProvider(R.drawable.ic_heart),
                                    contentDescription = "Likes",
                                    modifier = GlanceModifier.size(20.dp)
                                )
                                Spacer(modifier = GlanceModifier.width(4.dp))
                                Text(
                                    text = formatCount(mostLikedPost.likeCount),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorProvider(CatppuccinUI.CurrentPalette.Red)
                                    )
                                )
                            }

                        Spacer(modifier = GlanceModifier.width(30.dp))

                            // Comment Count
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Image(
                                    provider = ImageProvider(R.drawable.ic_comment),
                                    contentDescription = "Comments",
                                    modifier = GlanceModifier.size(20.dp)
                                )
                                Spacer(modifier = GlanceModifier.width(4.dp))
                                Text(
                                    text = formatCount(mostLikedPost.commentCount),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorProvider(CatppuccinUI.CurrentPalette.Blue)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun formatCount(count: Int): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
            count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
            else -> count.toString()
        }
    }
}