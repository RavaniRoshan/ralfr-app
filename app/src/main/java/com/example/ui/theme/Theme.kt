package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val RappiShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small      = RoundedCornerShape(10.dp),
    medium     = RoundedCornerShape(14.dp),
    large      = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

val PillShape       = RoundedCornerShape(percent = 50)
val SheetShape      = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
val ChatBubbleIncomingShape = RoundedCornerShape(18.dp, 18.dp, 18.dp, 6.dp)
val ChatBubbleOutgoingShape = RoundedCornerShape(18.dp, 18.dp, 6.dp, 18.dp)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // disabled for strict brand fidelity
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) RappiDarkColors else RappiLightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = RappiTypography,
        shapes = RappiShapes,
        content = content
    )
}

