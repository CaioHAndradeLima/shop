package com.example.data.shop.remote.api

import com.example.data.shop.remote.dto.ShopDto
import retrofit2.http.GET

internal interface ShopApi {
    @GET("cce3-b92b-42c2-9733")
    suspend fun getShops(): List<ShopDto>
}