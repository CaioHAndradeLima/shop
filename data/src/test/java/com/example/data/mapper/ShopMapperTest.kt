package com.example.data.mapper

import com.example.data.shop.local.entity.ShopEntity
import com.example.data.shop.mapper.asDomain
import com.example.data.shop.mapper.toDomain
import com.example.data.shop.mapper.toEntity
import com.example.data.shop.remote.dto.ShopDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger

class ShopMapperTest {

    @Test
    fun `ShopDto maps to Domain correctly`() {
        val dto = ShopDto(
            name = "Test Shop",
            description = "Description",
            picture = "https://image.png",
            rating = 4.5,
            address = "Some address",
            coordinates = listOf(10.5, 20.5),
            googleMapsLink = "https://maps",
            website = "https://site"
        )

        val domain = dto.toDomain()

        assertEquals(BigInteger("1052059111212"), domain.id)
        assertEquals("Test Shop", domain.name)
        assertEquals("Description", domain.description)
        assertEquals("https://image.png", domain.picture)
        assertEquals(4.5, domain.rating)
        assertEquals("Some address", domain.address)
        assertEquals(BigDecimal("10.5"), domain.coordinates.latitude)
        assertEquals(BigDecimal("20.5"), domain.coordinates.longitude)
        assertEquals("https://maps", domain.googleMapsLink)
        assertEquals("https://site", domain.website)
    }

    @Test
    fun `ShopDto with null picture maps to Domain correctly`() {
        val dto = ShopDto(
            name = "Shop",
            description = "Desc",
            picture = null,
            rating = 4.0,
            address = "Address",
            coordinates = listOf(1.0, 2.0),
            googleMapsLink = "maps",
            website = "site"
        )

        val domain = dto.toDomain()

        assertNull(domain.picture)
    }

    @Test
    fun `ShopDto maps to Entity correctly`() {
        val dto = ShopDto(
            name = "Entity Shop",
            description = "Desc",
            picture = "https://img",
            rating = 3.5,
            address = "Addr",
            coordinates = listOf(5.0, 6.0),
            googleMapsLink = "maps",
            website = "site"
        )

        val entity = dto.toEntity()

        assertEquals("506011444", entity.id)
        assertEquals("Entity Shop", entity.name)
        assertEquals("Desc", entity.description)
        assertEquals("https://img", entity.picture)
        assertEquals(3.5, entity.rating)
        assertEquals("Addr", entity.address)
        assertEquals(5.0, entity.latitude)
        assertEquals(6.0, entity.longitude)
        assertEquals("maps", entity.googleMapsLink)
        assertEquals("site", entity.website)
    }

    @Test
    fun `ShopEntity maps to Domain correctly`() {
        val entity = ShopEntity(
            id = "456",
            name = "Local Shop",
            description = "Local desc",
            picture = "https://local.img",
            rating = 5.0,
            address = "Local addr",
            latitude = 12.34,
            longitude = 56.78,
            googleMapsLink = "maps",
            website = "site"
        )

        val domain = entity.toDomain()

        assertEquals(BigInteger("456"), domain.id)
        assertEquals("Local Shop", domain.name)
        assertEquals("Local desc", domain.description)
        assertEquals("https://local.img", domain.picture)
        assertEquals(5.0, domain.rating)
        assertEquals("Local addr", domain.address)
        assertEquals(BigDecimal.valueOf(12.34), domain.coordinates.latitude)
        assertEquals(BigDecimal.valueOf(56.78), domain.coordinates.longitude)
        assertEquals("maps", domain.googleMapsLink)
        assertEquals("site", domain.website)
    }

    @Test
    fun `List of ShopDto maps to Domain list correctly`() {
        val list = listOf(
            ShopDto(
                name = "A",
                description = "A desc",
                picture = null,
                rating = 4.0,
                address = "Addr",
                coordinates = listOf(1.0, 2.0),
                googleMapsLink = "maps",
                website = "site"
            ),
            ShopDto(
                name = "B",
                description = "B desc",
                picture = null,
                rating = 3.0,
                address = "Addr2",
                coordinates = listOf(3.0, 4.0),
                googleMapsLink = "maps2",
                website = "site2"
            )
        )

        val result = list.toDomain()

        assertEquals(2, result.size)
        assertEquals(BigInteger("10201644"), result[0].id)
        assertEquals(BigInteger("30401655"), result[1].id)
    }

    @Test
    fun `List of ShopEntity maps to Domain list correctly`() {
        val list = listOf(
            ShopEntity(
                id = "1",
                name = "Shop 1",
                description = "Desc 1",
                picture = null,
                rating = 4.0,
                address = "Addr",
                latitude = 1.0,
                longitude = 2.0,
                googleMapsLink = "maps",
                website = "site"
            )
        )

        val result = list.asDomain()

        assertEquals(1, result.size)
        assertEquals(BigInteger("1"), result.first().id)
    }
}
