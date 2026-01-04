package com.olaz.instasprite.ui.gallery.contract

import android.content.Context
import com.olaz.instasprite.data.model.ISpriteData

sealed interface SpriteListEvent {
    data class OpenDeleteDialog(val spriteName: String, val spriteId: String) : SpriteListEvent
    data class OpenRenameDialog(val spriteId: String) : SpriteListEvent
    data class OpenPager(val sprite: ISpriteData) : SpriteListEvent
    data class OpenDrawingActivity(val sprite: ISpriteData, val context: Context) : SpriteListEvent
}