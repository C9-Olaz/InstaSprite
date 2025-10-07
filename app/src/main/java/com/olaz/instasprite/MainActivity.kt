package com.olaz.instasprite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.olaz.instasprite.data.database.AppDatabase
import com.olaz.instasprite.utils.AuthStateUtils
import com.olaz.instasprite.data.network.NetworkModule
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import com.olaz.instasprite.data.repository.SortSettingRepository
import com.olaz.instasprite.data.repository.StorageLocationRepository
import com.olaz.instasprite.data.repository.ProfileRepository
import com.olaz.instasprite.ui.screens.homescreen.HomeScreen
import com.olaz.instasprite.ui.screens.homescreen.HomeScreenViewModel
import com.olaz.instasprite.ui.screens.googleauthscreen.GoogleAuthViewModel
import com.olaz.instasprite.ui.theme.InstaSpriteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val display = window.windowManager.defaultDisplay
        val modes = display.supportedModes
        val highest = modes.maxByOrNull { it.refreshRate }

        highest?.let {
            val params = window.attributes
            params.preferredDisplayModeId = it.modeId
            window.attributes = params
        }


        NetworkModule.initialize(applicationContext)
        
        val database = AppDatabase.getInstance(applicationContext)
        val spriteDataRepository =
            ISpriteDatabaseRepository(database.spriteDataDao(), database.spriteMetaDataDao())
        val sortSettingRepository = SortSettingRepository(applicationContext)
        val storageLocationRepository = StorageLocationRepository(applicationContext)
        val profileRepository = ProfileRepository()
        val authStateUtils = AuthStateUtils(applicationContext)

        val homeViewModel = HomeScreenViewModel(
            spriteDatabaseRepository = spriteDataRepository,
            sortSettingRepository = sortSettingRepository,
            storageLocationRepository = storageLocationRepository,
            profileRepository = profileRepository
        )

        val authViewModel = GoogleAuthViewModel(
            tokenUtils = authStateUtils.getTokenManager()
        )

        setContent {
            InstaSpriteTheme {
                MainApp(
                    homeViewModel = homeViewModel,
                    authViewModel = authViewModel,
                    authStateUtils = authStateUtils
                )
            }
        }
    }
}

@Composable
fun MainApp(
    homeViewModel: HomeScreenViewModel,
    authViewModel: GoogleAuthViewModel,
    authStateUtils: AuthStateUtils
) {
    val isLoggedIn by authStateUtils.isLoggedIn.collectAsState()

    BackHandler(enabled = isLoggedIn) {
    }

    HomeScreen(homeViewModel)
}