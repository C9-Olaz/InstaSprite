package com.olaz.instasprite.ui.gallery.contract

import android.content.Context
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.model.ISpriteWithMetaData

sealed interface ImagePagerEvent {
    data class OpenDeleteDialog(val spriteName: String, val spriteId: String) : ImagePagerEvent
    data class OpenSaveImageDialog(val sprite: ISpriteWithMetaData) : ImagePagerEvent
    data class OpenDrawingActivity(val sprite: ISpriteData, val context: Context) : ImagePagerEvent
}


