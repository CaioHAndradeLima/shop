@file:Suppress("UnusedFlow")
package com.example.presentation.shops.state

import com.example.presentation.MainDispatcherRule
import com.example.shops.domain.common.DataFailure
import com.example.shops.domain.common.RequestResource
import com.example.shops.domain.entity.Shop
import com.example.shops.domain.usecase.ShopsUseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
@ExtendWith(MainDispatcherRule::class)
class ShopsViewModelTest {

    @MockK
    lateinit var shopsUseCase: ShopsUseCase

    private lateinit var viewModel: ShopsViewModel

    @Test
    fun `initial state should be Loading`() {
        every { shopsUseCase() } returns flowOf(RequestResource.Loading())

        viewModel = ShopsViewModel(shopsUseCase)

        assertEquals(ShopsState.Loading, viewModel.shopsState.value)
    }

    @Test
    fun `on init should emit Show when use case succeeds`() = runTest {
        val shops = listOf(
            mockk<Shop>(),
        )

        every { shopsUseCase() } returns flowOf(
            RequestResource.Loading(),
            RequestResource.Success(shops)
        )

        viewModel = ShopsViewModel(shopsUseCase)
        advanceUntilIdle()

        assertEquals(ShopsState.Show(shops), viewModel.shopsState.value)
    }

    @Test
    fun `on init should emit Error when use case fails`() = runTest {
        val failure = DataFailure.Connection

        every { shopsUseCase() } returns flowOf(
            RequestResource.Loading(),
            RequestResource.Error(failure)
        )

        viewModel = ShopsViewModel(shopsUseCase)
        advanceUntilIdle()

        assertEquals(ShopsState.Error(failure), viewModel.shopsState.value)
    }

    @Test
    fun `use case should be called once on init`() = runTest {
        every { shopsUseCase() } returns flowOf(RequestResource.Loading())

        viewModel = ShopsViewModel(shopsUseCase)
        advanceUntilIdle()

        verify(exactly = 1) { shopsUseCase() }
    }

    @Test
    fun `StartRequest event should trigger getShops`() = runTest {
        every { shopsUseCase() } returns flowOf(RequestResource.Loading())

        viewModel = ShopsViewModel(shopsUseCase)
        advanceUntilIdle()

        viewModel.on(ShopsEvent.StartRequest)
        advanceUntilIdle()

        verify(exactly = 2) { shopsUseCase() }
    }
}
