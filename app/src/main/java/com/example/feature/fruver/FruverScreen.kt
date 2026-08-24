package com.example.feature.fruver

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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

data class FruverCategory(val id: String, val name: String, val emoji: String)

@Composable
fun FruverScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTracking: () -> Unit,
    onNavigateToCheckout: () -> Unit = onNavigateToTracking,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        FruverCategory("frutas", "Frutas", "🍎"),
        FruverCategory("verduras", "Verduras", "🥦"),
        FruverCategory("tuberculos", "Tubérculos", "🥔"),
        FruverCategory("hierbas", "Hierbas", "🌿"),
        FruverCategory("congelados", "Congelados", "🧊"),
        FruverCategory("preparar", "Para Preparar", "🥗"),
        FruverCategory("packs", "Packs Frutas", "🧺"),
    )
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    val totalCount = CartManager.totalItems
    val totalPrice = CartManager.totalPrice

    val products = when (selectedCategory.id) {
        "verduras" -> MockDataProvider.fruverProducts.filter { it.category == "Verduras" }
        "tuberculos" -> MockDataProvider.fruverProducts.filter { it.category == "Tubérculos" }
        else -> MockDataProvider.fruverProducts
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = RappiColors.Surface,
        bottomBar = {
            if (totalCount > 0) {
                RappiFloatingBasketBar(
                    itemCount = totalCount,
                    totalPrice = totalPrice,
                    onGoToBasket = onNavigateToCheckout,
                    thumbnailEmoji = "🥑"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Top Bar
            Surface(
                color = RappiColors.Surface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }

                    Text(
                        text = "Frutas y Verduras",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TextPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(RappiColors.ChipGrey),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = RappiColors.TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Split Layout: Left Category Rail & Right Product Grid
            Box(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Left Vertical Navigation Rail
                    LazyColumn(
                        modifier = Modifier
                            .width(88.dp)
                            .fillMaxHeight()
                            .background(RappiColors.SurfaceMuted)
                            .border(width = 0.5.dp, color = RappiColors.Divider)
                    ) {
                        items(categories) { cat ->
                            val isSelected = selectedCategory.id == cat.id
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedCategory = cat }
                                    .background(if (isSelected) Color.White else Color.Transparent)
                                    .padding(vertical = 12.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) RappiColors.OrangeLight else RappiColors.ChipGrey),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = cat.emoji, fontSize = 22.sp)
                                }

                                Text(
                                    text = cat.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) RappiColors.Orange else RappiColors.TextSecondary,
                                    fontSize = 10.sp,
                                    maxLines = 2
                                )
                            }
                        }
                    }

                    // Right Products Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(products) { product ->
                            FruverProductCard(
                                product = product,
                                onProductClick = { onProductClick(product) },
                                onAddClick = { CartManager.addProduct(product) }
                            )
                        }
                    }
                }

                // Floating "AISLES" button at bottom right (matches Image 3)
                Surface(
                    shape = PillShape,
                    color = RappiColors.TextPrimary,
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Aisles",
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

@Composable
private fun FruverProductCard(
    product: Product,
    onProductClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick() }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(RappiColors.CardBgAlt),
                contentAlignment = Alignment.Center
            ) {
                Text(text = product.iconEmoji, fontSize = 44.sp)

                if (product.discountPercent != null) {
                    DiscountBadge(
                        discountPercent = product.discountPercent,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = RappiColors.TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp
            )

            Text(
                text = product.unit,
                style = MaterialTheme.typography.labelSmall,
                color = RappiColors.TextSecondary,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = product.price.formatCop(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TextPrimary,
                        fontSize = 13.sp
                    )
                    if (product.originalPrice != null) {
                        Text(
                            text = product.originalPrice.formatCop(),
                            style = MaterialTheme.typography.labelSmall,
                            color = RappiColors.TextTertiary,
                            textDecoration = TextDecoration.LineThrough,
                            fontSize = 9.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
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
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
