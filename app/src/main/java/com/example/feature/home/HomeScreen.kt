package com.example.feature.home

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CartManager
import com.example.data.MockDataProvider
import com.example.data.UserManager
import com.example.domain.model.Product
import com.example.domain.model.Store
import com.example.domain.model.formatCop
import com.example.ui.components.AddressSelectorSheet
import com.example.ui.components.RappiBottomNavBar
import com.example.ui.components.RappiFloatingBasketBar
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors

data class HomeTile(
    val id: String,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val bgBrush: Brush,
    val badge: String? = null
)

@Composable
fun HomeScreen(
    onNavigateToTurbo: () -> Unit,
    onNavigateToFruver: () -> Unit,
    onNavigateToStore: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToTracking: () -> Unit,
    onNavigateToOffers: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("home") }
    var showAddressSheet by remember { mutableStateOf(false) }
    val cartItems by CartManager.items.collectAsState()
    val user by UserManager.user.collectAsState()
    val totalCount = CartManager.totalItems
    val totalPrice = CartManager.totalPrice

    val tiles = listOf(
        HomeTile(
            id = "restaurants",
            title = "Restaurants",
            subtitle = "Up to 50% OFF",
            emoji = "🍔",
            bgBrush = Brush.linearGradient(listOf(Color(0xFFFF9E80), Color(0xFFFF6D00))),
            badge = "PROMO"
        ),
        HomeTile(
            id = "turbo",
            title = "Turbo",
            subtitle = "In 10–15 min",
            emoji = "⚡",
            bgBrush = Brush.linearGradient(listOf(Color(0xFF81C784), Color(0xFF2E7D32))),
            badge = "10 MIN"
        ),
        HomeTile(
            id = "fruver",
            title = "Fruver",
            subtitle = "Fresh produce",
            emoji = "🥑",
            bgBrush = Brush.linearGradient(listOf(Color(0xFFAED581), Color(0xFF689F38))),
            badge = "FRESH"
        ),
        HomeTile(
            id = "super",
            title = "Supermarket",
            subtitle = "Everything for home",
            emoji = "🛒",
            bgBrush = Brush.linearGradient(listOf(Color(0xFFFFD54F), Color(0xFFFFA000)))
        ),
        HomeTile(
            id = "pharmacy",
            title = "Pharmacy",
            subtitle = "Health & care",
            emoji = "💊",
            bgBrush = Brush.linearGradient(listOf(Color(0xFF80DEEA), Color(0xFF0097A7)))
        ),
        HomeTile(
            id = "gol",
            title = "¡Grita Gol!",
            subtitle = "Matchday combos",
            emoji = "⚽",
            bgBrush = Brush.linearGradient(listOf(Color(0xFFFF8A80), Color(0xFFD50000))),
            badge = "LIVE"
        ),
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = RappiColors.SurfaceMuted,
        bottomBar = {
            Column {
                if (totalCount > 0) {
                    RappiFloatingBasketBar(
                        itemCount = totalCount,
                        totalPrice = totalPrice,
                        onGoToBasket = onNavigateToCheckout
                    )
                }
                RappiBottomNavBar(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        when (tab) {
                            "home" -> { selectedTab = "home" }
                            "offers" -> onNavigateToOffers()
                            "orders" -> onNavigateToOrders()
                            "account" -> onNavigateToAccount()
                        }
                    },
                    onSearchClick = onNavigateToSearch
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Header: Address & Notifications
            item {
                HomeHeader(
                    addressLabel = user.selectedAddress.label,
                    addressLine = user.selectedAddress.addressLine,
                    neighborhood = user.selectedAddress.cityAndNeighborhood,
                    avatarEmoji = user.avatarEmoji,
                    onAddressClick = { showAddressSheet = true },
                    onNotificationClick = onNavigateToAccount
                )
            }

            // Search Bar Field
            item {
                HomeSearchBar(
                    onSearchClick = onNavigateToSearch,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Live Order Sticky Card (Direct shortcut to Order Tracking Screen)
            item {
                LiveOrderCard(
                    onTrackingClick = onNavigateToTracking,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Dad's the Best & Soccer Hero Banner
            item {
                HeroPromoBanner(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    onBannerClick = onNavigateToStore
                )
            }

            // Main Category Grid (3D Tiles)
            item {
                Text(
                    text = "Explore Categories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = RappiColors.TextPrimary,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp)
                )
            }

            item {
                CategoryGridSection(
                    tiles = tiles,
                    onTileClick = { tileId ->
                        when (tileId) {
                            "turbo" -> onNavigateToTurbo()
                            "fruver" -> onNavigateToFruver()
                            "restaurants" -> onNavigateToStore()
                            "super" -> onNavigateToSearch()
                            "pharmacy" -> onNavigateToSearch()
                            "gol" -> onNavigateToOffers()
                            else -> onNavigateToTurbo()
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Turbo Delivery Section Preview
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "⚡ Turbo",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = RappiColors.TextPrimary
                        )
                        Box(
                            modifier = Modifier
                                .clip(PillShape)
                                .background(RappiColors.TurboGreenDeep)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "10 min",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "See all",
                        style = MaterialTheme.typography.labelMedium,
                        color = RappiColors.Orange,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToTurbo() }
                    )
                }
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(MockDataProvider.turboFeaturedProducts) { product ->
                        ProductHomeCard(
                            product = product,
                            onProductClick = { onProductClick(product) },
                            onAddClick = { CartManager.addProduct(product) }
                        )
                    }
                }
            }

            // Featured Stores Section (Burger King)
            item {
                Text(
                    text = "Featured Stores",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = RappiColors.TextPrimary,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 10.dp)
                )
            }

            item {
                StoreBannerCard(
                    store = MockDataProvider.burgerKingStore,
                    onClick = onNavigateToStore,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    if (showAddressSheet) {
        AddressSelectorSheet(
            onDismiss = { showAddressSheet = false }
        )
    }
}

@Composable
private fun HomeHeader(
    addressLabel: String,
    addressLine: String,
    neighborhood: String,
    avatarEmoji: String,
    onAddressClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Surface(
        color = RappiColors.Surface,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onAddressClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = RappiColors.Orange,
                    modifier = Modifier.size(24.dp)
                )

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$addressLabel · $addressLine",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = RappiColors.TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = RappiColors.TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = neighborhood,
                        style = MaterialTheme.typography.bodySmall,
                        color = RappiColors.TextSecondary
                    )
                }
            }

            // Notification Bell / Profile
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(RappiColors.OrangeLight)
                    .clickable { onNotificationClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = avatarEmoji, fontSize = 20.sp)
            }
        }
    }
}

@Composable
private fun HomeSearchBar(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = PillShape,
        color = RappiColors.SearchBarBg,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable { onSearchClick() }
            .testTag("home_search_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = RappiColors.TextTertiary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Search soda, burgers, fruits, snacks...",
                style = MaterialTheme.typography.bodyMedium,
                color = RappiColors.TextTertiary,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun LiveOrderCard(
    onTrackingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = RappiColors.TurboGreenDeep),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTrackingClick() }
            .testTag("live_order_banner")
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
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(RappiColors.ProgressGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ElectricScooter,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Text(
                        text = "Your order is on the way",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Turbo 06:30 · Brayan Alexander",
                        style = MaterialTheme.typography.bodySmall,
                        color = RappiColors.TurboTextOnDeep
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Track",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = RappiColors.TurboGreenDeep
                )
            }
        }
    }
}

@Composable
private fun HeroPromoBanner(
    modifier: Modifier = Modifier,
    onBannerClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { onBannerClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFFFF5024), Color(0xFFFF8E53))
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(PillShape)
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "⚽ DAD'S CELEBRATION",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Up to 50% OFF in Combos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        lineHeight = 22.sp
                    )

                    Text(
                        text = "Burger King, Turbo & more",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Text(
                    text = "🍔⚽",
                    fontSize = 48.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoryGridSection(
    tiles: List<HomeTile>,
    onTileClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            tiles.take(3).forEach { tile ->
                CategoryTileItem(tile = tile, onClick = { onTileClick(tile.id) }, modifier = Modifier.weight(1f))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            tiles.drop(3).take(3).forEach { tile ->
                CategoryTileItem(tile = tile, onClick = { onTileClick(tile.id) }, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CategoryTileItem(
    tile: HomeTile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .height(105.dp)
            .clickable { onClick() }
            .testTag("category_tile_${tile.id}")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            if (tile.badge != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(PillShape)
                        .background(RappiColors.Orange)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = tile.badge,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = tile.emoji,
                    fontSize = 28.sp
                )

                Column {
                    Text(
                        text = tile.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TextPrimary,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                    Text(
                        text = tile.subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = RappiColors.TextSecondary,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductHomeCard(
    product: Product,
    onProductClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .width(135.dp)
            .clickable { onProductClick() }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(RappiColors.CardBgAlt),
                contentAlignment = Alignment.Center
            ) {
                Text(text = product.iconEmoji, fontSize = 40.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = RappiColors.TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.price.formatCop(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = RappiColors.TextPrimary,
                    fontSize = 13.sp
                )

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(RappiColors.Success)
                        .clickable { onAddClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StoreBannerCard(
    store: Store,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(RappiColors.OrangeLight),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "👑", fontSize = 32.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = store.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TextPrimary
                    )
                }

                Text(
                    text = "${store.category} · ★${store.rating} (${store.ratingCount})",
                    style = MaterialTheme.typography.bodySmall,
                    color = RappiColors.TextSecondary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = "⚡ ${store.deliveryTimeMin} min",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TurboHeaderBg
                    )
                    Text(
                        text = "🛵 FREE Delivery",
                        style = MaterialTheme.typography.labelSmall,
                        color = RappiColors.TextSecondary
                    )
                }
            }
        }
    }
}
