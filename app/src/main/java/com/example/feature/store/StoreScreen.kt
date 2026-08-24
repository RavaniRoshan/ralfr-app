package com.example.feature.store

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CartManager
import com.example.data.MockDataProvider
import com.example.domain.model.Product
import com.example.domain.model.formatCop
import com.example.ui.components.DiscountBadge
import com.example.ui.components.RappiFloatingBasketBar
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors

@Composable
fun StoreScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTracking: () -> Unit,
    onNavigateToCheckout: () -> Unit = onNavigateToTracking,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val store = MockDataProvider.burgerKingStore
    val combos = MockDataProvider.burgerKingCombos
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Menu", "Most Ordered", "Deals", "Pro Discounts")
    var isDeliverySelected by remember { mutableStateOf(true) }

    val totalCount = CartManager.totalItems
    val totalPrice = CartManager.totalPrice

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = RappiColors.SurfaceMuted,
        bottomBar = {
            if (totalCount > 0) {
                RappiFloatingBasketBar(
                    itemCount = totalCount,
                    totalPrice = totalPrice,
                    onGoToBasket = onNavigateToCheckout,
                    thumbnailEmoji = "🍔"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Hero Store Header Image & Back button
            item {
                StoreHeroHeader(
                    storeName = store.name,
                    onBackClick = onNavigateBack
                )
            }

            // Store Info Card
            item {
                StoreInfoCard(
                    store = store,
                    isDelivery = isDeliverySelected,
                    onToggleDelivery = { isDeliverySelected = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Tabs: Menu / Most Ordered / Deals
            item {
                Surface(
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tabs.indices.toList()) { index ->
                            val isSelected = selectedTabIndex == index
                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(if (isSelected) RappiColors.Orange else RappiColors.ChipGrey)
                                    .clickable { selectedTabIndex = index }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = tabs[index],
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else RappiColors.TextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Deals & Combos Section Header
            item {
                Text(
                    text = "Deals & Combos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = RappiColors.TextPrimary,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            // Combos list
            items(combos) { product ->
                ComboProductItem(
                    product = product,
                    onProductClick = { onProductClick(product) },
                    onAddClick = { CartManager.addProduct(product) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun StoreHeroHeader(
    storeName: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF502314), Color(0xFFD62300))
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White
                    )
                }
            }
        }

        // Center Hero Graphic / Title
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "👑🍔🍟", fontSize = 48.sp)
        }
    }
}

@Composable
private fun StoreInfoCard(
    store: com.example.domain.model.Store,
    isDelivery: Boolean,
    onToggleDelivery: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = store.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TextPrimary
                    )
                    Text(
                        text = store.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = RappiColors.TextSecondary
                    )
                }

                Surface(
                    shape = PillShape,
                    color = RappiColors.CardBgAlt,
                    modifier = Modifier.border(1.dp, RappiColors.Divider, PillShape)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = RappiColors.StarGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${store.rating} (${store.ratingCount})",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Delivery vs Pickup Toggle Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(PillShape)
                    .background(RappiColors.ChipGrey)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(PillShape)
                        .background(if (isDelivery) Color.White else Color.Transparent)
                        .clickable { onToggleDelivery(true) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricScooter,
                            contentDescription = null,
                            tint = if (isDelivery) RappiColors.Orange else RappiColors.TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Home delivery (12 min)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isDelivery) RappiColors.TextPrimary else RappiColors.TextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(PillShape)
                        .background(if (!isDelivery) Color.White else Color.Transparent)
                        .clickable { onToggleDelivery(false) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = null,
                            tint = if (!isDelivery) RappiColors.Orange else RappiColors.TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Pick up in store",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (!isDelivery) RappiColors.TextPrimary else RappiColors.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ComboProductItem(
    product: Product,
    onProductClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onProductClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (product.discountPercent != null) {
                    DiscountBadge(
                        discountPercent = product.discountPercent,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = RappiColors.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = RappiColors.TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = product.price.formatCop(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = RappiColors.TextPrimary
                    )
                    if (product.originalPrice != null) {
                        Text(
                            text = product.originalPrice.formatCop(),
                            style = MaterialTheme.typography.bodySmall,
                            color = RappiColors.TextTertiary,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Product thumbnail & Add CTA
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(RappiColors.CardBgAlt),
                contentAlignment = Alignment.Center
            ) {
                Text(text = product.iconEmoji, fontSize = 42.sp)

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(RappiColors.Success)
                        .clickable { onAddClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
