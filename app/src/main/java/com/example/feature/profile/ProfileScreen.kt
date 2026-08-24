package com.example.feature.profile

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserManager
import com.example.domain.model.formatCop
import com.example.ui.components.AddressSelectorSheet
import com.example.ui.components.RappiBottomNavBar
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit,
    onNavigateToOffers: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToTracking: () -> Unit = onNavigateToOrders,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user by UserManager.user.collectAsState()
    var showAddressSheet by remember { mutableStateOf(false) }
    var showSupportDialog by remember { mutableStateOf(false) }
    var showTopupDialog by remember { mutableStateOf(false) }
    var pushNotificationsEnabled by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            RappiBottomNavBar(
                selectedTab = "account",
                onTabSelected = { tab ->
                    when (tab) {
                        "home" -> onNavigateToHome()
                        "offers" -> onNavigateToOffers()
                        "orders" -> onNavigateToOrders()
                        "account" -> {}
                    }
                },
                onSearchClick = onNavigateToSearch
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(RappiColors.Background)
                .padding(innerPadding)
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // 1. User Header Profile Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(RappiColors.OrangeLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = user.avatarEmoji, fontSize = 36.sp)
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = user.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Black,
                                        color = RappiColors.TextPrimary
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(PillShape)
                                            .background(Color(0xFFFFD700))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "PRO",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 9.sp,
                                            color = Color.Black
                                        )
                                    }
                                }

                                Text(
                                    text = user.email,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = RappiColors.TextSecondary
                                )
                                Text(
                                    text = user.phone,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = RappiColors.TextTertiary
                                )
                            }
                        }

                        HorizontalDivider(color = RappiColors.Divider)

                        // Stats counters
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            ProfileStat(label = "Past Orders", value = "28")
                            ProfileStat(label = "Prime Saved", value = "$184.000")
                            ProfileStat(label = "RappiPoints", value = "4.250")
                        }
                    }
                }
            }

            // 2. RappiPrime Plus VIP Banner
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E24)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF1C1C22), Color(0xFF322E18))
                                )
                            )
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = "👑", fontSize = 22.sp)
                                Text(
                                    text = "RappiPrime Plus",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFFD700)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(Color(0xFFFFD700).copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700)
                                )
                            }
                        }

                        Text(
                            text = "Unlimited FREE delivery on Turbo & Restaurants · Includes Max (HBO) streaming · 5% Cashback in RappiCredits",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // 3. RappiPay Wallet Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = "💳", fontSize = 20.sp)
                                Text(
                                    text = "RappiPay & Credits",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = RappiColors.TextPrimary
                                )
                            }

                            Button(
                                onClick = { showTopupDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = RappiColors.OrangeLight),
                                shape = PillShape,
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text(
                                    text = "+ Top Up",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = RappiColors.Orange
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(RappiColors.SurfaceVariant)
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Account Balance",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = RappiColors.TextSecondary
                                    )
                                    Text(
                                        text = user.rappiPayBalance.formatCop(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = RappiColors.TextPrimary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(RappiColors.SurfaceVariant)
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "RappiCredits",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = RappiColors.TextSecondary
                                    )
                                    Text(
                                        text = user.rappiCredits.formatCop(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = RappiColors.Orange
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Menu Actions Section
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 6.dp)) {
                        ProfileMenuItem(
                            icon = Icons.Default.LocationOn,
                            title = "My Addresses",
                            subtitle = "${user.savedAddresses.size} saved addresses",
                            onClick = { showAddressSheet = true }
                        )

                        HorizontalDivider(color = RappiColors.Divider, modifier = Modifier.padding(horizontal = 16.dp))

                        ProfileMenuItem(
                            icon = Icons.Default.CreditCard,
                            title = "Payment Methods",
                            subtitle = "RappiPay, Visa ****4242, Cash",
                            onClick = {}
                        )

                        HorizontalDivider(color = RappiColors.Divider, modifier = Modifier.padding(horizontal = 16.dp))

                        ProfileMenuItem(
                            icon = Icons.Default.History,
                            title = "Order History",
                            subtitle = "View past deliveries and re-order",
                            onClick = onNavigateToOrders
                        )

                        HorizontalDivider(color = RappiColors.Divider, modifier = Modifier.padding(horizontal = 16.dp))

                        ProfileMenuItem(
                            icon = Icons.Default.LocalOffer,
                            title = "Coupons & Promos",
                            subtitle = "Redeem promotional codes",
                            onClick = onNavigateToOffers
                        )

                        HorizontalDivider(color = RappiColors.Divider, modifier = Modifier.padding(horizontal = 16.dp))

                        ProfileMenuItem(
                            icon = Icons.Default.HeadsetMic,
                            title = "Help & Customer Care",
                            subtitle = "24/7 Live Support & FAQs",
                            onClick = { showSupportDialog = true }
                        )
                    }
                }
            }

            // 5. Settings Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsNone,
                                    contentDescription = null,
                                    tint = RappiColors.Orange
                                )
                                Column {
                                    Text(
                                        text = "Push Notifications",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = RappiColors.TextPrimary
                                    )
                                    Text(
                                        text = "Live tracking and promo alerts",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = RappiColors.TextSecondary
                                    )
                                }
                            }

                            Switch(
                                checked = pushNotificationsEnabled,
                                onCheckedChange = { pushNotificationsEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = RappiColors.Orange)
                            )
                        }
                    }
                }
            }

            // 6. Sign Out Action
            item {
                Card(
                    onClick = {
                        UserManager.logout()
                        onLogout()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp),
                    modifier = Modifier.fillMaxWidth().testTag("logout_button")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Log Out",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Log Out of Rappi",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

    if (showAddressSheet) {
        AddressSelectorSheet(
            onDismiss = { showAddressSheet = false }
        )
    }

    if (showSupportDialog) {
        AlertDialog(
            onDismissRequest = { showSupportDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "🛟", fontSize = 24.sp)
                    Text(text = "Rappi Live Support", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Our support agents are available 24/7 to help you with active orders, refunds, and inquiries.")
                    Card(
                        colors = CardDefaults.cardColors(containerColor = RappiColors.OrangeLight),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "⚡ Average response time: < 2 minutes",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = RappiColors.Orange,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSupportDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = RappiColors.Orange)
                ) {
                    Text("Start Live Chat", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSupportDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    if (showTopupDialog) {
        AlertDialog(
            onDismissRequest = { showTopupDialog = false },
            title = { Text("Top Up RappiPay Balance", fontWeight = FontWeight.Bold) },
            text = {
                Text("Select amount to recharge your wallet directly with PSE or Credit Card.")
            },
            confirmButton = {
                Button(
                    onClick = { showTopupDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = RappiColors.Orange)
                ) {
                    Text("Add $50.000 COP", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTopupDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ProfileStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = RappiColors.TextPrimary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = RappiColors.TextSecondary
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(RappiColors.SurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = RappiColors.Orange,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = RappiColors.TextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = RappiColors.TextSecondary
                )
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = RappiColors.TextTertiary,
            modifier = Modifier.size(20.dp)
        )
    }
}
