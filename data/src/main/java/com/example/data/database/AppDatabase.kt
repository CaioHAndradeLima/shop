package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.shop.local.dao.ShopDao
import com.example.data.shop.local.entity.ShopEntity

@Database(
    entities = [ShopEntity::class],
    version = 1,
    exportSchema = false
)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun shopDao(): ShopDao
}
