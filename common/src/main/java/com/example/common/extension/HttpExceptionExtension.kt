package com.example.common.extension

import com.example.common.R
import com.example.common.resource.UiText
import retrofit2.HttpException

fun HttpException.toErrorMessage(): UiText {
    return if(localizedMessage.isNullOrEmpty()) {
        UiText.Resource(R.string.an_unexpected_error_occurred)
    } else {
        UiText.Dynamic(localizedMessage)
    }
}