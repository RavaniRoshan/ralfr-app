package com.example.domain.model

import java.text.NumberFormat
import java.util.Locale
import kotlin.time.Duration

enum class OrderStatus(
    val stepIndex: Int,
    val title: String,
    val progressFraction: Float,
) {
    Confirming(1, "We're confirming your order", 0.05f),
    Preparing(2, "Your order is being prepared", 0.25f),
    PickingUp(3, "A Rappi is picking up your order", 0.45f),
    OnTheWay(4, "Your order is on the way", 0.66f),
    Arrived(5, "Your Rappi has arrived", 0.95f),
    Delivered(6, "Order delivered", 1.0f);

    val isLiveCountdown: Boolean
        get() = this == Preparing || this == PickingUp || this == OnTheWay
}

data class Courier(
    val id: String = "c_102",
    val name: String = "Brayan Alexander",
    val rating: Float = 4.4f,
    val ordersCount: Int = 2186,
    val tipAmount: Int = 400,
    val phoneMasked: String = "+57 (601) 794-0000",
    val vehicle: String = "Scooter",
    val avatarRes: Int? = null,
)

data class ChatMessage(
    val id: String,
    val text: String,
    val timestamp: String,
    val isIncoming: Boolean,
)

data class Store(
    val id: String,
    val name: String,
    val logoText: String,
    val rating: Float = 4.7f,
    val ratingCount: Int = 526,
    val deliveryTimeMin: Int = 12,
    val deliveryFeeText: String = "FREE",
    val isFreeDelivery: Boolean = true,
    val category: String = "Burgers & Fast Food",
    val closingSoonText: String = "Closes in 14 minutes",
)

data class Product(
    val id: String,
    val name: String,
    val brand: String = "",
    val price: Int,
    val originalPrice: Int? = null,
    val discountPercent: Int? = null,
    val unit: String = "1 Und",
    val unitPriceText: String = "",
    val isCold: Boolean = false,
    val isOutsourced: Boolean = false,
    val category: String = "General",
    val section: String = "General",
    val sizes: List<String> = listOf("1.5 L", "2.5 L", "300 mL"),
    val packSizes: List<String> = listOf("x1", "x2", "x6"),
    val description: String = "Fresh chilled beverage with premium carbonation and natural mineral extracts.",
    val iconEmoji: String = "🥤",
)

data class Address(
    val id: String,
    val label: String, // "Home", "Work", "Partner's Place"
    val addressLine: String, // "Calle 93 # 11A-28"
    val cityAndNeighborhood: String = "Chicó Norte, Bogotá",
    val instructions: String = "Apto 402, Ring intercom",
    val isDefault: Boolean = false,
    val iconEmoji: String = "🏠"
)

enum class PaymentType {
    RappiPay,
    CreditCard,
    Cash,
    AppleGooglePay
}

data class PaymentMethod(
    val id: String,
    val type: PaymentType,
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val isDefault: Boolean = false
)

data class UserProfile(
    val id: String = "usr_1",
    val name: String = "Brayan Smith",
    val email: String = "brayan.smith@rappi.com",
    val phone: String = "+57 312 984 7621",
    val avatarEmoji: String = "👨🏻‍💻",
    val isPrime: Boolean = true,
    val primeTierName: String = "RappiPrime Plus",
    val rappiPayBalance: Int = 145000,
    val rappiCredits: Int = 25000,
    val savedAddresses: List<Address> = listOf(
        Address("addr_1", "Home", "Calle 93 # 11A-28", "Chicó Norte, Bogotá", "Apto 402, portería", true, "🏠"),
        Address("addr_2", "Work", "Cra. 7 # 71-21, Torre B", "Zona G, Bogotá", "Piso 8, Oficina Rappi", false, "💼"),
        Address("addr_3", "Family", "Calle 140 # 12-30", "Cedritos, Bogotá", "Casa 12", false, "❤️")
    ),
    val selectedAddressId: String = "addr_1"
) {
    val selectedAddress: Address
        get() = savedAddresses.firstOrNull { it.id == selectedAddressId } ?: savedAddresses.first()
}

data class PastOrder(
    val id: String,
    val storeName: String,
    val storeEmoji: String,
    val dateText: String,
    val itemsSummary: String,
    val totalAmount: Int,
    val statusText: String = "Delivered",
    val isTurbo: Boolean = false,
    val ratingGiven: Int = 5
)

data class CartItem(
    val product: Product,
    val quantity: Int = 1,
    val selectedSize: String = "1.5 L",
    val selectedPackSize: String = "x1",
)

fun Int.formatCop(): String {
    // Colombian Peso formatting: e.g. $12.400, $48.800
    val nf = NumberFormat.getIntegerInstance(Locale("es", "CO"))
    return "$${nf.format(this)}"
}

fun Duration.formatMmSs(): String {
    val totalSeconds = this.inWholeSeconds.coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

