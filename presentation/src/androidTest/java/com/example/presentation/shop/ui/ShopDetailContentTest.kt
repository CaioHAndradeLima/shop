@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.presentation.shop.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.presentation.shop.state.ShopState
import com.example.shops.domain.common.DataFailure
import com.example.shops.domain.entity.Coordinates
import com.example.shops.domain.entity.Shop
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.math.BigInteger

class ShopDetailContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loading_state_shows_progress() {
        composeRule.setContent {
            ShopDetailContent(
                state = ShopState.Loading,
                onBack = {},
                onRetry = {}
            )
        }

        composeRule
            .onNodeWithTag("loading_indicator")
            .assertIsDisplayed()

        composeRule
            .onNode(hasProgressBarRangeInfo(rangeInfo = ProgressBarRangeInfo.Indeterminate))
            .assertIsDisplayed()
    }

    @Test
    fun show_state_displays_shop_details() {
        val shop = fakeShop(name = "Coffee Shop")

        composeRule.setContent {
            ShopDetailContent(
                state = ShopState.Show(shop),
                onBack = {},
                onRetry = {}
            )
        }

        composeRule
            .onNodeWithText("Coffee Shop")
            .assertIsDisplayed()
    }

    @Test
    fun error_state_shows_error_message_and_retry() {
        val failure = DataFailure.Connection

        composeRule.setContent {
            ShopDetailContent(
                state = ShopState.Error(failure),
                onBack = {},
                onRetry = {}
            )
        }

        composeRule
            .onNodeWithText("Check your internet connection")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Try again", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun retry_click_triggers_onRetry() {
        var retryCalled = false

        composeRule.setContent {
            ShopDetailContent(
                state = ShopState.Error(DataFailure.Connection),
                onBack = {},
                onRetry = { retryCalled = true }
            )
        }

        composeRule
            .onNodeWithText("Try again", substring = true)
            .performClick()

        assertTrue(retryCalled)
    }

    @Test
    fun back_button_calls_onBack() {
        var backCalled = false

        composeRule.setContent {
            ShopDetailContent(
                state = ShopState.Show(fakeShop()),
                onBack = { backCalled = true },
                onRetry = {}
            )
        }

        composeRule
            .onNodeWithContentDescription("Back")
            .performClick()

        assertTrue(backCalled)
    }
}

private fun fakeShop(
    name: String = "Shop name"
): Shop =
    Shop(
        id = BigInteger.ONE,
        name = name,
        description = "Description",
        rating = 4.5,
        address = "123 Main Street",
        picture = null,
        coordinates = Coordinates(
            latitude = 23.53535.toBigDecimal(),
            longitude = 46.35352.toBigDecimal()
        ),
        googleMapsLink = "",
        website = ""
    )
