package com.example.doosen.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat

// 🎨 Warna-warna custom Islamic Emerald
val EmeraldGreen = Color(0xFF00695C)
val LightMint = Color(0xFFB2DFDB)
val LightBackground = Color(0xFFF9FDFD)
val AccentPurple = Color(0xFF4B00A0)
val AccentPink = Color(0xFF8A00C8)
val TextDark = Color(0xFF212121)
val TextLight = Color.White

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldGreen,
    onPrimary = Color.White,
    secondary = AccentPurple,
    onSecondary = Color.White,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldGreen,
    onPrimary = Color.White,
    secondary = LightMint,
    onSecondary = Color.Black,
    background = LightBackground,
    surface = Color.White,
    onBackground = TextDark,
    onSurface = TextDark,
    error = Color(0xFFB00020),
    onError = Color.White
)

@Composable
fun DOOSENTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // ⛔ Matikan dynamic agar tetap emerald
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
        window.statusBarColor = colorScheme.primary.toArgb()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // gunakan default dulu, bisa disesuaikan nanti
        content = content
    )
}
