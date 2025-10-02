package com.olaz.instasprite

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.olaz.instasprite.ui.screens.notificationscreen.NotificationScreen
import com.olaz.instasprite.ui.theme.InstaSpriteTheme

class NotificationActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstaSpriteTheme(darkTheme = false) {
                NotificationScreen(
                    onBackClick = { finish() }
                )
            }
        }
    }

    // Static method to start this activity
    companion object {
        fun startActivity(context: android.content.Context) {
            val intent = Intent(context, NotificationActivity::class.java).apply {
            }
            context.startActivity(intent)
        }
    }
}
