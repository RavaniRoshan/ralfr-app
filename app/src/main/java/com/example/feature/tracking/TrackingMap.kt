package com.example.feature.tracking

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.OrderStatus
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors

@Composable
fun TrackingMap(
    courierPos: MapPoint,
    courierBearing: Float,
    status: OrderStatus,
    onLocateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Smooth lerp animations for courier position on screen
    val animatedX by animateFloatAsState(
        targetValue = courierPos.x,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "courier_x"
    )
    val animatedY by animateFloatAsState(
        targetValue = courierPos.y,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "courier_y"
    )
    val animatedBearing by animateFloatAsState(
        targetValue = courierBearing,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "courier_bearing"
    )

    // Pulse animation for locator / courier
    val infiniteTransition = rememberInfiniteTransition(label = "map_infinite")
    val radarScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radar_scale"
    )
    val radarAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radar_alpha"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(RappiColors.MapBase)
    ) {
        val densityVal = LocalDensity.current.density
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        // Canvas Map Layer
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRappiMap(widthPx, heightPx)
        }

        // Store Pin Marker (at StorePosition)
        val storeXPx = widthPx * 0.50f
        val storeYPx = heightPx * 0.30f
        Box(
            modifier = Modifier
                .offset(
                    x = (storeXPx / densityVal).dp - 24.dp,
                    y = (storeYPx / densityVal).dp - 48.dp
                )
                .size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 6.dp,
                modifier = Modifier
                    .size(40.dp)
                    .border(1.5.dp, RappiColors.Orange, RoundedCornerShape(12.dp))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = "Store",
                        tint = RappiColors.Orange,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Destination Pin Marker (at DestinationPosition)
        val destXPx = widthPx * 0.38f
        val destYPx = heightPx * 0.85f
        Box(
            modifier = Modifier
                .offset(
                    x = (destXPx / densityVal).dp - 22.dp,
                    y = (destYPx / densityVal).dp - 44.dp
                )
                .size(44.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = CircleShape,
                color = RappiColors.TurboGreenDeep,
                shadowElevation = 6.dp,
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Destination",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Courier Animated Marker (interpolated coordinates)
        val courierXPx = widthPx * animatedX
        val courierYPx = heightPx * animatedY

        Box(
            modifier = Modifier
                .offset(
                    x = (courierXPx / densityVal).dp - 26.dp,
                    y = (courierYPx / densityVal).dp - 26.dp
                )
                .size(52.dp),
            contentAlignment = Alignment.Center
        ) {
            // Radar pulse ring
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .scale(radarScale)
                    .clip(CircleShape)
                    .background(RappiColors.Orange.copy(alpha = radarAlpha))
            )

            // Scooter illustration bubble
            Surface(
                shape = CircleShape,
                color = RappiColors.Orange,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .size(44.dp)
                    .rotate(animatedBearing - 90f) // Orient in direction of movement
                    .border(2.dp, Color.White, CircleShape)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ElectricScooter,
                        contentDescription = "Courier scooter",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Map Floating Badges: "Turbo Express" & "Locate FAB"
        // Turbo Express badge (Top-Left)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 16.dp)
        ) {
            Surface(
                shape = PillShape,
                color = RappiColors.TurboBadgeFill,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .border(1.5.dp, RappiColors.TurboBadgeRing, PillShape)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Text(
                        text = "Turbo Express",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Locate FAB (Top-Right)
        Surface(
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 6.dp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 16.dp)
                .size(48.dp)
                .clip(CircleShape)
                .clickable { onLocateClick() }
                .testTag("locate_fab")
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Locate",
                    tint = RappiColors.Orange,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

private fun DrawScope.drawRappiMap(w: Float, h: Float) {
    // 1. Warm map background fill
    drawRect(color = RappiColors.MapBase, size = Size(w, h))

    // 2. Parks & Greenspaces
    // Parque Santa Barbara / Virrey style greens
    drawRoundRect(
        color = RappiColors.MapGreenspace.copy(alpha = 0.55f),
        topLeft = Offset(w * 0.08f, h * 0.18f),
        size = Size(w * 0.35f, h * 0.16f),
        cornerRadius = CornerRadius(16f, 16f)
    )
    drawRoundRect(
        color = RappiColors.MapGreenspace.copy(alpha = 0.45f),
        topLeft = Offset(w * 0.65f, h * 0.50f),
        size = Size(w * 0.30f, h * 0.22f),
        cornerRadius = CornerRadius(20f, 20f)
    )

    // 3. Water body (Canal del Virrey / Molinos)
    val waterPath = Path().apply {
        moveTo(0f, h * 0.40f)
        cubicTo(w * 0.3f, h * 0.38f, w * 0.7f, h * 0.44f, w, h * 0.42f)
    }
    drawPath(
        path = waterPath,
        color = RappiColors.MapWater,
        style = Stroke(width = 16f, cap = StrokeCap.Round)
    )

    // 4. Street Grid - Pink Casing & Roads
    // Major Avenues (Avenida Carrera 15, Calle 127, Carrera 11)
    val roadStrokeWidth = 24f
    val roadFillWidth = 18f

    val avenuePath1 = Path().apply {
        moveTo(w * 0.50f, 0f)
        lineTo(w * 0.47f, h * 0.62f)
        lineTo(w * 0.38f, h)
    }

    val avenuePath2 = Path().apply {
        moveTo(0f, h * 0.28f)
        lineTo(w, h * 0.34f)
    }

    val avenuePath3 = Path().apply {
        moveTo(0f, h * 0.62f)
        lineTo(w, h * 0.60f)
    }

    val minorRoad1 = Path().apply {
        moveTo(w * 0.22f, 0f)
        lineTo(w * 0.22f, h)
    }
    val minorRoad2 = Path().apply {
        moveTo(w * 0.78f, 0f)
        lineTo(w * 0.78f, h)
    }

    val roads = listOf(avenuePath1, avenuePath2, avenuePath3, minorRoad1, minorRoad2)

    // Draw Pink casing
    roads.forEach { road ->
        drawPath(
            path = road,
            color = RappiColors.MapRoadStroke,
            style = Stroke(width = roadStrokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }

    // Draw White/off-white Road inner fill
    roads.forEach { road ->
        drawPath(
            path = road,
            color = RappiColors.MapRoad,
            style = Stroke(width = roadFillWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }

    // 5. Delivery Route Polyline: Solid black, 6dp (18px)
    val routePolyline = Path().apply {
        moveTo(w * 0.50f, h * 0.30f) // Store
        lineTo(w * 0.49f, h * 0.45f)
        lineTo(w * 0.47f, h * 0.60f)
        lineTo(w * 0.44f, h * 0.72f) // Courier
        lineTo(w * 0.38f, h * 0.85f) // House
    }

    drawPath(
        path = routePolyline,
        color = RappiColors.RoutePolyline,
        style = Stroke(width = 16f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}
