package com.example.presentation.shop.state

import com.example.shops.domain.common.DataFailure
import com.example.shops.domain.entity.Shop

sealed class ShopState {
    object Loading : ShopState()
    data class Show(val shop: Shop) : ShopState()
    data class Error(val failure: DataFailure) : ShopState()
}
