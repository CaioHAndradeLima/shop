package com.example.presentation.shops.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shops.domain.common.RequestResource
import com.example.shops.domain.usecase.ShopsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val shopsUseCase: ShopsUseCase,
) : ViewModel() {

    private val _shopsState = MutableStateFlow<ShopsState>(ShopsState.Loading)
    val shopsState = _shopsState.asStateFlow()

    init {
        on(ShopsEvent.StartRequest)
    }

    fun on(event: ShopsEvent) {
        when (event) {
            ShopsEvent.StartRequest -> getShops()
        }
    }

    private fun getShops() {
        shopsUseCase().onEach { result ->

            when (result) {

                is RequestResource.Loading ->
                    _shopsState.value = ShopsState.Loading

                is RequestResource.Success ->
                    _shopsState.value = ShopsState.Show(result.data)

                is RequestResource.Error ->
                    _shopsState.value = ShopsState.Error(result.failure)
            }

        }.launchIn(viewModelScope)
    }
}
