package com.example.feature.deals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CartManager
import com.example.data.MockDataProvider
import com.example.data.UserManager
import com.example.domain.model.Product
import com.example.domain.model.formatCop
import com.example.ui.components.DiscountBadge
import com.example.ui.components.RappiBottomNavBar
import com.example.ui.components.RappiFloatingBasketBar
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors
import kotlinx.coroutines.launch

data class PromoCoupon(
    val code: String,
    val title: String,
    val description: String,
    val discountText: String,
    val emoji: String = "🎟️"
)

@Composable
fun DealsScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val cartItems by CartManager.items.collectAsState()
    val appliedCoupon by UserManager.appliedPromoCode.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val availableCoupons = listOf(
        PromoCoupon("FUTBOL50", "¡Grita Gol! 20% OFF", "Valid on restaurants & beverages during matchday", "$10.000 max", "⚽"),
        PromoCoupon("RAPPIYA", "Welcome $5.000 OFF", "First order discount on any Turbo or Supermarket order", "$5.000 OFF", "🎉"),
        PromoCoupon("TURBO10", "Turbo Speed $4.000 OFF", "Fast delivery guaranteed in 10 minutes", "$4.000 OFF", "⚡"),
        PromoCoupon("PRIMEVIP", "VIP Supermarket Discount", "Exclusive coupon for active Prime members", "$6.000 OFF", "👑")
    )

    val discountedProducts = listOf(
        MockDataProvider.burgerKingCombos[0],
        MockDataProvider.burgerKingCombos[1],
        MockDataProvider.sodaProducts[0],
        MockDataProvider.sodaProducts[1],
        MockDataProvider.fruverProducts[0],
        MockDataProvider.fruverProducts[2]
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "🔥", fontSize = 24.sp)
                        Text(
                            text = "Promos & Deals",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = RappiColors.TextPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(PillShape)
                            .background(RappiColors.DiscountYellow)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Up to 50% OFF",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = RappiColors.TextPrimary
                        )
                    }
                }
            }
        },
        bottomBar = {
            Column {
                if (cartItems.isNotEmpty()) {
                    RappiFloatingBasketBar(
                        itemCount = CartManager.totalItems,
                        totalPrice = CartManager.totalPrice,
                        onGoToBasket = onNavigateToCheckout
                    )
                }

                RappiBottomNavBar(
                    selectedTab = "offers",
                    onTabSelected = { tab ->
                        when (tab) {
                            "home" -> onNavigateToHome()
                            "offers" -> {}
                            "orders" -> onNavigateToOrders()
                            "account" -> onNavigateToAccount()
                        }
                    },
                    onSearchClick = onNavigateToSearch
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(RappiColors.Background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Matchday Hero Banner
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0D3B66)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF0F4C81), Color(0xFF072A4A), Color(0xFF0F1E2C))
                                )
                            )
                            .padding(18.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(PillShape)
                                        .background(RappiColors.DiscountYellow)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "¡GRITA GOL!",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = Color.Black,
                                        fontSize = 11.sp
                                    )
                                }

                                Text(
                                    text = "Matchday Special ⚽",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Text(
                                text = "Combos with 50% OFF for the match!",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                lineHeight = 26.sp
                            )

                            Text(
                                text = "Burgers, Cold Sodas, Snacks & Beer delivered in 10 mins.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            // Coupons & Promo Vouchers Carousel
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Available Coupons",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TextPrimary
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(end = 8.dp)
                    ) {
                        items(availableCoupons) { coupon ->
                            val isApplied = appliedCoupon == coupon.code
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isApplied) RappiColors.OrangeLight else Color.White
                                ),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.width(260.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(text = coupon.emoji, fontSize = 20.sp)
                                            Text(
                                                text = coupon.code,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Black,
                                                color = RappiColors.Orange
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .clip(PillShape)
                                                .background(RappiColors.DiscountYellow)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = coupon.discountText,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }

                                    Text(
                                        text = coupon.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = RappiColors.TextPrimary
                                    )

                                    Text(
                                        text = coupon.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = RappiColors.TextSecondary,
                                        maxLines = 2
                                    )

                                    Button(
                                        onClick = {
                                            if (isApplied) {
                                                UserManager.removePromoCode()
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Coupon removed")
                                                }
                                            } else {
                                                UserManager.applyPromoCode(coupon.code)
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Coupon '${coupon.code}' applied to checkout!")
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isApplied) RappiColors.Success else RappiColors.Orange
                                        ),
                                        shape = PillShape,
                                        modifier = Modifier.fillMaxWidth().height(36.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            if (isApplied) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Text(
                                                text = if (isApplied) "Applied to Basket" else "Apply Coupon",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Flash Deals Product Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Flash Discount Products",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TextPrimary
                    )

                    Text(
                        text = "Ends in 03:45:12",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.Orange
                    )
                }
            }

            // Discount Products Grid
            items(discountedProducts.chunked(2)) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { product ->
                        Card(
                            onClick = { onProductClick(product) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(RappiColors.ChipGreyAlt),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = product.iconEmoji, fontSize = 52.sp)

                                    product.discountPercent?.let { disc ->
                                        DiscountBadge(
                                            discountPercent = disc,
                                            modifier = Modifier.align(Alignment.TopStart).padding(6.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RappiColors.TextPrimary,
                                    maxLines = 2
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = product.price.formatCop(),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Black,
                                        color = RappiColors.TextPrimary
                                    )
                                    product.originalPrice?.let { orig ->
                                        Text(
                                            text = orig.formatCop(),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = RappiColors.TextTertiary,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    }
                                }

                                Button(
                                    onClick = { CartManager.addProduct(product, 1) },
                                    colors = ButtonDefaults.buttonColors(containerColor = RappiColors.Orange),
                                    shape = PillShape,
                                    modifier = Modifier.fillMaxWidth().height(36.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Add",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text("Add", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}
