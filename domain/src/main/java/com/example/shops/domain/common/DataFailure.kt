package com.example.shops.domain.common

import com.example.common.resource.UiText

sealed class DataFailure : Throwable() {

    object Connection : DataFailure()

    data class Http(
        val code: Int,
        val uiText: UiText
    ) : DataFailure()

    data class Unknown(
        val error: Throwable
    ) : DataFailure()
}