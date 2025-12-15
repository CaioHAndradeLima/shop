package com.example.shops.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.presentation.shop.ShopDetailScreen
import com.example.presentation.shops.ShopsScreen

@Composable
fun SetupNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Shops.route
    ) {
        composable(ScreenRoute.Shops.route) {
            ShopsScreen(
                onClick = { shopId ->
                    navController.navigate(ScreenRoute.Shop.create(shopId.toString()))
                }
            )
        }

        composable(
            route = ScreenRoute.Shop.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) {
            ShopDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
