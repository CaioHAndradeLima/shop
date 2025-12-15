@file:Suppress("FilterIsInstanceResultIsAlwaysEmpty")

package com.example.domain

import com.example.shops.domain.common.DataFailure
import com.example.shops.domain.common.RequestResource
import com.example.shops.domain.entity.Shop
import com.example.shops.domain.repository.ShopApiRepository
import com.example.shops.domain.usecase.ShopUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigInteger

@ExtendWith(MockKExtension::class)
class ShopUseCaseTest {

    @MockK
    lateinit var repository: ShopApiRepository

    private lateinit var useCase: ShopUseCase

    @BeforeEach
    fun setup() {
        useCase = ShopUseCase(repository)
    }

    @Test
    fun `invoke should emit Loading first`() = runTest {
        val shop = mockk<Shop>().apply {
            every { id } returns BigInteger.ONE
        }
        coEvery { repository.getShop("1") } returns Result.success(shop)

        val emissions = useCase("1").toList()

        assert(emissions.first() is RequestResource.Loading)
    }

    @Test
    fun `invoke should emit Success when repository returns success`() = runTest {
        val shop = mockk<Shop>().apply {
            every { id } returns BigInteger.ONE
        }
        coEvery { repository.getShop("1") } returns Result.success(shop)

        val emissions = useCase("1").toList()

        val success = emissions.filterIsInstance<RequestResource.Success<Shop>>().first()
        assertEquals(shop, success.data)
    }

    @Test
    fun `invoke should emit Error when repository returns DataFailure`() = runTest {
        val failure = DataFailure.Connection
        coEvery { repository.getShop("1") } returns Result.failure(failure)

        val emissions = useCase("1").toList()

        val error = emissions.filterIsInstance<RequestResource.Error<*>>().first()
        assertEquals(failure, error.failure)
    }

    @Test
    fun `invoke should wrap unknown exception as DataFailure_Unknown`() = runTest {
        val exception = IllegalStateException("boom")
        coEvery { repository.getShop("1") } returns Result.failure(exception)

        val emissions = useCase("1").toList()

        val error = emissions.filterIsInstance<RequestResource.Error<*>>().first()
        assert(error.failure is DataFailure.Unknown)
    }
}
