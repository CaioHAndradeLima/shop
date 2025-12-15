package com.example.shops.domain.entity

import android.net.Uri
import java.math.BigInteger

data class Shop(
    val id: BigInteger,
    val name: String,
    val description: String,
    val picture: String?,
    val rating: Double,
    val address: String,
    val coordinates: Coordinates,
    val googleMapsLink: String,
    val website: String,
)