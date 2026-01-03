package com.olaz.instasprite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.olaz.instasprite.data.database.AppDatabase
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import com.olaz.instasprite.data.repository.SortSettingRepository
import com.olaz.instasprite.data.repository.StorageLocationRepository
import com.olaz.instasprite.ui.gallery.GalleryScreen
import com.olaz.instasprite.ui.gallery.GalleryViewModel
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

        val database = AppDatabase.getInstance(applicationContext)
        val spriteDataRepository =
            ISpriteDatabaseRepository(database.spriteDataDao(), database.spriteMetaDataDao())
        val sortSettingRepository = SortSettingRepository(applicationContext)
        val storageLocationRepository = StorageLocationRepository(applicationContext)

        val viewModel = GalleryViewModel(
            spriteDatabaseRepository = spriteDataRepository,
            sortSettingRepository = sortSettingRepository,
            storageLocationRepository = storageLocationRepository
        )

        setContent {
            InstaSpriteTheme {
                GalleryScreen(viewModel)
            }
        }
    }
}