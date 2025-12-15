package com.example.presentation.shops

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.shops.state.ShopsEvent
import com.example.presentation.shops.state.ShopsViewModel
import com.example.presentation.shops.ui.ShopsContent
import java.math.BigInteger


@Composable
fun ShopsScreen(
    onClick: (BigInteger) -> Unit,
    viewModel: ShopsViewModel = hiltViewModel()
) {
    val state by viewModel.shopsState.collectAsStateWithLifecycle()

    ShopsContent(
        state = state,
        onRetry = { viewModel.on(ShopsEvent.StartRequest) },
        onClick = onClick
    )
}
