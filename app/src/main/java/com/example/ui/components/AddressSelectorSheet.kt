package com.example.ui.components

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserManager
import com.example.domain.model.Address
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSelectorSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val user by UserManager.user.collectAsState()
    var isAddingNew by remember { mutableStateOf(false) }

    var newLabel by remember { mutableStateOf("") }
    var newAddressLine by remember { mutableStateOf("") }
    var newNeighborhood by remember { mutableStateOf("Bogotá, Colombia") }
    var newInstructions by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { RappiGrabber() },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isAddingNew) "Add New Address" else "Delivery Address",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = RappiColors.TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = RappiColors.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isAddingNew) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newLabel,
                        onValueChange = { newLabel = it },
                        label = { Text("Label (e.g. Home, Office, Gym)") },
                        placeholder = { Text("Home") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RappiColors.Orange,
                            cursorColor = RappiColors.Orange
                        )
                    )

                    OutlinedTextField(
                        value = newAddressLine,
                        onValueChange = { newAddressLine = it },
                        label = { Text("Street Address") },
                        placeholder = { Text("Cra. 15 # 85-30") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RappiColors.Orange,
                            cursorColor = RappiColors.Orange
                        )
                    )

                    OutlinedTextField(
                        value = newInstructions,
                        onValueChange = { newInstructions = it },
                        label = { Text("Delivery details / Notes") },
                        placeholder = { Text("Apartment 301, Ring buzzer") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RappiColors.Orange,
                            cursorColor = RappiColors.Orange
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { isAddingNew = false },
                            colors = ButtonDefaults.buttonColors(containerColor = RappiColors.ChipGreyAlt),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", color = RappiColors.TextPrimary)
                        }

                        Button(
                            onClick = {
                                if (newAddressLine.isNotBlank()) {
                                    UserManager.addNewAddress(
                                        label = if (newLabel.isNotBlank()) newLabel else "Location",
                                        addressLine = newAddressLine,
                                        neighborhood = newNeighborhood,
                                        instructions = newInstructions
                                    )
                                    isAddingNew = false
                                    onDismiss()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RappiColors.Orange),
                            shape = RoundedCornerShape(12.dp),
                            enabled = newAddressLine.isNotBlank(),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Save Address", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    items(user.savedAddresses) { address ->
                        val isSelected = address.id == user.selectedAddressId
                        Card(
                            onClick = {
                                UserManager.selectAddress(address.id)
                                onDismiss()
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) RappiColors.OrangeLight else RappiColors.SurfaceVariant
                            ),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, RappiColors.Orange) else null,
                            modifier = Modifier.fillMaxWidth().testTag("address_card_${address.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) RappiColors.Orange else Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = address.iconEmoji, fontSize = 20.sp)
                                    }

                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = address.label,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = RappiColors.TextPrimary
                                            )
                                            if (address.isDefault) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(PillShape)
                                                        .background(RappiColors.DiscountYellow)
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "Default",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }

                                        Text(
                                            text = address.addressLine,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = RappiColors.TextPrimary,
                                            fontWeight = FontWeight.Medium
                                        )

                                        if (address.instructions.isNotBlank()) {
                                            Text(
                                                text = address.instructions,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = RappiColors.TextSecondary
                                            )
                                        }
                                    }
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(RappiColors.Orange),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Add New Address Button
                Button(
                    onClick = { isAddingNew = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RappiColors.SurfaceVariant,
                        contentColor = RappiColors.Orange
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = RappiColors.Orange
                        )
                        Text(
                            text = "Add new address",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
