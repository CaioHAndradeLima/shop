package com.example.presentation.shops.state

import com.example.shops.domain.common.DataFailure
import com.example.shops.domain.entity.Shop

sealed class ShopsState {
    object Loading : ShopsState()
    data class Show(val shops: List<Shop>) : ShopsState()
    data class Error(val failure: DataFailure) : ShopsState()
}
