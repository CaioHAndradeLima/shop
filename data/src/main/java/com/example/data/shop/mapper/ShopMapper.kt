package com.example.data.shop.mapper

import com.example.data.shop.local.entity.ShopEntity
import com.example.data.shop.remote.dto.ShopDto
import com.example.shops.domain.entity.Coordinates
import com.example.shops.domain.entity.Shop
import java.math.BigDecimal
import java.math.BigInteger

internal fun ShopDto.toDomain(): Shop {
    return Shop(
        id = BigInteger(id.toString()),
        name = name,
        description = description,
        picture = picture,
        rating = rating,
        address = address,
        coordinates = Coordinates(
            latitude = BigDecimal(coordinates.first()),
            longitude = BigDecimal(coordinates.last()),
        ),
        googleMapsLink = googleMapsLink,
        website = website,
    )
}

internal fun ShopDto.toEntity(): ShopEntity {
    return ShopEntity(
        id = id.toString(),
        name = name,
        description = description,
        picture = picture,
        rating = rating,
        address = address,
        latitude = coordinates.first(),
        longitude = coordinates.last(),
        googleMapsLink = googleMapsLink,
        website = website
    )
}

internal fun ShopEntity.toDomain(): Shop {
    return Shop(
        id = BigInteger(id),
        name = name,
        description = description,
        picture = picture,
        rating = rating,
        address = address,
        coordinates = Coordinates(
            latitude = BigDecimal.valueOf(latitude),
            longitude = BigDecimal.valueOf(longitude)
        ),
        googleMapsLink = googleMapsLink,
        website = website,
    )
}

internal fun List<ShopDto>.toDomain(): List<Shop> = map { it.toDomain() }
internal fun List<ShopEntity>.asDomain(): List<Shop> = map { it.toDomain() }
