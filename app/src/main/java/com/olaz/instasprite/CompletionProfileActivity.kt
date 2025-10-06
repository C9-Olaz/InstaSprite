package com.olaz.instasprite

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.olaz.instasprite.ui.screens.completionprofilescreen.ProfileCompletionScreen
import com.olaz.instasprite.ui.screens.completionprofilescreen.ProfileCompletionViewModel
import com.olaz.instasprite.ui.theme.InstaSpriteTheme

class ProfileCompletionViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileCompletionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileCompletionViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CompletionProfileActivity : ComponentActivity() {
    
    private val viewModel: ProfileCompletionViewModel by viewModels {
        ProfileCompletionViewModelFactory(this)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            InstaSpriteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileCompletionScreen(
                        onProfileCompleted = {
                            finish()
                        },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}