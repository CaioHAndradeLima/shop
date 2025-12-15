package com.example.shops.route

sealed class ScreenRoute(val route: String) {

    object Shops : ScreenRoute("shops")

    object Shop : ScreenRoute("shop/{id}") {
        fun create(id: String): String = "shop/$id"
    }
}
