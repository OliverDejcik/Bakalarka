package com.example.bakalarka.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ================================================================
// 🌞🌚  BACKGROUND COLORS
// ================================================================

// 🌞 LIGHT MODE
val prBackgroundLight      = Color(0xFFEFF3F9)   // celé pozadie appky
val prOnBackgroundLight    = Color(0xFF1A1A1A)   // text na pozadí

// 🌚 DARK MODE
val prBackgroundDark       = Color(0xFF0A0F1A)   // celé pozadie v dark mode
val prOnBackgroundDark     = Color(0xFFFFFFFF)   // text na dark pozadí


// ================================================================
// 🌞🌚  SURFACE COLORS  tu pis komponenty ktore to pouzivaju
// ================================================================

// 🌞 LIGHT MODE
val prSurfaceLight         = Color(0xFFFFFFFF)   // surface
val prOnSurfaceLight       = Color(0xFF1A1A1A)   // text / ikony na surface

// 🌚 DARK MODE
val prSurfaceDark          = Color(0xFF111827)   // surface dark
val prOnSurfaceDark        = Color(0xFFFFFFFF)   // text / ikony na surface dark


// ================================================================
// 🌞🌚  PRIMARY COLORS NAV BARS
// ================================================================

// 🌞 LIGHT MODE
val prPrimaryLight         = Color(0xFF8A2BFF)   // primary
val prOnPrimaryLight       = Color(0xFFFFFFFF)   // text / ikony na primary

// 🌚 DARK MODE
val prPrimaryDark          = Color(0xFFD19CFF)   // primary dark
val prOnPrimaryDark        = Color(0xFF000000)   // text / ikony na primary dark


// ================================================================
// 🌞🌚  SECONDARY COLORS
// ================================================================

// 🌞 LIGHT MODE
val prSecondaryLight       = Color(0xFFC26BFF)   // secondary
val prOnSecondaryLight     = Color(0xFF1A1A1A)   // text / ikony na secondary

// 🌚 DARK MODE
val prSecondaryDark        = Color(0xFFE3B8FF)   // secondary dark
val prOnSecondaryDark      = Color(0xFF000000)   // text / ikony na secondary dark


// ================================================================
// 🌞🌚  TERTIARY COLORS
// ================================================================

// 🌞 LIGHT MODE
val prTertiaryLight        = Color(0xFF6A1BFF)   // tertiary
val prOnTertiaryLight      = Color(0xFFFFFFFF)   // text / ikony na tertiary

// 🌚 DARK MODE
val prTertiaryDark         = Color(0xFFF0D4FF)   // tertiary dark
val prOnTertiaryDark       = Color(0xFF1A1A1A)   // text / ikony na tertiary dark






// LIGHT SCHEME
val LightColors = lightColorScheme(
    primary = prPrimaryLight,
    onPrimary = prOnPrimaryLight,

    secondary = prSecondaryLight,
    onSecondary = prOnSecondaryLight,

    background = prBackgroundLight,
    onBackground = prOnBackgroundLight,

    surface = prSurfaceLight,
    onSurface = prOnSurfaceLight,

    tertiary = prTertiaryLight,
    onTertiary = prOnTertiaryLight,
)

// DARK SCHEME
val DarkColors = darkColorScheme(
    primary = prPrimaryDark,
    onPrimary = prOnPrimaryDark,

    secondary = prSecondaryDark,
    onSecondary = prOnSecondaryDark,

    background = prBackgroundDark,
    onBackground = prOnBackgroundDark,

    surface = prSurfaceDark,
    onSurface = prOnSurfaceDark,

    tertiary = prTertiaryDark,
    onTertiary = prOnTertiaryDark,
)
