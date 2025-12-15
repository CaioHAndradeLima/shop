package com.example.data.shop.local.source

import com.example.data.shop.local.dao.ShopDao
import com.example.data.shop.local.entity.ShopEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ShopLocalDataSourceImpl @Inject constructor(
    private val dao: ShopDao
) : ShopLocalDataSource {

    override suspend fun getShops(): List<ShopEntity> = dao.getShops()

    override fun observeShops(): Flow<List<ShopEntity>> = dao.observeShops()

    override suspend fun getShop(id: String): ShopEntity = dao.getShop(id)

    override suspend fun updateShops(shops: List<ShopEntity>) = dao.insertAll(shops)
}
