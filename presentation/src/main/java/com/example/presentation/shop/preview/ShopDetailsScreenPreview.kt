package com.example.presentation.shop.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.presentation.preview.PreviewWrapper
import com.example.presentation.preview.shopPreview
import com.example.presentation.shop.ui.ShopDetailsContent

@Preview(
    name = "Portrait",
    showBackground = true,
    device = Devices.PIXEL_4
)
@Composable
fun ShopDetailsScreenPortraitPreview() {
    PreviewWrapper {
        ShopDetailsContent(
            shop = shopPreview(),
        )
    }
}

@Preview(
    name = "Landscape",
    showBackground = true,
    device = Devices.TABLET
)
@Composable
fun ShopDetailsScreenLandscapePreview() {
    PreviewWrapper {
        ShopDetailsContent(
            shop = shopPreview(),
        )
    }
}
