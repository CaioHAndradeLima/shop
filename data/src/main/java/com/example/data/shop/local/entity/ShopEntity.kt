package com.example.data.shop.local.entity

import androidx.annotation.VisibleForTesting
import androidx.room.Entity
import androidx.room.PrimaryKey

@VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
@Entity(tableName = "shops")
data class ShopEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val rating: Double,
    val address: String,
    val picture: String?,
    val latitude: Double,
    val longitude: Double,
    val googleMapsLink: String,
    val website: String
)