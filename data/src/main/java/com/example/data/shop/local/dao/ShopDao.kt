package com.example.data.shop.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.shop.local.entity.ShopEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ShopDao {

    @Query("SELECT * FROM shops")
    fun observeShops(): Flow<List<ShopEntity>>

    @Query("SELECT * FROM shops")
    suspend fun getShops(): List<ShopEntity>

    @Query("SELECT * FROM shops WHERE id = :id")
    suspend fun getShop(id: String): ShopEntity

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(shops: List<ShopEntity>)
}