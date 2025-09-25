package com.olaz.instasprite

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.olaz.instasprite.data.database.AppDatabase
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import com.olaz.instasprite.data.repository.StorageLocationRepository
import com.olaz.instasprite.ui.screens.profilescreen.ProfileScreen
import com.olaz.instasprite.ui.screens.profilescreen.ProfileScreenViewModel
import com.olaz.instasprite.ui.theme.InstaSpriteTheme

class ProfileActivity : ComponentActivity() {

    private lateinit var profileViewModel: ProfileScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get userId from intent extras (null means own profile)
        val userId = intent.getStringExtra("USER_ID")

        // Initialize database and repository
        val database = AppDatabase.getInstance(this)
        val spriteDataRepository = ISpriteDatabaseRepository(
            database.spriteDataDao(),
            database.spriteMetaDataDao()
        )
        val storageLocationRepository = StorageLocationRepository(this)

        // Create ViewModel using ViewModelProvider.Factory
        profileViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileScreenViewModel(
                    spriteDatabaseRepository = spriteDataRepository,
                    userId = userId,
                    storageLocationRepository = storageLocationRepository
                ) as T
            }
        })[ProfileScreenViewModel::class.java]

        setContent {
            InstaSpriteTheme(darkTheme = false) {
                ProfileScreen(
                    viewModel = profileViewModel,
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