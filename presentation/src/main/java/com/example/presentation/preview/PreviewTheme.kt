package com.example.presentation.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

@Composable
fun PreviewTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme {
        Surface {
            content()
        }
    }
}
