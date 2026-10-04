package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
  primary = ChefCoral,
  onPrimary = Color.White,
  primaryContainer = ChefCoralContainer,
  onPrimaryContainer = ChefCoralDark,
  secondary = ChefGreen,
  onSecondary = Color.White,
  secondaryContainer = ChefGreenContainer,
  onSecondaryContainer = ChefGreenDark,
  tertiary = ChefGold,
  onTertiary = Color(0xFF3E2723),
  tertiaryContainer = ChefGoldContainer,
  onTertiaryContainer = Color(0xFFE65100),
  background = ChefCreamBg,
  onBackground = ChefOnSurface,
  surface = ChefSurface,
  onSurface = ChefOnSurface,
  surfaceVariant = ChefSurfaceVariant,
  onSurfaceVariant = ChefOnSurfaceVariant,
  outline = ChefOutline
)

private val DarkColorScheme = darkColorScheme(
  primary = ChefCoralLight,
  onPrimary = Color(0xFF4A1002),
  primaryContainer = ChefCoralDark,
  onPrimaryContainer = Color(0xFFFFDBCF),
  secondary = ChefGreenLight,
  onSecondary = Color(0xFF00390B),
  secondaryContainer = ChefGreenDark,
  onSecondaryContainer = Color(0xFFB9F6CA),
  tertiary = ChefGoldLight,
  onTertiary = Color(0xFF432C00),
  tertiaryContainer = Color(0xFF624300),
  onTertiaryContainer = Color(0xFFFFE082),
  background = Color(0xFF1E1715),
  onBackground = Color(0xFFEDE0DA),
  surface = Color(0xFF28201D),
  onSurface = Color(0xFFEDE0DA),
  surfaceVariant = Color(0xFF3B2E28),
  onSurfaceVariant = Color(0xFFD6C4BD),
  outline = Color(0xFF70615A)
)

@Composable
fun KidsFoodChefTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      if (window != null) {
        window.statusBarColor = colorScheme.background.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
      }
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
