package com.olaz.instasprite.ui.screens.postscreen.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.ui.components.composable.CanvasPreviewer
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun PostImage(
    sprite: ISpriteData,
    onDoubleClick: () -> Unit,
    onImageClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CatppuccinUI.BackgroundColor
        )
    ) {
        CanvasPreviewer(
            spriteData = sprite,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp)
                .clickable { onImageClick() },
            onClick = onDoubleClick
        )
    }
}