package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PurpleGradStart,
    secondary = PurpleGradEnd,
    tertiary = AccentPurple,
    background = PremiumObsidian,
    surface = PremiumObsidian,
    surfaceVariant = PremiumCardBg,
    onPrimary = PureWhite,
    onSecondary = PureWhite,
    onBackground = PureWhite,
    onSurface = PureWhite,
    error = ErrorColor
)

private val LightColorScheme = lightColorScheme(
    primary = PurpleGradStart,
    secondary = PurpleGradEnd,
    tertiary = AccentPurple,
    background = PureWhite,
    surface = PureWhite,
    surfaceVariant = CardLight,
    onPrimary = PureWhite,
    onSecondary = PureWhite,
    onBackground = SlateGrey,
    onSurface = SlateGrey,
    error = ErrorColor
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Set false to prioritize premium purple brand identity unless user opts in
    content: @Composable () -> Unit,
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
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
