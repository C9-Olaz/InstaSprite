package com.olaz.instasprite.ui.screens.drawingscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.olaz.instasprite.ui.screens.drawingscreen.dialog.LoadISpriteDialog
import com.olaz.instasprite.ui.screens.drawingscreen.dialog.ResizeCanvasDialog
import com.olaz.instasprite.ui.screens.drawingscreen.dialog.SaveISpriteDialog
import com.olaz.instasprite.ui.screens.drawingscreen.dialog.SaveImageDialog
import androidx.compose.runtime.collectAsState
import com.olaz.instasprite.ui.screens.drawingscreen.dialog.ColorWheelDialog
import com.olaz.instasprite.ui.screens.drawingscreen.dialog.FileImportDialog
import com.olaz.instasprite.ui.screens.drawingscreen.dialog.ImportOptionsDialog
import com.olaz.instasprite.ui.screens.drawingscreen.dialog.LospecImportDialog

@Composable
fun DrawingScreenDialogs(
    uiState: DrawingScreenState,
    viewModel: DrawingScreenViewModel
) {
    val context = LocalContext.current

    var lastSavedUri = viewModel.lastSavedLocation.collectAsState().value

    LaunchedEffect(Unit) {
        lastSavedUri = viewModel.getLastSavedLocation()
    }

    uiState.dialogStack.forEach { dialog ->
        when (dialog) {
            ActiveDialog.SaveISprite ->
                SaveISpriteDialog(
                    onDismiss = viewModel::closeTopDialog,
                    folderUri = lastSavedUri,
                    onFolderSelected = viewModel::setLastSavedLocation,
                    onSave = { uri, name -> viewModel.saveISprite(context, uri, name) }
                )

            ActiveDialog.SaveImage ->
                SaveImageDialog(
                    onDismiss = viewModel::closeTopDialog,
                    folderUri = lastSavedUri,
                    onFolderSelected = viewModel::setLastSavedLocation,
                    onSave = { uri, name, scale -> viewModel.saveImage(context, uri, name, scale) }
                )

            ActiveDialog.LoadISprite ->
                LoadISpriteDialog(
                    onDismiss = viewModel::closeTopDialog,

                    onFilePicked = { uri ->
                        viewModel.getISpriteDataFromFile(context, uri)
                    },

                    onLoad = viewModel::loadISprite
                )

            ActiveDialog.ResizeCanvas ->
                ResizeCanvasDialog(
                    onDismiss = viewModel::closeTopDialog,
                    currentCanvasWidth = viewModel.canvasState.value.width,
                    currentCanvasHeight = viewModel.canvasState.value.height,
                    onResize = viewModel::resizeCanvas
                )

            ActiveDialog.ColorWheel ->
                ColorWheelDialog(
                    initialColor = viewModel.activeColor.collectAsState().value,
                    colorPalette = viewModel.colorPalette.collectAsState().value,
                    onDismiss = viewModel::closeTopDialog,
                    onColorSelected = viewModel::selectColor,
                    onOpenImportColorPaletteDialog = {
                        viewModel.openDialog(ActiveDialog.ImportColorPalettes)
                    }
                )

            ActiveDialog.ImportColorPalettes ->
                ImportOptionsDialog(
                    onDismiss = viewModel::closeTopDialog,
                    onLospecSelected = {
                        viewModel.openDialog(ActiveDialog.LospecPaletteImport)
                    },
                    onFileSelected = {
                        viewModel.openDialog(ActiveDialog.FilePaletteImport)
                    }
                )

            ActiveDialog.FilePaletteImport ->
                FileImportDialog(
                    onDismiss = {
                        // Hacky way to close import dialog, since we need to close down to color wheel
                        viewModel.closeTopDialog()
                        viewModel.closeTopDialog()
                    },
                    onImportPaletteFromFile = viewModel::importColorsFromFile,
                    onImport = viewModel::updateColorPalette,
                )

            ActiveDialog.LospecPaletteImport ->
                LospecImportDialog(
                    onDismiss = {
                        viewModel.closeTopDialog()
                        viewModel.closeTopDialog()
                    },
                    onImportColorsFromLospecUrl = viewModel::importColorsFromLospecUrl,
                    onImport = viewModel::updateColorPalette,
                )
        }
    }
}