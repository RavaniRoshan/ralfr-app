package com.example.feature.product

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Product
import com.example.domain.model.formatCop
import com.example.ui.components.ColdBadge
import com.example.ui.components.DiscountBadge
import com.example.ui.components.QuantityStepper
import com.example.ui.components.RappiGrabber
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailSheet(
    product: Product,
    onAddToCart: (Product, Int, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var quantity by remember { mutableIntStateOf(1) }
    var selectedSize by remember { mutableStateOf(product.sizes.firstOrNull() ?: "1.5 L") }
    var selectedPack by remember { mutableStateOf(product.packSizes.firstOrNull() ?: "x1") }

    val packMultiplier = when (selectedPack) {
        "x2" -> 2
        "x6" -> 6
        else -> 1
    }
    val itemTotal = product.price * quantity * packMultiplier

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { RappiGrabber() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 4.dp)
        ) {
            // Action bar: Close & Share / Favorite
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
                Row {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                    }
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    }
                }
            }

            // Hero Product Visual Showcase
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(RappiColors.CardBgAlt),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = product.iconEmoji,
                    fontSize = 96.sp
                )

                if (product.isCold) {
                    ColdBadge(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    )
                }

                if (product.discountPercent != null) {
                    DiscountBadge(
                        discountPercent = product.discountPercent,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Brand & Title
            if (product.brand.isNotBlank()) {
                Text(
                    text = product.brand.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = RappiColors.Orange,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = RappiColors.TextPrimary,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Pricing Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = product.price.formatCop(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = RappiColors.TextPrimary
                )

                if (product.originalPrice != null) {
                    Text(
                        text = product.originalPrice.formatCop(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = RappiColors.TextTertiary,
                        textDecoration = TextDecoration.LineThrough
                    )
                }

                if (product.unitPriceText.isNotBlank()) {
                    Text(
                        text = product.unitPriceText,
                        style = MaterialTheme.typography.bodySmall,
                        color = RappiColors.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenido / Size options
            Text(
                text = "Size / Contenido",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = RappiColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                product.sizes.forEach { sizeOption ->
                    val isSelected = selectedSize == sizeOption
                    Surface(
                        shape = PillShape,
                        color = if (isSelected) RappiColors.OrangeLight else RappiColors.CardBgAlt,
                        modifier = Modifier
                            .clip(PillShape)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) RappiColors.Orange else RappiColors.Divider,
                                shape = PillShape
                            )
                            .clickable { selectedSize = sizeOption }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = sizeOption,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) RappiColors.Orange else RappiColors.TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pack size options
            Text(
                text = "Pack Presentation",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = RappiColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                product.packSizes.forEach { packOption ->
                    val isSelected = selectedPack == packOption
                    Surface(
                        shape = PillShape,
                        color = if (isSelected) RappiColors.OrangeLight else RappiColors.CardBgAlt,
                        modifier = Modifier
                            .clip(PillShape)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) RappiColors.Orange else RappiColors.Divider,
                                shape = PillShape
                            )
                            .clickable { selectedPack = packOption }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = packOption,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) RappiColors.Orange else RappiColors.TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = "Product Details",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = RappiColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                color = RappiColors.TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom CTA Row: Quantity Stepper + Green Add Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                QuantityStepper(
                    quantity = quantity,
                    onIncrement = { quantity++ },
                    onDecrement = { if (quantity > 1) quantity-- }
                )

                Button(
                    onClick = {
                        onAddToCart(product, quantity, selectedSize, selectedPack)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RappiColors.Success,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .testTag("add_product_to_cart_cta")
                ) {
                    Text(
                        text = "Add ${itemTotal.formatCop()}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}
