package com.example.shops.domain.usecase

import com.example.shops.domain.common.RequestResource
import com.example.shops.domain.entity.Shop
import com.example.shops.domain.repository.ShopApiRepository
import com.example.shops.domain.common.DataFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShopUseCase @Inject constructor(
    private val repository: ShopApiRepository
) {

    operator fun invoke(id: String): Flow<RequestResource<Shop>> = flow {
        emit(RequestResource.Loading())

        val result = repository.getShop(id)

        result.fold(
            onSuccess = { shop ->
                emit(RequestResource.Success(shop))
            },
            onFailure = { throwable ->
                val failure = throwable as? DataFailure
                    ?: DataFailure.Unknown(throwable)

                emit(RequestResource.Error(failure))
            }
        )
    }
}
