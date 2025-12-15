@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.presentation.shops.ui

import android.content.res.Configuration
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.example.common_ui.DefaultTopBarComponent
import com.example.common_ui.ProgressComponent
import com.example.common_ui.TryAgainComponent
import com.example.presentation.common.error.toErrorText
import com.example.presentation.shops.state.ShopsState
import java.math.BigInteger

@Composable
fun ShopsContent(
    state: ShopsState,
    onRetry: () -> Unit,
    onClick: (BigInteger) -> Unit
) {
    val configuration = LocalConfiguration.current
    val columns = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

    Scaffold(
        topBar = { DefaultTopBarComponent() }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {

            when (state) {

                ShopsState.Loading -> {
                    ProgressComponent()
                }

                is ShopsState.Error -> {
                    val message = state.failure.toErrorText()
                    TryAgainComponent(
                        errorMessage = message,
                        onTryAgain = onRetry
                    )
                }

                is ShopsState.Show -> {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(columns)
                    ) {
                        items(
                            items = state.shops,
                            key = { it.id }
                        ) { shop ->
                            ShopItemComponent(
                                shop = shop,
                                onItemClick = { onClick(shop.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
