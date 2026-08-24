package com.example.feature.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CartManager
import com.example.data.UserManager
import com.example.domain.model.PaymentType
import com.example.domain.model.formatCop
import com.example.ui.components.AddressSelectorSheet
import com.example.ui.components.RappiButton
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors

@Composable
fun CheckoutScreen(
    onNavigateBack: () -> Unit,
    onOrderPlaced: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cartItems by CartManager.items.collectAsState()
    val user by UserManager.user.collectAsState()
    val paymentMethods by UserManager.paymentMethods.collectAsState()
    val selectedPaymentId by UserManager.selectedPaymentMethodId.collectAsState()
    val appliedCoupon by UserManager.appliedPromoCode.collectAsState()

    var showAddressSheet by remember { mutableStateOf(false) }
    var selectedSpeed by remember { mutableStateOf("turbo") } // "turbo" or "standard"
    var selectedTip by remember { mutableStateOf(2000) } // tip in COP
    var promoInput by remember { mutableStateOf("") }
    var promoError by remember { mutableStateOf(false) }
    var isPlacingOrder by remember { mutableStateOf(false) }

    val subtotal = CartManager.totalPrice
    val deliveryFee = if (user.isPrime) 0 else if (selectedSpeed == "turbo") 3900 else 2500
    val serviceFee = 1200
    val discount = UserManager.getDiscountAmount(subtotal)
    val grandTotal = (subtotal + deliveryFee + serviceFee + selectedTip - discount).coerceAtLeast(0)

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 3.dp,
                modifier = Modifier.fillMaxWidth().statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("checkout_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = RappiColors.TextPrimary
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Checkout",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = RappiColors.TextPrimary
                        )
                        Text(
                            text = "${CartManager.totalItems} items · ${user.selectedAddress.addressLine}",
                            style = MaterialTheme.typography.labelSmall,
                            color = RappiColors.TextSecondary
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 16.dp,
                modifier = Modifier.fillMaxWidth().navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total to Pay",
                                style = MaterialTheme.typography.labelSmall,
                                color = RappiColors.TextSecondary
                            )
                            Text(
                                text = grandTotal.formatCop(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Black,
                                color = RappiColors.TextPrimary
                            )
                        }

                        if (user.isPrime) {
                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(RappiColors.DiscountYellow)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "⚡ Prime Free Delivery",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = RappiColors.TextPrimary
                                )
                            }
                        }
                    }

                    RappiButton(
                        text = if (isPlacingOrder) "Confirming Order..." else "Place Order · ${grandTotal.formatCop()}",
                        enabled = cartItems.isNotEmpty() && !isPlacingOrder,
                        onClick = {
                            isPlacingOrder = true
                            val itemsDesc = cartItems.joinToString(", ") { "${it.quantity}x ${it.product.name}" }
                            val isTurbo = selectedSpeed == "turbo"
                            UserManager.recordNewOrder(
                                storeName = if (isTurbo) "Turbo 10 Min - Chicó Norte" else "Rappi Supermarket",
                                storeEmoji = if (isTurbo) "⚡" else "🛒",
                                itemsSummary = itemsDesc,
                                total = grandTotal,
                                isTurbo = isTurbo
                            )
                            CartManager.clearCart()
                            onOrderPlaced()
                        },
                        testTag = "place_order_button"
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "🛒", fontSize = 56.sp)
                    Text(
                        text = "Your basket is empty",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TextPrimary
                    )
                    Text(
                        text = "Add delicious food and drinks to get started!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RappiColors.TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onNavigateBack,
                        colors = ButtonDefaults.buttonColors(containerColor = RappiColors.Orange),
                        shape = PillShape
                    ) {
                        Text("Start Shopping", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(RappiColors.Background)
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                // 1. Delivery Address Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
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
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(RappiColors.OrangeLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = user.selectedAddress.iconEmoji, fontSize = 22.sp)
                                }

                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "Delivering to ${user.selectedAddress.label}",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = RappiColors.TextPrimary
                                        )
                                    }
                                    Text(
                                        text = user.selectedAddress.addressLine,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = RappiColors.TextSecondary
                                    )
                                }
                            }

                            TextButton(
                                onClick = { showAddressSheet = true },
                                modifier = Modifier.testTag("change_address_button")
                            ) {
                                Text(
                                    text = "Change",
                                    color = RappiColors.Orange,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // 2. Delivery Speed Options
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Delivery Options",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = RappiColors.TextPrimary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Turbo option
                                val isTurbo = selectedSpeed == "turbo"
                                Card(
                                    onClick = { selectedSpeed = "turbo" },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isTurbo) Color(0xFF0F3818) else RappiColors.SurfaceVariant
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.FlashOn,
                                                contentDescription = null,
                                                tint = RappiColors.TurboNeonGreen,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = "Turbo 10-15m",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isTurbo) Color.White else RappiColors.TextPrimary
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = if (user.isPrime) "FREE with Prime" else "$3.900 COP",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isTurbo) RappiColors.TurboNeonGreen else RappiColors.TextSecondary
                                        )
                                    }
                                }

                                // Standard option
                                val isStd = selectedSpeed == "standard"
                                Card(
                                    onClick = { selectedSpeed = "standard" },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isStd) RappiColors.OrangeLight else RappiColors.SurfaceVariant
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ElectricScooter,
                                                contentDescription = null,
                                                tint = RappiColors.Orange,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = "Standard 30m",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = RappiColors.TextPrimary
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = if (user.isPrime) "FREE with Prime" else "$2.500 COP",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = RappiColors.TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Basket Items
                item {
                    Text(
                        text = "Items in your order",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TextPrimary
                    )
                }

                items(cartItems) { item ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
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
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(RappiColors.ChipGreyAlt),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = item.product.iconEmoji, fontSize = 24.sp)
                                }

                                Column {
                                    Text(
                                        text = item.product.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = RappiColors.TextPrimary,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${item.selectedSize} · ${item.selectedPackSize}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = RappiColors.TextSecondary
                                    )
                                    Text(
                                        text = (item.product.price * item.quantity).formatCop(),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = RappiColors.Orange
                                    )
                                }
                            }

                            // Stepper controls
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(RappiColors.ChipGreyAlt)
                                        .clickable {
                                            CartManager.removeProduct(item.product.id)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (item.quantity == 1) Icons.Default.DeleteOutline else Icons.Default.Remove,
                                        contentDescription = "Minus",
                                        tint = RappiColors.TextPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Text(
                                    text = item.quantity.toString(),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = RappiColors.TextPrimary
                                )

                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(RappiColors.OrangeLight)
                                        .clickable {
                                            CartManager.addProduct(item.product, 1, item.selectedSize, item.selectedPackSize)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Plus",
                                        tint = RappiColors.Orange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 4. Promo Coupons
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = RappiColors.Orange,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Promotions & Coupons",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = RappiColors.TextPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            if (appliedCoupon != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(RappiColors.DiscountYellow)
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Coupon '$appliedCoupon' applied (-${discount.formatCop()})",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = RappiColors.TextPrimary
                                    )
                                    IconButton(
                                        onClick = { UserManager.removePromoCode() },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove coupon",
                                            tint = RappiColors.TextPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = promoInput,
                                        onValueChange = {
                                            promoInput = it
                                            promoError = false
                                        },
                                        placeholder = { Text("e.g. RAPPIYA, FUTBOL50", fontSize = 13.sp) },
                                        singleLine = true,
                                        modifier = Modifier.weight(1f),
                                        isError = promoError,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = RappiColors.Orange,
                                            cursorColor = RappiColors.Orange
                                        )
                                    )

                                    Button(
                                        onClick = {
                                            val success = UserManager.applyPromoCode(promoInput)
                                            if (success) {
                                                promoInput = ""
                                                promoError = false
                                            } else {
                                                promoError = true
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = RappiColors.Orange),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.height(52.dp)
                                    ) {
                                        Text("Apply", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }

                                if (promoError) {
                                    Text(
                                        text = "Invalid coupon code. Try 'RAPPIYA' or 'FUTBOL50'",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Red,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 5. Courier Tip Selector
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Tip for your courier (Brayan A.) ❤️",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = RappiColors.TextPrimary
                            )
                            Text(
                                text = "100% of your tip goes directly to the courier",
                                style = MaterialTheme.typography.labelSmall,
                                color = RappiColors.TextSecondary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            val tipOptions = listOf(0, 1000, 2000, 3000, 5000)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                tipOptions.forEach { tipVal ->
                                    val isSelected = selectedTip == tipVal
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isSelected) RappiColors.Orange else RappiColors.ChipGreyAlt)
                                            .clickable { selectedTip = tipVal }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (tipVal == 0) "$0" else "$${tipVal / 1000}k",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else RappiColors.TextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 6. Payment Method Selector
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Payment Method",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = RappiColors.TextPrimary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            paymentMethods.forEach { method ->
                                val isSelected = method.id == selectedPaymentId
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) RappiColors.OrangeLight else Color.Transparent)
                                        .clickable { UserManager.selectPaymentMethod(method.id) }
                                        .padding(horizontal = 10.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(text = method.iconEmoji, fontSize = 20.sp)
                                        Column {
                                            Text(
                                                text = method.title,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = RappiColors.TextPrimary
                                            )
                                            Text(
                                                text = method.subtitle,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = RappiColors.TextSecondary
                                            )
                                        }
                                    }

                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(RappiColors.Orange),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 7. Order Breakdown Summary
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Order Summary",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = RappiColors.TextPrimary
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Products Subtotal", style = MaterialTheme.typography.bodyMedium, color = RappiColors.TextSecondary)
                                Text(subtotal.formatCop(), style = MaterialTheme.typography.bodyMedium, color = RappiColors.TextPrimary)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Delivery Fee", style = MaterialTheme.typography.bodyMedium, color = RappiColors.TextSecondary)
                                Text(
                                    if (deliveryFee == 0) "FREE (Prime)" else deliveryFee.formatCop(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (deliveryFee == 0) RappiColors.Success else RappiColors.TextPrimary,
                                    fontWeight = if (deliveryFee == 0) FontWeight.Bold else FontWeight.Normal
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Service Fee", style = MaterialTheme.typography.bodyMedium, color = RappiColors.TextSecondary)
                                Text(serviceFee.formatCop(), style = MaterialTheme.typography.bodyMedium, color = RappiColors.TextPrimary)
                            }

                            if (selectedTip > 0) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Courier Tip", style = MaterialTheme.typography.bodyMedium, color = RappiColors.TextSecondary)
                                    Text(selectedTip.formatCop(), style = MaterialTheme.typography.bodyMedium, color = RappiColors.TextPrimary)
                                }
                            }

                            if (discount > 0) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Coupon Discount", style = MaterialTheme.typography.bodyMedium, color = RappiColors.Success, fontWeight = FontWeight.Bold)
                                    Text("-${discount.formatCop()}", style = MaterialTheme.typography.bodyMedium, color = RappiColors.Success, fontWeight = FontWeight.Bold)
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = RappiColors.Divider)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RappiColors.TextPrimary)
                                Text(grandTotal.formatCop(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = RappiColors.Orange)
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }

    if (showAddressSheet) {
        AddressSelectorSheet(
            onDismiss = { showAddressSheet = false }
        )
    }
}
