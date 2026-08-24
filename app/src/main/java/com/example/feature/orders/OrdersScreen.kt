package com.example.feature.orders

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CartManager
import com.example.data.MockDataProvider
import com.example.data.UserManager
import com.example.domain.model.formatCop
import com.example.ui.components.RappiBottomNavBar
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors

@Composable
fun OrdersScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToOffers: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToTracking: () -> Unit,
    onReorderToCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pastOrders by UserManager.pastOrders.collectAsState()

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth().statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "My Orders",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = RappiColors.TextPrimary
                    )

                    Box(
                        modifier = Modifier
                            .clip(PillShape)
                            .background(RappiColors.OrangeLight)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${pastOrders.size} total",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = RappiColors.Orange
                        )
                    }
                }
            }
        },
        bottomBar = {
            RappiBottomNavBar(
                selectedTab = "orders",
                onTabSelected = { tab ->
                    when (tab) {
                        "home" -> onNavigateToHome()
                        "offers" -> onNavigateToOffers()
                        "orders" -> {}
                        "account" -> onNavigateToAccount()
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Active Order Live Tracking Hero
            item {
                Card(
                    onClick = onNavigateToTracking,
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(3.dp),
                    modifier = Modifier.fillMaxWidth().testTag("active_order_card")
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFFFF8F3), Color.White)
                                )
                            )
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(RappiColors.Orange),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ElectricScooter,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Active Live Delivery",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = RappiColors.TextPrimary
                                    )
                                    Text(
                                        text = "Turbo 10 Min · Courier on the way",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = RappiColors.TextSecondary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(RappiColors.DiscountYellow)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "06:30 min",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = RappiColors.TextPrimary
                                )
                            }
                        }

                        // Progress Bar
                        LinearProgressIndicator(
                            progress = { 0.66f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(PillShape),
                            color = RappiColors.Orange,
                            trackColor = RappiColors.ChipGreyAlt
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Courier: Brayan Alexander (4.4 ⭐)",
                                style = MaterialTheme.typography.labelSmall,
                                color = RappiColors.TextSecondary
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Track on Map",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = RappiColors.Orange
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = RappiColors.Orange,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Past Orders Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Past Orders",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TextPrimary
                    )
                    Text(
                        text = "Last 30 days",
                        style = MaterialTheme.typography.labelSmall,
                        color = RappiColors.TextSecondary
                    )
                }
            }

            // List of past orders
            items(pastOrders) { order ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(RappiColors.ChipGreyAlt),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = order.storeEmoji, fontSize = 22.sp)
                                }

                                Column {
                                    Text(
                                        text = order.storeName,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = RappiColors.TextPrimary
                                    )
                                    Text(
                                        text = "${order.dateText} · ${order.id}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = RappiColors.TextSecondary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(RappiColors.LightGreenTag)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = order.statusText,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = RappiColors.DarkGreenText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Text(
                            text = order.itemsSummary,
                            style = MaterialTheme.typography.bodySmall,
                            color = RappiColors.TextPrimary,
                            maxLines = 2
                        )

                        HorizontalDivider(color = RappiColors.Divider)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = order.totalAmount.formatCop(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = RappiColors.TextPrimary
                            )

                            OutlinedButton(
                                onClick = {
                                    // Add sample items to cart and proceed
                                    CartManager.addProduct(MockDataProvider.sodaProducts[0], 1)
                                    onReorderToCheckout()
                                },
                                shape = PillShape,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = RappiColors.Orange)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Replay,
                                        contentDescription = "Re-order",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Re-order",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}
