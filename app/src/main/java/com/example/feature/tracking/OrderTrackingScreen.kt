package com.example.feature.tracking

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.OrderStatus
import com.example.ui.components.RappiGrabber
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    viewModel: OrderTrackingViewModel,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var showCallDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = RappiColors.Surface
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Fixed Header with Close, Help, Status title, ETA pill, and Progress rail
                TrackingHeader(
                    status = state.status,
                    etaRemaining = state.etaRemaining,
                    progressFraction = state.progress,
                    onCloseClick = onBackToHome,
                    onHelpClick = { viewModel.setHelpSheetVisible(true) },
                    onHeaderClick = { viewModel.nextStatus() }
                )

                // Map View & Floating elements filling available middle area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    TrackingMap(
                        courierPos = state.courierPosition,
                        courierBearing = state.courierBearing,
                        status = state.status,
                        onLocateClick = {
                            viewModel.triggerLocate()
                            Toast.makeText(context, "Centered on courier", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Order verification pin badge (shown during delivery)
                    Surface(
                        shape = PillShape,
                        color = Color.White,
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, bottom = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = RappiColors.TurboHeaderBg,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "PIN: ${state.deliveryCode}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = RappiColors.TextPrimary
                            )
                        }
                    }

                    // Interactive Status Switcher (Allows cycling all 6 real-world states)
                    Surface(
                        shape = PillShape,
                        color = Color.White.copy(alpha = 0.95f),
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp, bottom = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(
                                onClick = { viewModel.prevStatus() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Previous state",
                                    tint = RappiColors.TextPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Text(
                                text = "State ${state.status.stepIndex}/6",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = RappiColors.Orange
                            )

                            IconButton(
                                onClick = { viewModel.nextStatus() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Next state",
                                    tint = RappiColors.TextPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Bottom Courier Sheet with messages and interactive composer
                CourierSheet(
                    courier = state.courier,
                    messages = state.messages,
                    onSendMessage = { viewModel.sendMessage(it) },
                    onOpenTipModal = { viewModel.setTipModalVisible(true) },
                    onCallCourier = { showCallDialog = true }
                )
            }

            // Tip Bottom Sheet Modal
            if (state.isTipModalOpen) {
                TipModal(
                    courier = state.courier,
                    onTipSelected = { amount ->
                        viewModel.addTip(amount)
                        Toast.makeText(context, "Tip added to ${state.courier.name}!", Toast.LENGTH_SHORT).show()
                    },
                    onDismiss = { viewModel.setTipModalVisible(false) }
                )
            }

            // Help Bottom Sheet Modal
            if (state.isHelpSheetOpen) {
                HelpBottomSheet(
                    onDismiss = { viewModel.setHelpSheetVisible(false) },
                    onAction = { topic ->
                        viewModel.setHelpSheetVisible(false)
                        Toast.makeText(context, "Support ticket opened for: $topic", Toast.LENGTH_LONG).show()
                    }
                )
            }

            // Masked Call Dialog
            if (showCallDialog) {
                AlertDialog(
                    onDismissRequest = { showCallDialog = false },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(RappiColors.OrangeLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                tint = RappiColors.Orange,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    title = {
                        Text(
                            text = "Calling ${state.courier.name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Text(
                            text = "Connecting through Rappi masked proxy to ${state.courier.phoneMasked}. Your personal phone number remains private.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = RappiColors.TextSecondary
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showCallDialog = false
                                Toast.makeText(context, "Calling courier via secure proxy...", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RappiColors.Orange)
                        ) {
                            Text("Call Now")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showCallDialog = false }) {
                            Text("Cancel", color = RappiColors.TextSecondary)
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HelpBottomSheet(
    onDismiss: () -> Unit,
    onAction: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val helpTopics = listOf(
        "I want to change my delivery address",
        "My order is running later than expected",
        "I need to add instructions for the courier",
        "Problem with items in my basket",
        "Cancel my order"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { RappiGrabber() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = "How can we help with your order?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = RappiColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            helpTopics.forEach { topic ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = RappiColors.CardBgAlt,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onAction(topic) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = topic,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = RappiColors.TextPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = RappiColors.TextTertiary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
