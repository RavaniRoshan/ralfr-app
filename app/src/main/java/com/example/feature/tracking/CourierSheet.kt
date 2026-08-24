package com.example.feature.tracking

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ChatMessage
import com.example.domain.model.Courier
import com.example.domain.model.formatCop
import com.example.ui.components.RappiGrabber
import com.example.ui.theme.ChatBubbleIncomingShape
import com.example.ui.theme.ChatBubbleOutgoingShape
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors
import com.example.ui.theme.SheetShape

@Composable
fun CourierSheet(
    courier: Courier,
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onOpenTipModal: () -> Unit,
    onCallCourier: () -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 16.dp, shape = SheetShape),
        color = RappiColors.Surface,
        shape = SheetShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Grabber handle
            RappiGrabber()

            Spacer(modifier = Modifier.height(8.dp))

            // Courier Profile & Tip Row
            CourierProfileRow(
                courier = courier,
                onOpenTipModal = onOpenTipModal
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Chat Messages (Recent preview)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                messages.takeLast(3).forEach { msg ->
                    ChatBubbleItem(message = msg)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Chat & Phone Composer Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Pill Chat text field with trailing send icon
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = "Chat with your Rappi...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = RappiColors.TextTertiary,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("chat_input_field"),
                    shape = PillShape,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = RappiColors.BubbleGrey,
                        unfocusedContainerColor = RappiColors.BubbleGrey,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = RappiColors.Orange
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (inputText.isNotBlank()) {
                                onSendMessage(inputText)
                                inputText = ""
                                focusManager.clearFocus()
                            }
                        }
                    ),
                    trailingIcon = {
                        Box(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (inputText.isNotBlank()) RappiColors.Orange else RappiColors.IconDark)
                                .clickable {
                                    if (inputText.isNotBlank()) {
                                        onSendMessage(inputText)
                                        inputText = ""
                                        focusManager.clearFocus()
                                    }
                                }
                                .testTag("chat_send_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send message",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(16.dp)
                                    .rotate(-20f)
                            )
                        }
                    }
                )

                // Call Phone Action Button (52dp circle)
                Surface(
                    shape = CircleShape,
                    color = RappiColors.ChipGrey,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .clickable { onCallCourier() }
                        .testTag("call_courier_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call courier",
                            tint = RappiColors.IconMuted,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CourierProfileRow(
    courier: Courier,
    onOpenTipModal: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Avatar + Star badge + Courier details
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Courier Avatar with rating badge
            Box(
                modifier = Modifier.size(52.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(RappiColors.OrangeDark),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🛵",
                        fontSize = 24.sp
                    )
                }

                // ★4.4 pill rating badge
                Surface(
                    shape = PillShape,
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .border(1.dp, Color(0xFFEEEEEE), PillShape)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = RappiColors.StarGold,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = "%.1f".format(courier.rating),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = RappiColors.TextPrimary,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            // Courier name and orders / tip info
            Column {
                Text(
                    text = courier.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = RappiColors.TextPrimary,
                    fontSize = 17.sp
                )
                Text(
                    text = "${courier.ordersCount} orders · Tip: ${courier.tipAmount.formatCop()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = RappiColors.TextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        // Right: [Tip +] chip and more menu
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Tip + chip (#EEF0F2 fill)
            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(RappiColors.ChipGreyAlt)
                    .clickable { onOpenTipModal() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("tip_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tip +",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = RappiColors.TextPrimary,
                    fontSize = 13.sp
                )
            }

            // More ⋯ icon
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable { }
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "Options",
                    tint = RappiColors.TextTertiary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatBubbleItem(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isIncoming) Arrangement.Start else Arrangement.End
    ) {
        Column(
            horizontalAlignment = if (message.isIncoming) Alignment.Start else Alignment.End
        ) {
            Box(
                modifier = Modifier
                    .clip(if (message.isIncoming) ChatBubbleIncomingShape else ChatBubbleOutgoingShape)
                    .background(if (message.isIncoming) RappiColors.BubbleGrey else RappiColors.TextPrimary)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (message.isIncoming) RappiColors.TextPrimary else Color.White,
                    fontSize = 14.sp
                )
            }

            Text(
                text = message.timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = RappiColors.TextTertiary,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
            )
        }
    }
}
