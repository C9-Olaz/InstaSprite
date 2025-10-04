package com.olaz.instasprite

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.olaz.instasprite.ui.screens.postscreen.PostScreen
import com.olaz.instasprite.ui.theme.InstaSpriteTheme

class PostActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get postId from intent extras
        val postId = intent.getStringExtra("POST_ID")

        setContent {
            InstaSpriteTheme(darkTheme = false) {
                PostScreen(
                    postId = postId,
                    onBackClick = { finish() }
                )
            }
        }
    }

    // Static method to start this activity
    companion object {
        fun startActivity(context: android.content.Context, postId: String? = null) {
            val intent = Intent(context, PostActivity::class.java).apply {
                if (postId != null) {
                    putExtra("POST_ID", postId)
                }
            }
            context.startActivity(intent)
        }
    }
}