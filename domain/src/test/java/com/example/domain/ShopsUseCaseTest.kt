@file:Suppress("FilterIsInstanceResultIsAlwaysEmpty")

package com.example.domain

import com.example.shops.domain.common.DataFailure
import com.example.shops.domain.common.RequestResource
import com.example.shops.domain.entity.Shop
import com.example.shops.domain.repository.ShopApiRepository
import com.example.shops.domain.usecase.ShopsUseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ShopsUseCaseTest {

    @MockK
    lateinit var repository: ShopApiRepository

    private lateinit var useCase: ShopsUseCase

    @BeforeEach
    fun setup() {
        useCase = ShopsUseCase(repository)
    }

    @Test
    fun `invoke should emit Loading first`() = runTest {
        every { repository.observeShops() } returns flowOf(emptyList())

        val emissions = useCase().toList()

        assert(emissions.first() is RequestResource.Loading)
    }

    @Test
    fun `invoke should emit Success when repository emits shops`() = runTest {
        val shops = listOf(
            mockk<Shop>(name = "Shop 1")
        )
        every { repository.observeShops() } returns flowOf(shops)

        val emissions = useCase().toList()

        val success = emissions.filterIsInstance<RequestResource.Success<List<Shop>>>().first()
        assertSame(shops, success.data)
    }

    @Test
    fun `invoke should emit Error when repository throws DataFailure`() = runTest {
        val failure = DataFailure.Connection
        every { repository.observeShops() } returns flow {
            throw failure
        }

        val emissions = useCase().toList()

        val error = emissions.filterIsInstance<RequestResource.Error<*>>().first()
        assertEquals(failure, error.failure)
    }

    @Test
    fun `invoke should wrap unknown exception as DataFailure_Unknown`() = runTest {
        val exception = IllegalStateException("boom")
        every { repository.observeShops() } returns flow {
            throw exception
        }

        val emissions = useCase().toList()

        val error = emissions.filterIsInstance<RequestResource.Error<*>>().first()
        assert(error.failure is DataFailure.Unknown)
    }
}
