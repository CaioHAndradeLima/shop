package com.example.data.repository

import com.example.data.shop.local.entity.ShopEntity
import com.example.data.shop.local.source.ShopLocalDataSource
import com.example.data.shop.remote.dto.ShopDto
import com.example.data.shop.remote.source.ShopRemoteDataSource
import com.example.data.shop.repository.ShopRemoteRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class ShopRemoteRepositoryTest {

    @MockK
    lateinit var remoteDataSource: ShopRemoteDataSource

    @MockK
    lateinit var localDataSource: ShopLocalDataSource

    private lateinit var repository: ShopRemoteRepository

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = CoroutineScope(testDispatcher)

    @BeforeEach
    fun setup() {
        repository = ShopRemoteRepository(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            applicationScope = testScope
        )
    }

    @Test
    fun `observeShops should emit mapped local shops`() = runTest {
        // given
        val entities = listOf(
            shopEntityMock("1", "Shop 1")
        )

        every { localDataSource.observeShops() } returns flowOf(entities)
        coEvery { remoteDataSource.getShops() } returns emptyList()
        coEvery { localDataSource.updateShops(any()) } just Runs

        // when
        val result = repository.observeShops().first()

        // then
        assertEquals(1, result.size)
        assertEquals("Shop 1", result.first().name)
    }

    @Test
    fun `observeShops should update local when remote returns data`() = runTest {
        // given
        val remoteDtos = listOf(
            shopDtoMock("Remote Shop")
        )

        every { localDataSource.observeShops() } returns flowOf(emptyList())
        coEvery { remoteDataSource.getShops() } returns remoteDtos
        coEvery { localDataSource.updateShops(any()) } just Runs

        // when
        repository.observeShops().first()
        testDispatcher.scheduler.advanceUntilIdle()

        // then
        coVerify(exactly = 1) {
            localDataSource.updateShops(
                match { it.first().name == "Remote Shop" }
            )
        }
    }

    @Test
    fun `getShops should return remote data when remote succeeds`() = runTest {
        // given
        val remoteDtos = listOf(
            shopDtoMock("Remote Shop")
        )

        coEvery { remoteDataSource.getShops() } returns remoteDtos

        // when
        val result = repository.getShops().getOrThrow()

        // then
        assertEquals(1, result.size)
        assertEquals("Remote Shop", result.first().name)
    }

    @Test
    fun `getShops should fallback to local when remote fails`() = runTest {
        // given
        val localEntities = listOf(
            shopEntityMock(name = "Local Shop")
        )

        coEvery { remoteDataSource.getShops() } throws RuntimeException("Network error")
        coEvery { localDataSource.getShops() } returns localEntities

        // when
        val result = repository.getShops().getOrThrow()

        // then
        assertEquals(1, result.size)
        assertEquals("Local Shop", result.first().name)
    }

    @Test
    fun `getShop should return remote shop when remote succeeds`() = runTest {
        // given
        val remoteDtos = listOf(shopDtoMock("Remote Shop"))

        coEvery { remoteDataSource.getShops() } returns remoteDtos
        coEvery { localDataSource.getShop(any()) } returns mockk()
        // when
        val result = repository.getShop("23550546633311192223").getOrThrow()

        // then
        assertEquals("Remote Shop", result.name)
        coVerify(exactly = 0) { localDataSource.getShop(any()) }
    }

    @Test
    fun `getShop should fallback to local when remote fails`() = runTest {
        // given
        val localEntity = shopEntityMock("1", "Local Shop")

        coEvery { remoteDataSource.getShops() } throws RuntimeException()
        coEvery { localDataSource.getShop("1") } returns localEntity

        // when
        val result = repository.getShop("1").getOrThrow()

        // then
        assertEquals("Local Shop", result.name)
    }

    fun shopDtoMock(
        name: String = "Coffee Shop",
        description: String = "Best coffee in town",
        picture: String? = "https://image.jpg",
        rating: Double = 4.5,
        address: String = "123 Main Street",
        latitude: Double = -23.5505,
        longitude: Double = -46.6333,
        googleMapsLink: String = "https://maps.google.com",
        website: String = "https://coffeeshop.com"
    ): ShopDto {
        return ShopDto(
            name = name,
            description = description,
            picture = picture,
            rating = rating,
            address = address,
            coordinates = listOf(latitude, longitude),
            googleMapsLink = googleMapsLink,
            website = website
        )
    }

    fun shopEntityMock(
        id: String = "123",
        name: String = "Coffee Shop",
        description: String = "Best coffee in town",
        rating: Double = 4.5,
        address: String = "123 Main Street",
        picture: String? = "https://image.jpg",
        latitude: Double = -23.5505,
        longitude: Double = -46.6333,
        googleMapsLink: String = "https://maps.google.com",
        website: String = "https://coffeeshop.com"
    ): ShopEntity {
        return ShopEntity(
            id = id,
            name = name,
            description = description,
            rating = rating,
            address = address,
            picture = picture,
            latitude = latitude,
            longitude = longitude,
            googleMapsLink = googleMapsLink,
            website = website
        )
    }
}
