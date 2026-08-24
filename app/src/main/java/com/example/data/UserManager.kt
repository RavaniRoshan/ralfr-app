package com.example.data

import com.example.domain.model.Address
import com.example.domain.model.PastOrder
import com.example.domain.model.PaymentMethod
import com.example.domain.model.PaymentType
import com.example.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object UserManager {
    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _user = MutableStateFlow(
        UserProfile(
            id = "usr_101",
            name = "Brayan Smith",
            email = "brayan.smith@rappi.com",
            phone = "+57 312 984 7621",
            avatarEmoji = "👨🏻‍💻",
            isPrime = true,
            primeTierName = "RappiPrime Plus",
            rappiPayBalance = 145000,
            rappiCredits = 25000
        )
    )
    val user: StateFlow<UserProfile> = _user.asStateFlow()

    private val _paymentMethods = MutableStateFlow(
        listOf(
            PaymentMethod(
                id = "pm_rappi_pay",
                type = PaymentType.RappiPay,
                title = "RappiPay Balance",
                subtitle = "Available: $145.000 COP",
                iconEmoji = "💳",
                isDefault = true
            ),
            PaymentMethod(
                id = "pm_visa",
                type = PaymentType.CreditCard,
                title = "Visa ending in 4242",
                subtitle = "Exp 12/28",
                iconEmoji = "💳",
                isDefault = false
            ),
            PaymentMethod(
                id = "pm_cash",
                type = PaymentType.Cash,
                title = "Cash on Delivery",
                subtitle = "Exact change appreciated",
                iconEmoji = "💵",
                isDefault = false
            )
        )
    )
    val paymentMethods: StateFlow<List<PaymentMethod>> = _paymentMethods.asStateFlow()

    private val _selectedPaymentMethodId = MutableStateFlow("pm_rappi_pay")
    val selectedPaymentMethodId: StateFlow<String> = _selectedPaymentMethodId.asStateFlow()

    private val _appliedPromoCode = MutableStateFlow<String?>(null)
    val appliedPromoCode: StateFlow<String?> = _appliedPromoCode.asStateFlow()

    private val _pastOrders = MutableStateFlow<List<PastOrder>>(
        listOf(
            PastOrder(
                id = "ORD-94812",
                storeName = "Burger King - Calle 127",
                storeEmoji = "🍔",
                dateText = "Yesterday, 8:45 PM",
                itemsSummary = "Combo Whopper Jr + Papas Medianas + Gaseosa",
                totalAmount = 21900,
                statusText = "Delivered",
                isTurbo = false,
                ratingGiven = 5
            ),
            PastOrder(
                id = "ORD-89301",
                storeName = "Turbo 10 Min - Chicó Norte",
                storeEmoji = "⚡",
                dateText = "20 Aug, 2:15 PM",
                itemsSummary = "Bretaña Soda 1.5L, Aguacate Hass x2, Leche Alquería",
                totalAmount = 16900,
                statusText = "Delivered",
                isTurbo = true,
                ratingGiven = 5
            ),
            PastOrder(
                id = "ORD-76120",
                storeName = "Fruver Fresco",
                storeEmoji = "🥑",
                dateText = "16 Aug, 11:30 AM",
                itemsSummary = "Banano Criollo 1kg, Tomate Chonto 1kg, Manzanas",
                totalAmount = 12400,
                statusText = "Delivered",
                isTurbo = false,
                ratingGiven = 5
            )
        )
    )
    val pastOrders: StateFlow<List<PastOrder>> = _pastOrders.asStateFlow()

    fun login(name: String, email: String, phone: String, avatar: String = "👨🏻‍💻") {
        _user.update {
            it.copy(
                name = name,
                email = email,
                phone = phone,
                avatarEmoji = avatar
            )
        }
        _isLoggedIn.value = true
    }

    fun loginWithGoogle(accountName: String = "Brayan Smith", accountEmail: String = "brayan.smith@gmail.com") {
        login(accountName, accountEmail, "+57 312 984 7621", "👨🏻‍💻")
    }

    fun loginWithPhone(phoneNumber: String = "+57 300 123 4567") {
        login("Rappi Explorer", "user@rappi.com", phoneNumber, "🚀")
    }

    fun loginAsGuest() {
        login("Guest User", "guest@rappi.com", "+57 300 000 0000", "🛵")
    }

    fun logout() {
        _isLoggedIn.value = false
    }

    fun selectAddress(addressId: String) {
        _user.update { it.copy(selectedAddressId = addressId) }
    }

    fun addNewAddress(label: String, addressLine: String, neighborhood: String, instructions: String, iconEmoji: String = "📍") {
        val newAddr = Address(
            id = "addr_${System.currentTimeMillis()}",
            label = label,
            addressLine = addressLine,
            cityAndNeighborhood = neighborhood,
            instructions = instructions,
            iconEmoji = iconEmoji
        )
        _user.update {
            it.copy(
                savedAddresses = it.savedAddresses + newAddr,
                selectedAddressId = newAddr.id
            )
        }
    }

    fun selectPaymentMethod(id: String) {
        _selectedPaymentMethodId.value = id
    }

    fun applyPromoCode(code: String): Boolean {
        val clean = code.trim().uppercase()
        return if (clean == "RAPPIYA" || clean == "FUTBOL50" || clean == "TURBO10" || clean == "PRIMEVIP") {
            _appliedPromoCode.value = clean
            true
        } else {
            false
        }
    }

    fun removePromoCode() {
        _appliedPromoCode.value = null
    }

    fun getDiscountAmount(subtotal: Int): Int {
        return when (_appliedPromoCode.value) {
            "RAPPIYA" -> 5000
            "FUTBOL50" -> (subtotal * 0.20).toInt().coerceAtMost(10000)
            "TURBO10" -> 4000
            "PRIMEVIP" -> 6000
            else -> 0
        }
    }

    fun recordNewOrder(storeName: String, storeEmoji: String, itemsSummary: String, total: Int, isTurbo: Boolean) {
        val orderId = "ORD-${(10000..99999).random()}"
        val now = SimpleDateFormat("d MMM, h:mm a", Locale.getDefault()).format(Date())
        val newOrder = PastOrder(
            id = orderId,
            storeName = storeName,
            storeEmoji = storeEmoji,
            dateText = "Today, $now",
            itemsSummary = itemsSummary,
            totalAmount = total,
            statusText = "In Progress",
            isTurbo = isTurbo,
            ratingGiven = 5
        )
        _pastOrders.update { listOf(newOrder) + it }
    }
}
