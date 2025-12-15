package com.example.presentation.shops.state

sealed class ShopsEvent {
    object StartRequest : ShopsEvent()
}