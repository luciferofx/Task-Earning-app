package com.example.ui.theme

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

private val GeometricDarkColorScheme = darkColorScheme(
    primary = GeoPrimary,
    onPrimary = GeoPrimaryDark,
    primaryContainer = GeoPrimaryContainer,
    onPrimaryContainer = GeoOnPrimaryContainer,
    secondary = GeoPrimaryContainer,
    onSecondary = GeoPrimaryDark,
    secondaryContainer = GeoSurfaceElevated,
    onSecondaryContainer = GeoPrimary,
    tertiary = GeoGoldAccent,
    background = GeoBgDark,
    onBackground = GeoTextPrimary,
    surface = GeoSurfaceDark,
    onSurface = GeoTextPrimary,
    surfaceVariant = GeoSurfaceElevated,
    onSurfaceVariant = GeoTextMuted,
    outline = GeoBorderDark,
    outlineVariant = GeoBorderMuted,
    error = GeoDangerRed,
    onError = GeoCategoryVideoFg
)

private val GeometricLightColorScheme = lightColorScheme(
    primary = GeoPrimaryDark,
    onPrimary = Color.White,
    primaryContainer = GeoPrimaryContainer,
    onPrimaryContainer = GeoOnPrimaryContainer,
    secondary = GeoPrimaryDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = GeoPrimaryDark,
    tertiary = Color(0xFF7D5260),
    background = LightBg,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightBorder,
    outlineVariant = Color(0xFFCAC4D0),
    error = Color(0xFFB3261E),
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Geometric Balance showcases modern dark aesthetic by default
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> GeometricDarkColorScheme
        else -> GeometricLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
