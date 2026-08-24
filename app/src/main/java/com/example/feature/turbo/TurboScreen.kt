package com.example.feature.turbo

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
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
fun TurboScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTracking: () -> Unit,
    onNavigateToCheckout: () -> Unit = onNavigateToTracking,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Todo") }
    val categories = listOf("Todo", "Fútbol ⚽", "Frescos 🥑", "Despensa 🍞", "Bebidas 🥤", "Farmacia 💊", "Snacks 🍿")

    val totalCount = CartManager.totalItems
    val totalPrice = CartManager.totalPrice

    val products = when (selectedCategory) {
        "Frescos 🥑" -> MockDataProvider.fruverProducts
        "Bebidas 🥤" -> MockDataProvider.sodaProducts
        else -> MockDataProvider.turboFeaturedProducts + MockDataProvider.sodaProducts
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF0F1B12),
        bottomBar = {
            if (totalCount > 0) {
                RappiFloatingBasketBar(
                    itemCount = totalCount,
                    totalPrice = totalPrice,
                    onGoToBasket = onNavigateToCheckout
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Turbo Header Bar
            item {
                TurboTopBar(onNavigateBack = onNavigateBack)
            }

            // Category Chips Row
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .clip(PillShape)
                                .background(if (isSelected) RappiColors.TurboPillGreen else Color(0xFF1E2E22))
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFFB0C4B4),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Hero Turbo Banner: "Todo tu súper y más EN MINUTOS"
            item {
                TurboHeroBanner(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }

            // Section: Locura Turbo & Ofertas Flash
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PromoPill(
                        title = "Rappi Locura",
                        subtitle = "Up to -50%",
                        bgBrush = Brush.linearGradient(listOf(Color(0xFFFF5252), Color(0xFFFF7A00))),
                        modifier = Modifier.weight(1f)
                    )
                    PromoPill(
                        title = "12 Min Delivery",
                        subtitle = "Guaranteed speed",
                        bgBrush = Brush.linearGradient(listOf(Color(0xFF00C853), Color(0xFF1B5E20))),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Text(
                    text = "Most Popular in Turbo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            // Products Grid
            items(products.chunked(2)) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TurboProductCard(
                        product = pair[0],
                        onProductClick = { onProductClick(pair[0]) },
                        onAddClick = { CartManager.addProduct(pair[0]) },
                        modifier = Modifier.weight(1f)
                    )
                    if (pair.size > 1) {
                        TurboProductCard(
                            product = pair[1],
                            onProductClick = { onProductClick(pair[1]) },
                            onAddClick = { CartManager.addProduct(pair[1]) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun TurboTopBar(onNavigateBack: () -> Unit) {
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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "⚡ Turbo",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = Color.White,
                fontSize = 22.sp
            )
        }

        // 12 min delivery pill
        Box(
            modifier = Modifier
                .clip(PillShape)
                .background(RappiColors.TurboBadgeFill)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "⚡ 12 min",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun TurboHeroBanner(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF1B5E20), Color(0xFF43A047))
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "TODO TU SÚPER Y MÁS",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD54F)
                    )
                    Text(
                        text = "En Minutos",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "From our dark store directly to you",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }

                Text(
                    text = "🥑⚡🛒",
                    fontSize = 36.sp
                )
            }
        }
    }
}

@Composable
private fun PromoPill(
    title: String,
    subtitle: String,
    bgBrush: Brush,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bgBrush)
            .padding(10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 13.sp
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun TurboProductCard(
    product: Product,
    onProductClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2A1E)),
        modifier = modifier.clickable { onProductClick() }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF263829)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = product.iconEmoji, fontSize = 48.sp)

                if (product.discountPercent != null) {
                    DiscountBadge(
                        discountPercent = product.discountPercent,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 13.sp
            )

            Text(
                text = product.unit,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF90A894),
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = product.price.formatCop(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    if (product.originalPrice != null) {
                        Text(
                            text = product.originalPrice.formatCop(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF7A947E),
                            textDecoration = TextDecoration.LineThrough,
                            fontSize = 10.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
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
