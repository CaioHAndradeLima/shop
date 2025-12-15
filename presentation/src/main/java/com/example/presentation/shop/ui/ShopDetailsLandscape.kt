package com.example.presentation.shop.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.shops.domain.entity.Shop

@Composable
fun ShopDetailsLandscape(shop: Shop) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ShopHeaderImage(
            imageUrl = shop.picture?.toUri(),
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        ShopDetailsContent(
            shop = shop,
            modifier = Modifier.weight(1f)
        )
    }
}
