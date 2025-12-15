package com.example.presentation.shop.ui

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.common_ui.CoilImageComponent

@Composable
fun ShopHeaderImage(
    imageUrl: Uri?,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(280.dp)
) {
    CoilImageComponent(
        imageUrl = imageUrl,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}
