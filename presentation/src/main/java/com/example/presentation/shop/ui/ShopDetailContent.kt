@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.presentation.shop.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.common_ui.DefaultTopBarComponent
import com.example.common_ui.ProgressComponent
import com.example.common_ui.TryAgainComponent
import com.example.presentation.R
import com.example.presentation.common.error.toErrorText
import com.example.presentation.shop.state.ShopState

@Composable
fun ShopDetailContent(
    state: ShopState,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            DefaultTopBarComponent(
                title = "Shop details",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (state) {

                ShopState.Loading -> {
                    ProgressComponent()
                }

                is ShopState.Show -> {
                    ShopDetailComponent(shop = state.shop)
                }

                is ShopState.Error -> {
                    TryAgainComponent(
                        errorMessage = state.failure.toErrorText(),
                        onTryAgain = onRetry
                    )
                }
            }
        }
    }
}
