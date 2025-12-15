package com.example.presentation.shop.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shops.domain.common.RequestResource
import com.example.shops.domain.usecase.ShopUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val shopUseCase: ShopUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _shopState = MutableStateFlow<ShopState>(ShopState.Loading)
    val shopState = _shopState.asStateFlow()

    private val shopId: String =
        requireNotNull(savedStateHandle["id"]) {
            "Shop id is required"
        }

    init {
        loadShop()
    }

    fun on(event: ShopEvent) {
        when (event) {
            ShopEvent.Retry -> loadShop()
        }
    }

    private fun loadShop() {
        shopUseCase(shopId)
            .onEach { result ->
                _shopState.value = when (result) {
                    is RequestResource.Loading ->
                        ShopState.Loading

                    is RequestResource.Success ->
                        ShopState.Show(result.data)

                    is RequestResource.Error ->
                        ShopState.Error(result.failure)
                }
            }
            .launchIn(viewModelScope)
    }
}
