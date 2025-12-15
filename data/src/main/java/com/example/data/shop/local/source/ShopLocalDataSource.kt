package com.example.data.shop.local.source

import androidx.annotation.VisibleForTesting
import com.example.data.shop.local.entity.ShopEntity
import kotlinx.coroutines.flow.Flow

sealed interface ShopLocalDataSource {

    fun observeShops(): Flow<List<ShopEntity>>
    suspend fun getShops(): List<ShopEntity>
    suspend fun getShop(id: String): ShopEntity
    suspend fun updateShops(shops: List<ShopEntity>)
}
