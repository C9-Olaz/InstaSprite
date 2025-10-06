package com.olaz.instasprite

import GoogleAuthScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.olaz.instasprite.ui.screens.googleauthscreen.GoogleAuthViewModel
import com.olaz.instasprite.utils.TokenUtils
import com.olaz.instasprite.ui.theme.InstaSpriteTheme


class AuthActivity : ComponentActivity() {

    private val viewModel: GoogleAuthViewModel by lazy { 
        GoogleAuthViewModel(
            tokenUtils = TokenUtils(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstaSpriteTheme {
                GoogleAuthScreen(
                    modifier = Modifier.padding(), 
                    viewModel = viewModel,
                    onLoginSuccess = {
                        finish()
                    }
                )
            }
        }
    }
}
