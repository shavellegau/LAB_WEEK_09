package com.example.lab_week_09.ui.theme // Pastikan ini adalah path yang benar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// --- UI Element for displaying a title ---

/**
 * Displays title text using the theme's onBackground color for high contrast.
 * Uses the TitleText Composable internally.
 * @param text The string content to display.
 */
@Composable
fun OnBackgroundTitleText(text: String) {
    TitleText(
        text = text,
        color = MaterialTheme.colorScheme.onBackground
    )
}

/**
 * Generic title text Composable, using titleLarge style from typography.
 * @param text The string content.
 * @param color The color of the text.
 */
@Composable
fun TitleText(text: String, color: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = color
    )
}

// --- UI Element for displaying an item list ---

/**
 * Displays item list text using the theme's onBackground color.
 * Uses the ItemText Composable internally.
 * @param text The string content to display.
 */
@Composable
fun OnBackgroundItemText(text: String) {
    ItemText(
        text = text,
        color = MaterialTheme.colorScheme.onBackground
    )
}

/**
 * Generic item text Composable, using bodySmall style from typography.
 * @param text The string content.
 * @param color The color of the text.
 */
@Composable
fun ItemText(text: String, color: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = color
    )
}

// --- UI Element for displaying a button ---

/**
 * Creates a standard primary button with white text and dark gray background.
 * Uses the TextButton Composable internally.
 * @param text The text displayed on the button.
 * @param onClick The action to perform when the button is clicked.
 */
@Composable
fun PrimaryTextButton(text: String, onClick: () -> Unit) {
    TextButton(
        text = text,
        textColor = Color.White,
        onClick = onClick
    )
}

/**
 * Generic button Composable, using labelMedium style for button text.
 * @param text The text displayed on the button.
 * @param textColor The color of the text content inside the button.
 * @param onClick The action to perform when the button is clicked.
 */
@Composable
fun TextButton(text: String, textColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp),
        colors = ButtonDefaults
            .buttonColors(
                containerColor = Color.DarkGray, // Warna latar belakang tombol
                contentColor = textColor         // Warna konten (teks)
            )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}