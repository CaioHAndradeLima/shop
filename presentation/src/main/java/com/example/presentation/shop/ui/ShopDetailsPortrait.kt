package com.example.presentation.shop.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import com.example.shops.domain.entity.Shop

@Composable
fun ShopDetailsPortrait(
    shop: Shop,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ShopHeaderImage(shop.picture?.toUri())
        ShopDetailsContent(shop)
    }
}
