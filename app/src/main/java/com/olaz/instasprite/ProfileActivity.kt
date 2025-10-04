package com.olaz.instasprite

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.olaz.instasprite.ui.screens.profilescreen.ProfileScreen
import com.olaz.instasprite.ui.theme.InstaSpriteTheme

class ProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get userId from intent extras (null means own profile)
        val userId = intent.getStringExtra("USER_ID")

        setContent {
            InstaSpriteTheme(darkTheme = false) {
                ProfileScreen(
                    userId = userId,
                    onBackClick = { finish() }
                )
            }
        }
    }

    // Static method to start this activity
    companion object {
        fun startActivity(context: android.content.Context, userId: String? = null) {
            val intent = Intent(context, ProfileActivity::class.java).apply {
                if (userId != null) {
                    putExtra("USER_ID", userId)
                }
            }
            context.startActivity(intent)
        }
    }
}