package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Geometric Balance Theme Palette
val GeoPrimary = Color(0xFFD0BCFF)           // Lilac Lavender Accent
val GeoPrimaryDark = Color(0xFF381E72)       // Deep Royal Purple (Text on GeoPrimary)
val GeoPrimaryContainer = Color(0xFFEADDFF)  // Soft Lavender Pill
val GeoOnPrimaryContainer = Color(0xFF21005D)// Dark Purple text

val GeoBgDark = Color(0xFF1C1B1F)            // Root dark background
val GeoSurfaceDark = Color(0xFF2B2930)       // Primary card surface
val GeoSurfaceElevated = Color(0xFF333138)   // Secondary / anti-fraud banner surface
val GeoNavBg = Color(0xFF211F26)             // Bottom navigation bar
val GeoBorderDark = Color(0xFF49454F)        // Outline / divider
val GeoBorderMuted = Color(0xFF938F99)       // Muted border

val GeoTextPrimary = Color(0xFFE6E1E5)       // Main text
val GeoTextMuted = Color(0xFFCAC4D0)         // Subtitles / captions
val GeoTextWhite = Color(0xFFFFFFFF)         // Bold headers

// Geometric Category Pastel Accents
val GeoCategoryGamingBg = Color(0xFFEADDFF)
val GeoCategoryGamingFg = Color(0xFF381E72)

val GeoCategoryVideoBg = Color(0xFFF2B8B5)
val GeoCategoryVideoFg = Color(0xFF601410)

val GeoCategorySurveyBg = Color(0xFFD0BCFF)
val GeoCategorySurveyFg = Color(0xFF381E72)

val GeoCategorySocialBg = Color(0xFFCBE6FF)
val GeoCategorySocialFg = Color(0xFF003355)

val GeoCategoryDailyBg = Color(0xFFC4EED0)
val GeoCategoryDailyFg = Color(0xFF0A3818)

// Status & Action Colors
val GeoSuccessGreen = Color(0xFF4ADE80)
val GeoSuccessContainer = Color(0xFF2E4C38)
val GeoDangerRed = Color(0xFFF2B8B5)
val GeoGoldAccent = Color(0xFFFFD54F)

// Backward compatible aliases to ensure smooth integration across all components
val PrimaryIndigo = GeoPrimary
val PrimaryIndigoLight = GeoPrimary
val PrimaryIndigoDark = GeoPrimaryDark

val GoldReward = GeoGoldAccent
val GoldRewardLight = Color(0xFFFFE082)
val GoldRewardDark = Color(0xFFFFB300)

val EmeraldSuccess = GeoSuccessGreen
val EmeraldSuccessLight = Color(0xFF86EFAC)
val EmeraldSuccessDark = Color(0xFF16A34A)

val RoseDanger = GeoDangerRed
val VioletAccent = GeoPrimary
val CyanAccent = Color(0xFF7DD3FC)

val DarkBg = GeoBgDark
val DarkSurface = GeoSurfaceDark
val DarkSurfaceVariant = GeoSurfaceElevated
val DarkBorder = GeoBorderDark

val LightBg = Color(0xFFF6F5F8)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFEDEBF0)
val LightBorder = Color(0xFFCAC4D0)

val TextPrimaryDark = GeoTextPrimary
val TextSecondaryDark = GeoTextMuted
val TextPrimaryLight = Color(0xFF1C1B1F)
val TextSecondaryLight = Color(0xFF49454F)
