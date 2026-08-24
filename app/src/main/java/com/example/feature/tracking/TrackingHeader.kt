package com.example.feature.tracking

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.OrderStatus
import com.example.domain.model.formatMmSs
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors
import kotlin.time.Duration

@Composable
fun TrackingHeader(
    status: OrderStatus,
    etaRemaining: Duration,
    progressFraction: Float,
    onCloseClick: () -> Unit,
    onHelpClick: () -> Unit,
    onHeaderClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = RappiColors.Surface,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
        ) {
            // Top action row: Close & Help
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Close button: 40x40 circle, ChipGrey
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(RappiColors.ChipGrey)
                        .clickable { onCloseClick() }
                        .testTag("tracking_close_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close tracking",
                        tint = RappiColors.IconDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Help chip: height 40, PillShape, ChipGrey
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .clip(PillShape)
                        .background(RappiColors.ChipGrey)
                        .clickable { onHelpClick() }
                        .padding(horizontal = 18.dp)
                        .testTag("tracking_help_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Help",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = RappiColors.TextTertiary,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Order Status Title (clickable to cycle states for demonstration)
            AnimatedContent(
                targetState = status,
                transitionSpec = {
                    (fadeIn(tween(300)) + slideInVertically { it / 2 })
                        .togetherWith(fadeOut(tween(200)) + slideOutVertically { -it / 2 })
                },
                label = "status_title"
            ) { currentStatus ->
                Text(
                    text = currentStatus.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = RappiColors.TextPrimary,
                    fontSize = 24.sp,
                    lineHeight = 28.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onHeaderClick() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Turbo ETA Pill (62dp height, #083411)
            TurboEtaPill(
                status = status,
                etaRemaining = etaRemaining,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Progress Rail
            TrackingProgressRail(
                status = status,
                progressFraction = progressFraction,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TurboEtaPill(
    status: OrderStatus,
    etaRemaining: Duration,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(62.dp)
            .clip(PillShape)
            .background(RappiColors.TurboGreenDeep)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Turbo logotype
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(RappiColors.TurboBadgeFill),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚡",
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = "Turbo",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    color = RappiColors.TurboTextOnDeep,
                    letterSpacing = (-0.5).sp,
                    fontSize = 22.sp
                )
            }

            // Right: Animated countdown time e.g. "06:30"
            if (status == OrderStatus.Delivered) {
                Text(
                    text = "Delivered ✓",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = RappiColors.TurboTextOnDeep,
                    fontSize = 20.sp
                )
            } else {
                val formattedTime = etaRemaining.formatMmSs()
                AnimatedContent(
                    targetState = formattedTime,
                    transitionSpec = {
                        (slideInVertically(animationSpec = tween(250)) { height -> height / 2 } + fadeIn(tween(250)))
                            .togetherWith(slideOutVertically(animationSpec = tween(200)) { height -> -height / 2 } + fadeOut(tween(200)))
                    },
                    label = "eta_countdown"
                ) { timeText ->
                    Text(
                        text = timeText,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TurboTextOnDeep,
                        fontSize = 32.sp,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Composable
fun TrackingProgressRail(
    status: OrderStatus,
    progressFraction: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction.coerceIn(0f, 1f),
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label = "rail_progress"
    )

    // Pulse animation for the active rider node halo
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (status.isLiveCountdown) 1.14f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rider Node (44dp circle) with Mint Pulse Halo
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            // Mint ring pulse halo
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(RappiColors.ProgressBadgeBg)
            )

            // Green circle
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(RappiColors.ProgressGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ElectricScooter,
                    contentDescription = "Rider node",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Track and fill
        Box(
            modifier = Modifier
                .weight(1f)
                .height(4.dp)
                .clip(PillShape)
                .background(RappiColors.ProgressTrack)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(4.dp)
                    .clip(PillShape)
                    .background(RappiColors.ProgressGreen)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Home destination node (40dp circle, SurfaceMuted #F9F9FB)
        val isAtHome = status == OrderStatus.Arrived || status == OrderStatus.Delivered
        val homeBgColor by animateColorAsState(
            targetValue = if (isAtHome) RappiColors.ProgressGreen else RappiColors.SurfaceMuted,
            label = "home_node_bg"
        )
        val homeIconTint by animateColorAsState(
            targetValue = if (isAtHome) Color.White else RappiColors.TextPrimary,
            label = "home_icon_tint"
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(homeBgColor)
                .border(1.dp, RappiColors.Divider, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Destination home",
                tint = homeIconTint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
