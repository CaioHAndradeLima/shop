package com.example.shops.domain.repository

import com.example.shops.domain.entity.Shop
import kotlinx.coroutines.flow.Flow

interface ShopApiRepository {

    fun observeShops(): Flow<List<Shop>>
    suspend fun getShops(): Result<List<Shop>>

    suspend fun getShop(id: String): Result<Shop>
}