package com.example.presentation.shop.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.example.shops.domain.entity.Shop

@Composable
fun ShopDetailComponent(shop: Shop) {
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        ShopDetailsLandscape(shop)
    } else {
        ShopDetailsPortrait(shop)
    }
}