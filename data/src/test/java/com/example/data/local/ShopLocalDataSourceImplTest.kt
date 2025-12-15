package com.example.data.local

import com.example.data.shop.local.dao.ShopDao
import com.example.data.shop.local.entity.ShopEntity
import com.example.data.shop.local.source.ShopLocalDataSourceImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ShopLocalDataSourceImplTest {

    private val dao: ShopDao = mockk()

    private lateinit var dataSource: ShopLocalDataSourceImpl

    @BeforeEach
    fun setup() {
        dataSource = ShopLocalDataSourceImpl(dao)
    }

    @Test
    fun `getShops should return shops from dao`() = runTest {
        // given
        val entities = listOf(
            mockk<ShopEntity>(),
            mockk<ShopEntity>(),
        )

        coEvery { dao.getShops() } returns entities

        // when
        val result = dataSource.getShops()

        // then
        assertEquals(entities, result)
        coVerify(exactly = 1) { dao.getShops() }
    }

    @Test
    fun `observeShops should emit values from dao`() = runTest {
        // given
        val entities = listOf(
            mockk<ShopEntity>()
        )
        val flow = flowOf(entities)

        every { dao.observeShops() } returns flow

        // when
        val result = dataSource.observeShops().first()

        // then
        assertEquals(entities, result)
        verify(exactly = 1) {
            @Suppress("UnusedFlow")
            dao.observeShops()
        }
    }

    @Test
    fun `getShop should return shop by id from dao`() = runTest {
        // given
        val entity = mockk<ShopEntity>()
        every { entity.id } returns "1"
        coEvery { dao.getShop("1") } returns entity

        // when
        val result = dataSource.getShop("1")

        // then
        assertEquals(entity, result)
        coVerify(exactly = 1) { dao.getShop("1") }
    }

    @Test
    fun `updateShops should insert all shops via dao`() = runTest {
        // given
        val entities = listOf(
            mockk<ShopEntity>(),
            mockk<ShopEntity>(),
        )

        coEvery { dao.insertAll(entities) } just Runs

        // when
        dataSource.updateShops(entities)

        // then
        coVerify(exactly = 1) { dao.insertAll(entities) }
    }
}