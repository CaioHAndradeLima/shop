package com.example.common_ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest

@Composable
fun CoilImageComponent(
    imageUrl: Uri?,
    width: Dp = 104.dp,
    boxModifier: Modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .background(Color.Gray),
    modifier: Modifier = Modifier
        .height((width.value * 1.36).dp),
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .placeholder(R.drawable.ic_shop_placeholder)
            .error(R.drawable.ic_shop_placeholder)
            .fallback(R.drawable.ic_shop_placeholder)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build(),
        contentDescription = stringResource(id = R.string.shop_image_accessibility),
        modifier = boxModifier.then(modifier),
        contentScale = contentScale
    )
}
