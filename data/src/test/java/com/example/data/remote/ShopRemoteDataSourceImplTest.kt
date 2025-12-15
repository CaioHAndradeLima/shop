package com.example.data.remote

import com.example.data.shop.remote.api.ShopApi
import com.example.data.shop.remote.dto.ShopDto
import com.example.data.shop.remote.source.ShopRemoteDataSourceImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ShopRemoteDataSourceImplTest {

    private val api = mockk<ShopApi>()

    private lateinit var dataSource: ShopRemoteDataSourceImpl

    @BeforeEach
    fun setup() {
        dataSource = ShopRemoteDataSourceImpl(api)
    }

    @Test
    fun `getShops should return shops from api`() = runTest {
        val shops = listOf(
            mockk<ShopDto>(),
            mockk<ShopDto>(),
        )
        coEvery { api.getShops() } returns shops

        val result = dataSource.getShops()

        assertEquals(shops, result)
        coVerify { api.getShops() }
    }

    @Test
    fun `getShop should return shop with matching id`() = runTest {
        val shop1 = mockk<ShopDto>().apply { every { id } returns "1".toBigInteger() }
        val shop2 = mockk<ShopDto>().apply { every { id } returns "2".toBigInteger() }

        coEvery { api.getShops() } returns listOf(shop1, shop2)

        val result = dataSource.getShop("2")

        assertEquals(shop2, result)
        coVerify { api.getShops() }
    }
}
