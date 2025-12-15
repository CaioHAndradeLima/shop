package com.example.presentation.shop.state

sealed class ShopEvent {
    object Retry : ShopEvent()
}
