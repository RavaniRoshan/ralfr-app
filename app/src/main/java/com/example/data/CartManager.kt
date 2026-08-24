package com.example.data

import com.example.domain.model.CartItem
import com.example.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object CartManager {
    private val _items = MutableStateFlow<List<CartItem>>(
        listOf(
            CartItem(product = MockDataProvider.sodaProducts[0], quantity = 1)
        )
    )
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    val totalItems: Int
        get() = _items.value.sumOf { it.quantity }

    val totalPrice: Int
        get() = _items.value.sumOf { it.product.price * it.quantity }

    fun addProduct(product: Product, quantity: Int = 1, size: String = "1.5 L", packSize: String = "x1") {
        _items.update { currentList ->
            val index = currentList.indexOfFirst { it.product.id == product.id && it.selectedSize == size && it.selectedPackSize == packSize }
            if (index >= 0) {
                currentList.toMutableList().apply {
                    val existing = this[index]
                    this[index] = existing.copy(quantity = existing.quantity + quantity)
                }
            } else {
                currentList + CartItem(product, quantity, size, packSize)
            }
        }
    }

    fun removeProduct(productId: String) {
        _items.update { currentList ->
            val index = currentList.indexOfFirst { it.product.id == productId }
            if (index >= 0) {
                val existing = currentList[index]
                if (existing.quantity > 1) {
                    currentList.toMutableList().apply {
                        this[index] = existing.copy(quantity = existing.quantity - 1)
                    }
                } else {
                    currentList.filter { it.product.id != productId }
                }
            } else {
                currentList
            }
        }
    }

    fun getQuantityForProduct(productId: String): Int {
        return _items.value.filter { it.product.id == productId }.sumOf { it.quantity }
    }

    fun clearCart() {
        _items.value = emptyList()
    }
}
