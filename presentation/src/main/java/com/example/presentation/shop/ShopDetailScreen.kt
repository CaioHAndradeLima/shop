package com.example.presentation.shop

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.shop.state.ShopEvent
import com.example.presentation.shop.state.ShopViewModel
import com.example.presentation.shop.ui.ShopDetailContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDetailScreen(
    onBack: () -> Unit,
    viewModel: ShopViewModel = hiltViewModel(),
) {
    val state by viewModel.shopState.collectAsStateWithLifecycle()

    ShopDetailContent(
        state = state,
        onBack = onBack,
        onRetry = { viewModel.on(ShopEvent.Retry) }
    )
}
