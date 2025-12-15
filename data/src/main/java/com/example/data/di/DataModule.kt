package com.example.data.di

import com.example.data.shop.local.source.ShopLocalDataSource
import com.example.data.shop.local.source.ShopLocalDataSourceImpl
import com.example.data.shop.remote.source.ShopRemoteDataSource
import com.example.data.shop.remote.source.ShopRemoteDataSourceImpl
import com.example.data.shop.repository.ShopRemoteRepository
import com.example.shops.domain.repository.ShopApiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindShopRepository(
        impl: ShopRemoteRepository
    ): ShopApiRepository

    @Binds
    @Singleton
    abstract fun bindShopRemoteDataSource(
        shopRemoteDataSource: ShopRemoteDataSourceImpl
    ): ShopRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindShopLocalDataSource(
        shopLocalDataSource: ShopLocalDataSourceImpl
    ): ShopLocalDataSource
}
