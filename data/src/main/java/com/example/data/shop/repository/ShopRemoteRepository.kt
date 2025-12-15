package com.example.data.shop.repository

import com.example.data.shop.local.source.ShopLocalDataSource
import com.example.data.shop.mapper.asDomain
import com.example.data.shop.mapper.toDomain
import com.example.data.shop.mapper.toEntity
import com.example.data.shop.remote.source.ShopRemoteDataSource
import com.example.data.util.callApi
import com.example.data.util.orLocal
import com.example.shops.domain.entity.Shop
import com.example.shops.domain.repository.ShopApiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class ShopRemoteRepository @Inject constructor(
    private val remoteDataSource: ShopRemoteDataSource,
    private val localDataSource: ShopLocalDataSource,
    private val applicationScope: CoroutineScope
) : ShopApiRepository {

    /**
     * Local-first repository.
     *
     * Local data is updated everytime the App is opened
     * initial data prepopulated from assets shops.json file.
     */

    override fun observeShops(): Flow<List<Shop>> =
        localDataSource.observeShops()
            .map { entities -> entities.map { it.toDomain() } }
            .onStart {
                applicationScope.launch {
                    val result = callApi {
                        remoteDataSource.getShops()
                    }

                    result.getOrNull()?.takeIf { it.isNotEmpty() }?.let { shops ->
                        localDataSource.updateShops(
                            shops.map { it.toEntity() }
                        )
                    }
                }
            }.distinctUntilChanged()

    /**
     * Remote-first repository.
     *
     * Remote data is NOT persisted locally.
     * Local data is used only as remote error scenario
     * prepopulated from assets shops.json file.
     */
    override suspend fun getShops() = callApi {
        remoteDataSource.getShops().toDomain()
    }.orLocal { localDataSource.getShops().asDomain() }


    override suspend fun getShop(id: String) = callApi {
        remoteDataSource.getShops().first { it.id.toString() == id }.toDomain()
    }.orLocal { localDataSource.getShop(id).toDomain() }
}
