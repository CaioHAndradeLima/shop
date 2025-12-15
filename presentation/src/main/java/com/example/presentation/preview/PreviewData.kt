package com.example.presentation.preview

import com.example.shops.domain.entity.Coordinates
import com.example.shops.domain.entity.Shop
import java.math.BigDecimal
import java.math.BigInteger

internal fun shopPreview() = Shop(
    id = BigInteger("1"),
    name = "Sake House Tokyo",
    description = "Traditional sake shop",
    picture = null,
    rating = 4.5,
    address = "123 Sakura Street, Tokyo",
    coordinates = Coordinates(BigDecimal(23.53535), BigDecimal(46.543536)),
    googleMapsLink = "",
    website = "",
)
