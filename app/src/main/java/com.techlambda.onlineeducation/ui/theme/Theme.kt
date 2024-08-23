package com.techlambda.onlineeducation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = blue,
    secondary = blue_light,
    tertiary = blue_dark,
    background = background_dark,
    surfaceContainer = Color.Black,
    secondaryContainer = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = blue,
    secondary = blue_light,
    tertiary = blue_light,
    background = background_light,
    onPrimary = Color.White,
    surfaceContainer = Color.White,
    onSecondaryContainer = Color.Black,
    onSurface = Color.Black,
    surface = Color.White,
    onSurfaceVariant = Color.Black,
    surfaceVariant = Color.White,
)

@Composable
fun VMTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}