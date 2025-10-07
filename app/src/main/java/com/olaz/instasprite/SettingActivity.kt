package com.olaz.instasprite

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.olaz.instasprite.ui.screens.settingscreen.SettingScreen
import com.olaz.instasprite.ui.theme.InstaSpriteTheme

class SettingActivity: ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstaSpriteTheme(darkTheme = false) {
                SettingScreen(
                    onBackClick = { finish() }
                )
            }
        }
    }

    // Static method to start this activity
    companion object {
        fun startActivity(context: android.content.Context) {
            val intent = Intent(context, SettingActivity::class.java).apply {
            }
            context.startActivity(intent)
        }
    }
}