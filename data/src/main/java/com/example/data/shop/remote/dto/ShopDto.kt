package com.example.data.shop.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigInteger
import kotlin.math.absoluteValue


data class ShopDto(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("picture") val picture: String?,
    @SerializedName("rating") val rating: Double,
    @SerializedName("address") val address: String,
    @SerializedName("coordinates") val coordinates: Collection<Double>,
    @SerializedName("google_maps_link") val googleMapsLink: String,
    @SerializedName("website") val website: String,
) {
    internal val id: BigInteger
        get() = BigInteger(
            coordinates.first().absoluteValue.toString().replace(".", "") +
                    coordinates.last().absoluteValue.toString().replace(".", "") +
            name.length.toString() +
            description.length.toString() +
            website.length.toString() +
            googleMapsLink.length.toString()
        )
}