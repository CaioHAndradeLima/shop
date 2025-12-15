package com.example.data.util

import com.example.common.extension.toErrorMessage
import com.example.shops.domain.common.DataFailure
import retrofit2.HttpException
import java.io.IOException

internal suspend inline fun <T> callApi(
    crossinline block: suspend () -> T
): Result<T> {
    return try {
        Result.success(block())
    } catch (e: HttpException) {
        Result.failure(DataFailure.Http(e.code(), uiText = e.toErrorMessage()))
    } catch (e: IOException) {
        Result.failure(DataFailure.Connection)
    } catch (e: Throwable) {
        Result.failure(DataFailure.Unknown(e))
    }
}

internal suspend inline fun <T> Result<T>.orLocal(crossinline block: suspend () -> T): Result<T> {
    when {
        isFailure -> {
            return try {
                Result.success(block())
            } catch (e: Throwable) {
                Result.failure(DataFailure.Unknown(e))
            }
        }
    }

    return this
}