@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.presentation.shops.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.presentation.shops.state.ShopsState
import com.example.shops.domain.common.DataFailure
import com.example.shops.domain.entity.Coordinates
import com.example.shops.domain.entity.Shop
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.math.BigInteger

class ShopsContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loading_state_shows_progress() {
        composeRule.setContent {
            ShopsContent(
                state = ShopsState.Loading,
                onRetry = {},
                onClick = {}
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
    fun error_state_shows_error_message_and_retry() {
        val failure = DataFailure.Connection

        composeRule.setContent {
            ShopsContent(
                state = ShopsState.Error(failure),
                onRetry = {},
                onClick = {}
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
    fun retry_click_triggers_callback() {
        var retryClicked = false

        composeRule.setContent {
            ShopsContent(
                state = ShopsState.Error(DataFailure.Connection),
                onRetry = { retryClicked = true },
                onClick = {}
            )
        }

        composeRule
            .onNodeWithText("Try again", substring = true)
            .performClick()

        assertEquals(true, retryClicked)
    }

    @Test
    fun show_state_displays_shops() {
        val shops = listOf(
            fakeShop(id = BigInteger.ONE, name = "Coffee Shop"),
            fakeShop(id = BigInteger.TWO, name = "Bakery")
        )

        composeRule.setContent {
            ShopsContent(
                state = ShopsState.Show(shops),
                onRetry = {},
                onClick = {}
            )
        }

        composeRule.onNodeWithText("Coffee Shop").assertIsDisplayed()
        composeRule.onNodeWithText("Bakery").assertIsDisplayed()
    }

    @Test
    fun clicking_shop_calls_onClick_with_correct_id() {
        val shop = fakeShop(id = BigInteger("42"), name = "Coffee Shop")
        var clickedId: BigInteger? = null

        composeRule.setContent {
            ShopsContent(
                state = ShopsState.Show(listOf(shop)),
                onRetry = {},
                onClick = { clickedId = it }
            )
        }

        composeRule
            .onNodeWithText("Coffee Shop")
            .performClick()

        assertEquals(BigInteger("42"), clickedId)
    }
}

private fun fakeShop(
    id: BigInteger,
    name: String
): Shop =
    Shop(
        id = id,
        name = name,
        description = "Description",
        rating = 4.5,
        address = "123 Main Street",
        picture = null,
        coordinates = Coordinates(
            latitude = 23.53535.toBigDecimal(),
            longitude = 46.35352.toBigDecimal(),
        ),
        googleMapsLink = "",
        website = ""
    )
