package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.database.AppDatabase
import com.example.data.database.DatabaseCallback
import com.example.data.shop.local.dao.ShopDao
import com.google.gson.Gson
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        databaseCallback: DatabaseCallback,
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "shop_db.db"
        )
            .addCallback(databaseCallback)
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabaseCallback(
        @ApplicationContext context: Context,
        gson: Gson,
        applicationScope: CoroutineScope,
        appDatabase: Lazy<AppDatabase>
    ): DatabaseCallback = DatabaseCallback(
        context = context,
        appDatabase = appDatabase,
        gson = gson,
        applicationScope = applicationScope,
    )

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    @Singleton
    fun provideShopDao(appDatabase: Lazy<AppDatabase>): ShopDao =
        appDatabase.get().shopDao()
}
