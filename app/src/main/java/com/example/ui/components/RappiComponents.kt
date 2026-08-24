package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.formatCop
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors

@Composable
fun RappiGrabber(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(top = 10.dp, bottom = 6.dp)
            .size(width = 36.dp, height = 4.dp)
            .clip(PillShape)
            .background(RappiColors.Grabber)
    )
}

@Composable
fun RappiMustacheMark(
    modifier: Modifier = Modifier,
    backgroundColor: Color = RappiColors.Orange,
    contentColor: Color = Color.White,
    sizeDp: Int = 32
) {
    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        // Mustache emoji / visual mark
        Text(
            text = "👨🏻",
            fontSize = (sizeDp * 0.55).sp,
            color = contentColor
        )
    }
}

@Composable
fun RappiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = RappiColors.Orange,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    testTag: String = "rappi_button"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.4f),
            disabledContentColor = contentColor.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .testTag(testTag)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun RappiRatingBadge(
    rating: Float,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = PillShape,
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier
            .border(1.dp, Color(0xFFF0F0F0), PillShape)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Rating star",
                tint = RappiColors.StarGold,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = "%.1f".format(rating),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = RappiColors.TextPrimary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun QuantityStepper(
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = RappiColors.Orange
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(RappiColors.ChipGreyAlt)
                .clickable { onDecrement() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease",
                tint = RappiColors.TextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = RappiColors.TextPrimary
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(RappiColors.ChipGreyAlt)
                .clickable { onIncrement() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase",
                tint = RappiColors.TextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun DiscountBadge(
    discountPercent: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(RappiColors.DiscountYellow)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "-$discountPercent%",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            color = RappiColors.TextPrimary,
            fontSize = 11.sp
        )
    }
}

@Composable
fun ColdBadge(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(PillShape)
            .background(RappiColors.ColdBadgeBlue)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = "❄️ Cold",
            style = MaterialTheme.typography.labelSmall,
            color = RappiColors.ColdBadgeText,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
    }
}

@Composable
fun OutsourcedBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(PillShape)
            .background(RappiColors.OutsourceTagBg)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "Outsourced pr...",
            style = MaterialTheme.typography.labelSmall,
            color = RappiColors.OutsourceTagText,
            fontSize = 10.sp
        )
    }
}

@Composable
fun RappiBottomNavBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        color = RappiColors.Surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavBarItem(
                title = "Home",
                icon = Icons.Default.Home,
                isSelected = selectedTab == "home",
                onClick = { onTabSelected("home") }
            )
            NavBarItem(
                title = "Offers",
                icon = Icons.Default.LocalOffer,
                isSelected = selectedTab == "offers",
                onClick = { onTabSelected("offers") }
            )
            NavBarItem(
                title = "Favorites",
                icon = Icons.Default.FavoriteBorder,
                isSelected = selectedTab == "favorites",
                onClick = { onTabSelected("favorites") }
            )
            NavBarItem(
                title = "Account",
                icon = Icons.Default.Person,
                isSelected = selectedTab == "account",
                onClick = { onTabSelected("account") }
            )

            // Floating Search FAB button
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, RappiColors.Divider, CircleShape)
                    .shadow(4.dp, CircleShape)
                    .clickable { onSearchClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = RappiColors.TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(RappiColors.OrangeLight)
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = RappiColors.Orange,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = RappiColors.TextTertiary,
                modifier = Modifier.size(22.dp)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) RappiColors.Orange else RappiColors.TextTertiary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 11.sp
        )
    }
}

@Composable
fun RappiFloatingBasketBar(
    itemCount: Int,
    totalPrice: Int,
    onGoToBasket: () -> Unit,
    modifier: Modifier = Modifier,
    thumbnailEmoji: String = "🍾"
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(elevation = 16.dp, shape = PillShape),
        shape = PillShape,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(RappiColors.ChipGreyAlt),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = thumbnailEmoji, fontSize = 22.sp)
                }

                Column {
                    Text(
                        text = "$itemCount ${if (itemCount == 1) "product" else "products"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = RappiColors.TextSecondary
                    )
                    Text(
                        text = totalPrice.formatCop(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TextPrimary
                    )
                }
            }

            Button(
                onClick = onGoToBasket,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RappiColors.Success,
                    contentColor = Color.White
                ),
                shape = PillShape,
                modifier = Modifier.height(44.dp)
            ) {
                Text(
                    text = "Go to basket",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
