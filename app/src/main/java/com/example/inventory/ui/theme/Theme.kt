package com.example.inventory.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.inventory.R

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFF27C38),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF29849),
    onPrimaryContainer = Color(0xFF000000),
    secondary = Color(0xFF8C745E),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF2F2F2),
    onSecondaryContainer = Color(0xFF000000),
    tertiary = Color(0xFF0487D9),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF2F2F2),
    onTertiaryContainer = Color(0xFF000000),
    error = Color.Red,
    errorContainer = Color(0xFFF2F2F2),
    onError = Color.White,
    onErrorContainer = Color.Red,
    background = Color(0xFFF2F2F2),
    onBackground = Color.Black,
    surface = Color(0xFFF2F2F2),
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF2F2F2),
    onSurfaceVariant = Color(0xFF000000),
    outline = Color(0xFFC4C4C4),
    inverseOnSurface = Color.White,
    inverseSurface = Color.Black,
    inversePrimary = Color(0xFFF27C38),
    surfaceTint = Color(0xFFF27C38),
    outlineVariant = Color(0xFFC4C4C4),
    scrim = Color(0x80000000)
)

private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

// Defina a família de fontes
val pawPrintFontFamily = FontFamily(
    Font(R.font.paw_print) // Substitua pelo nome do seu arquivo de fonte
)

private val Typography = Typography(
    // Título grande (por exemplo, título principal da tela)
    displayLarge = TextStyle(
        fontFamily = pawPrintFontFamily,
        fontSize = 57.sp,
        fontWeight = FontWeight.Bold,   // Negrito
        lineHeight = 64.sp
    ),


)




@Composable
fun InventoryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // Dynamic color in this app is turned off for learning purposes
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> LightColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat
                .getInsetsController(window, view)
                .isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes,
        content = content,
        typography = Typography,
    )
}
