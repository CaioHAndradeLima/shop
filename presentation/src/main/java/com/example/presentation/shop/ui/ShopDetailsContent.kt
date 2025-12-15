package com.example.presentation.shop.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.common_ui.RatingRow
import com.example.presentation.R
import com.example.shops.domain.entity.Shop

@Composable
fun ShopDetailsContent(
    shop: Shop,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = shop.name,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        RatingRow(rating = shop.rating)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = shop.description,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = shop.address,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_VIEW, shop.googleMapsLink.toUri())
                context.startActivity(intent)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, shop.website.toUri())
                context.startActivity(intent)
            }
        ) {
            Text(text = stringResource(R.string.visit_website))
        }
    }
}
