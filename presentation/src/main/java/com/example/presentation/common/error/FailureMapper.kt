package com.example.presentation.common.error

import com.example.common.resource.UiText
import com.example.shops.domain.common.DataFailure
import com.example.presentation.R

fun DataFailure.toErrorText() = when (this) {
    is DataFailure.Connection ->
        UiText.Resource(R.string.check_your_internet_connection)
    is DataFailure.Http -> uiText
    is DataFailure.Unknown -> UiText.Resource(R.string.unexpected_error)
}