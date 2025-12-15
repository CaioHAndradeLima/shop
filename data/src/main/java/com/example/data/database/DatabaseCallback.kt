package com.example.data.database

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.shop.mapper.toEntity
import com.example.data.shop.remote.dto.ShopDto
import com.google.gson.Gson
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class DatabaseCallback(
    private val context: Context,
    private val applicationScope: CoroutineScope,
    private val gson: Gson,
    private val appDatabase: Lazy<AppDatabase>
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        applicationScope.launch {
            val json = context.assets
                .open("shops.json")
                .bufferedReader()
                .use { it.readText() }

            val shops = gson.fromJson(json, Array<ShopDto>::class.java)
                .map { it.toEntity() }

            appDatabase.get().shopDao().insertAll(shops.toList())
        }
    }
}
