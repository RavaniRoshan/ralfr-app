package com.example.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

object RappiMotion {
    val SpringFast   = spring<Float>(dampingRatio = 0.82f, stiffness = Spring.StiffnessMedium)
    val SpringBouncy = spring<Float>(dampingRatio = 0.62f, stiffness = Spring.StiffnessMediumLow)
    val Emphasized   = tween<Float>(500, easing = CubicBezierEasing(0.2f, 0f, 0f, 1f))
    val Standard     = tween<Float>(300, easing = FastOutSlowInEasing)
    val Quick        = tween<Float>(180, easing = LinearOutSlowInEasing)
}
