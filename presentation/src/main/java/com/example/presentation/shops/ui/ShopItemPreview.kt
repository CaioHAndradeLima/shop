package com.example.presentation.shops.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.presentation.preview.PreviewTheme
import com.example.presentation.preview.shopPreview

@Preview(showBackground = true)
@Composable
fun ShopItemPreview() {
    PreviewTheme {
        ShopItemComponent(
            shop = shopPreview(),
            onItemClick = { shop -> }
        )
    }
}
