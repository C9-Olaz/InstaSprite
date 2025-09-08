package com.olaz.instasprite

import LoginRegisterScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.olaz.instasprite.ui.theme.InstaSpriteTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstaSpriteTheme {
                LoginRegisterScreen(
                    onLoginClick = { email, password ->
                        // TODO: Handle login
                        // Example: authViewModel.login(email, password)
                    },
                    onRegisterClick = { username, email, password, confirmPassword ->
                        // TODO: Handle register
                        // Example: authViewModel.register(username, email, password)
                    },
                    onForgotPasswordClick = {
                        // TODO: Handle forgot password action
                    },
                    onBackClick = {
                        // Optional: handle back navigation
                        finish()
                    }
                )
            }
        }
    }
}
