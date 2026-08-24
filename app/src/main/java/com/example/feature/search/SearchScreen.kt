package com.example.feature.search

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import com.example.ui.components.ColdBadge
import com.example.ui.components.DiscountBadge
import com.example.ui.components.OutsourcedBadge
import com.example.ui.components.RappiFloatingBasketBar
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors

@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTracking: () -> Unit,
    onNavigateToCheckout: () -> Unit = onNavigateToTracking,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("Soda") }
    var selectedBrand by remember { mutableStateOf("All") }
    val brands = listOf("All", "Coca cola", "Bretaña", "Quatro", "Pony malta", "Canada Dry", "Sprite")

    val totalCount = CartManager.totalItems
    val totalPrice = CartManager.totalPrice

    val filteredProducts = MockDataProvider.sodaProducts.filter { product ->
        val matchesQuery = product.name.contains(searchQuery, ignoreCase = true) || product.brand.contains(searchQuery, ignoreCase = true) || searchQuery.isBlank()
        val matchesBrand = selectedBrand == "All" || product.brand.equals(selectedBrand, ignoreCase = true)
        matchesQuery && matchesBrand
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = RappiColors.SurfaceMuted,
        bottomBar = {
            if (totalCount > 0) {
                RappiFloatingBasketBar(
                    itemCount = totalCount,
                    totalPrice = totalPrice,
                    onGoToBasket = onNavigateToCheckout,
                    thumbnailEmoji = "🍾"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input Header
            item {
                Surface(
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = onNavigateBack) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                            }

                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Search drinks, snacks...") },
                                shape = PillShape,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .testTag("search_input_field"),
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = RappiColors.SearchBarBg,
                                    unfocusedContainerColor = RappiColors.SearchBarBg,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                                        }
                                    }
                                }
                            )
                        }

                        // Brand filter chips
                        LazyRow(
                            contentPadding = PaddingValues(top = 8.dp, bottom = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(brands) { brand ->
                                val isSelected = selectedBrand == brand
                                Box(
                                    modifier = Modifier
                                        .clip(PillShape)
                                        .background(if (isSelected) RappiColors.Orange else RappiColors.ChipGrey)
                                        .clickable { selectedBrand = brand }
                                        .padding(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = brand,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else RappiColors.TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Results count
            item {
                Text(
                    text = "${filteredProducts.size} results found for \"$searchQuery\"",
                    style = MaterialTheme.typography.labelSmall,
                    color = RappiColors.TextSecondary,
                    modifier = Modifier.padding(start = 16.dp, top = 14.dp, bottom = 8.dp)
                )
            }

            // Product results list
            items(filteredProducts) { product ->
                SearchProductItem(
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
private fun SearchProductItem(
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Product image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(RappiColors.CardBgAlt),
                contentAlignment = Alignment.Center
            ) {
                Text(text = product.iconEmoji, fontSize = 42.sp)

                if (product.discountPercent != null) {
                    DiscountBadge(
                        discountPercent = product.discountPercent,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                // Badges row: Cold / Outsourced
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (product.isCold) {
                        ColdBadge()
                    }
                    if (product.isOutsourced) {
                        OutsourcedBadge()
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = RappiColors.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${product.unit} · ${product.unitPriceText}",
                    style = MaterialTheme.typography.bodySmall,
                    color = RappiColors.TextSecondary,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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

            Spacer(modifier = Modifier.width(8.dp))

            // Green add button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(RappiColors.Success)
                    .clickable { onAddClick() }
                    .testTag("add_search_item_${product.id}"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}
