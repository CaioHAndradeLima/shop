package com.example.data.shop.remote.source

import com.example.data.shop.remote.api.ShopApi
import com.example.data.shop.remote.dto.ShopDto
import javax.inject.Inject

internal class ShopRemoteDataSourceImpl @Inject constructor(
    private val api: ShopApi
) : ShopRemoteDataSource {

    override suspend fun getShops(): List<ShopDto> {
        return api.getShops()
    }

    override suspend fun getShop(id: String): ShopDto {
        return api.getShops().first { id == it.id.toString() }
    }
}
