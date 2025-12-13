package com.olaz.instasprite.ui.screens.drawingscreen.component

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import com.olaz.instasprite.domain.draw.DrawUtils
import com.olaz.instasprite.domain.tool.EraserTool
import com.olaz.instasprite.domain.tool.FillTool
import com.olaz.instasprite.domain.tool.PencilTool
import com.olaz.instasprite.domain.tool.Tool
import com.olaz.instasprite.ui.screens.drawingscreen.contract.PixelCanvasEvent
import com.olaz.instasprite.ui.screens.drawingscreen.contract.PixelCanvasState
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun PixelCanvas(
    modifier: Modifier = Modifier,
    pixelCanvasState: PixelCanvasState,
    selectedTool: Tool?,
    onEvent: (PixelCanvasEvent) -> Unit
) {

    val (canvasWidth, canvasHeight, pixels) = pixelCanvasState

    val bitmap = remember(canvasWidth, canvasHeight) {
        createBitmap(canvasWidth, canvasHeight)
    }

    val imageBitmap: ImageBitmap = remember(bitmap, pixels) {
        if (canvasWidth > 0 && canvasHeight > 0 && pixels.isNotEmpty()) {
            updateBitmapPixels(
                bitmap = bitmap,
                pixels = pixels,
                checkerColor1 = CatppuccinUI.CanvasChecker1Color.toArgb(),
                checkerColor2 = CatppuccinUI.CanvasChecker2Color.toArgb()
            )
        }
        bitmap.asImageBitmap()
    }

    // Store the stroke and color for the grid overlay once, maybe optimize memory
    val gridStroke = remember { Stroke(width = 1f) }
    val gridColor = remember { Color.LightGray.copy(alpha = 0.2f) }

    val aspectRatio = canvasWidth.toFloat() / canvasHeight.toFloat()
    val borderSize = 5.dp

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .border(borderSize, CatppuccinUI.BackgroundColor)
                .padding(borderSize)
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(aspectRatio)
                    .fillMaxWidth(0.9f)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawingPointerInput(canvasWidth, canvasHeight, selectedTool, onEvent)
                ) {
                    drawImage(
                        image = imageBitmap,
                        dstOffset = IntOffset.Zero,
                        dstSize = IntSize(size.width.toInt(), size.height.toInt()),
                        filterQuality = FilterQuality.None
                    )

//                    if (canvasWidth < 32 && canvasHeight < 32) {
//                        drawGridOverlay(canvasWidth, canvasHeight, gridColor, gridStroke)
//                    }
                }
            }
        }
    }
}

private fun updateBitmapPixels(
    bitmap: Bitmap,
    pixels: List<Color>,
    checkerColor1: Int,
    checkerColor2: Int
) {
    val width = bitmap.width
    val height = bitmap.height
    val pixelArray = IntArray(width * height)

    val useLargeCheckers = width >= 32 || height >= 32
    val blockSize = if (useLargeCheckers) 16 else 1

    for (row in 0 until height) {
        for (col in 0 until width) {
            val index = row * width + col

            val pixelColor = if (index < pixels.size) pixels[index] else Color.Transparent

            pixelArray[index] = if (pixelColor == Color.Transparent) {
                val checkerRow = row / blockSize
                val checkerCol = col / blockSize
                if ((checkerRow + checkerCol) % 2 == 0) checkerColor1 else checkerColor2
            } else {
                pixelColor.toArgb()
            }
        }
    }

    bitmap.setPixels(pixelArray, 0, width, 0, 0, width, height)
}


private fun DrawScope.drawGridOverlay(
    canvasWidth: Int,
    canvasHeight: Int,
    gridColor: Color,
    gridStroke: Stroke
) {
    val cellWidth = size.width / canvasWidth
    val cellHeight = size.height / canvasHeight
    val cellSize = Size(cellWidth, cellHeight)

    for (row in 0 until canvasHeight) {
        val y = row * cellHeight
        for (col in 0 until canvasWidth) {
            val x = col * cellWidth
            val topLeft = Offset(x, y)

            drawRect(
                color = gridColor,
                topLeft = topLeft,
                size = cellSize,
                style = gridStroke
            )
        }
    }
}

fun Offset.toGridCell(canvasWidth: Int, canvasHeight: Int, cols: Int, rows: Int): IntOffset {
    val cellWidth = canvasWidth.toFloat() / cols.toFloat()
    val cellHeight = canvasHeight.toFloat() / rows.toFloat()

    val gridX = (x / cellWidth).toInt().coerceIn(0, cols - 1)
    val gridY = (y / cellHeight).toInt().coerceIn(0, rows - 1)
    return IntOffset(gridX, gridY)
}

@Composable
fun Modifier.drawingPointerInput(
    canvasWidth: Int,
    canvasHeight: Int,
    selectedTool: com.olaz.instasprite.domain.tool.Tool?,
    onEvent: (PixelCanvasEvent) -> Unit
): Modifier {
    if (canvasWidth == 0 || canvasHeight == 0) return this

    return this.pointerInput(canvasWidth, canvasHeight, selectedTool) {
        awaitEachGesture {
            if (selectedTool in listOf(PencilTool, EraserTool, FillTool)) {
                onEvent(PixelCanvasEvent.OnCanvasTouchStart)
            }

            val down = awaitFirstDown(requireUnconsumed = false)
            val event = awaitPointerEvent()
            val pointerCount = event.changes.count { it.pressed }

            if (pointerCount > 1) {
                return@awaitEachGesture
            }

            val startCell = down.position.toGridCell(
                size.width, size.height,
                canvasWidth, canvasHeight
            )

            onEvent(PixelCanvasEvent.DrawAt(startCell.x, startCell.y))

            if (selectedTool in listOf(PencilTool, EraserTool)) {
                var lastCell = startCell

                drag(down.id) { change ->
                    change.consume()
                    val dragCell = change.position.toGridCell(
                        size.width, size.height,
                        canvasWidth, canvasHeight
                    )

                    if (dragCell != lastCell) {
                        val linePoints = DrawUtils.bresenhamLine(
                            lastCell.x.toInt(), lastCell.y.toInt(),
                            dragCell.x.toInt(), dragCell.y.toInt()
                        )

                        for ((x, y) in linePoints) {
                            onEvent(PixelCanvasEvent.DrawAt(x, y))
                        }

                        lastCell = dragCell
                    }
                }
            }
        }
    }
}