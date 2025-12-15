package com.example.data.shop.remote.source

import com.example.data.shop.remote.dto.ShopDto

sealed interface ShopRemoteDataSource {
    suspend fun getShops(): List<ShopDto>
    suspend fun getShop(id: String): ShopDto
}
