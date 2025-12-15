@file:Suppress("UnusedFlow")

package com.example.presentation.shop.state

import androidx.lifecycle.SavedStateHandle
import com.example.presentation.MainDispatcherRule
import com.example.shops.domain.common.DataFailure
import com.example.shops.domain.common.RequestResource
import com.example.shops.domain.entity.Shop
import com.example.shops.domain.usecase.ShopUseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
@ExtendWith(MainDispatcherRule::class)
class ShopViewModelTest {

    @MockK
    lateinit var shopUseCase: ShopUseCase

    private lateinit var viewModel: ShopViewModel

    private val shopId = "1"

    private fun createViewModel(
        flow: Flow<RequestResource<Shop>>
    ) {
        every { shopUseCase(shopId) } returns flow

        val savedStateHandle = SavedStateHandle(
            mapOf("id" to shopId)
        )

        viewModel = ShopViewModel(
            shopUseCase = shopUseCase,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `initial state is Loading`() = runTest {
        createViewModel(flowOf(RequestResource.Loading<Shop>()))

        assertEquals(
            ShopState.Loading,
            viewModel.shopState.value
        )
    }

    @Test
    fun `init calls use case with id`() = runTest {
        createViewModel(flowOf(RequestResource.Loading<Shop>()))

        verify(exactly = 1) { shopUseCase(shopId) }
    }

    @Test
    fun `success emits Show state`() = runTest {
        val shop = mockk<Shop>()

        createViewModel(
            flowOf(
                RequestResource.Loading(),
                RequestResource.Success(shop)
            )
        )

        advanceUntilIdle()

        assertEquals(
            ShopState.Show(shop),
            viewModel.shopState.value
        )
    }

    @Test
    fun `error emits Error state`() = runTest {
        val failure = DataFailure.Connection

        createViewModel(
            flowOf(
                RequestResource.Loading(),
                RequestResource.Error(failure)
            )
        )

        advanceUntilIdle()

        assertEquals(
            ShopState.Error(failure),
            viewModel.shopState.value
        )
    }

    @Test
    fun `retry calls use case again`() = runTest {
        every { shopUseCase(shopId) } returnsMany listOf(
            flowOf(RequestResource.Error(DataFailure.Connection)),
            flowOf(RequestResource.Success(mockk()))
        )

        val savedStateHandle = SavedStateHandle(
            mapOf("id" to shopId)
        )

        viewModel = ShopViewModel(
            shopUseCase = shopUseCase,
            savedStateHandle = savedStateHandle
        )

        advanceUntilIdle()

        viewModel.on(ShopEvent.Retry)
        advanceUntilIdle()

        verify(exactly = 2) { shopUseCase(shopId) }
    }
}
