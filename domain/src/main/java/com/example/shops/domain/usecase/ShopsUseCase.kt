package com.example.shops.domain.usecase

import com.example.shops.domain.common.DataFailure
import com.example.shops.domain.common.RequestResource
import com.example.shops.domain.entity.Shop
import com.example.shops.domain.repository.ShopApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ShopsUseCase @Inject constructor(
    private val repository: ShopApiRepository
) {

    operator fun invoke(): Flow<RequestResource<List<Shop>>> =
        repository.observeShops()
            .map<List<Shop>, RequestResource<List<Shop>>> { shops ->
                RequestResource.Success(shops)
            }
            .onStart {
                emit(RequestResource.Loading())
            }
            .catch { throwable ->
                val failure = throwable as? DataFailure
                    ?: DataFailure.Unknown(throwable)
                emit(RequestResource.Error(failure))
            }
}
