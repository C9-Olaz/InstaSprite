package com.olaz.instasprite.ui.drawing.contract

import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.domain.tool.Tool

data class PixelCanvasState(
    val width: Int = 0,
    val height: Int = 0,
    val pixels: List<Color> = emptyList(),
    val selectedTool: Tool? = null
)

sealed interface PixelCanvasEvent {
    data object OnCanvasTouchStart : PixelCanvasEvent
    data class DrawAt(val x: Int, val y: Int) : PixelCanvasEvent
}