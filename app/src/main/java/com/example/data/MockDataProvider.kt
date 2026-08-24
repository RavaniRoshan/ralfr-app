package com.example.data

import com.example.domain.model.Product
import com.example.domain.model.Store

object MockDataProvider {

    val sodaProducts = listOf(
        Product(
            id = "soda_1",
            name = "Bretaña Soda Botella",
            brand = "Bretaña",
            price = 3500,
            originalPrice = 5000,
            discountPercent = 30,
            unit = "1.5 L",
            unitPriceText = "$2.33 / ml",
            isCold = true,
            isOutsourced = false,
            category = "Beverages",
            section = "Sodas",
            iconEmoji = "🍾",
            description = "Refreshing sparkling soda water in 1.5 L bottle. Chilled and ready to drink."
        ),
        Product(
            id = "soda_2",
            name = "Coca-Cola Sabor Original",
            brand = "Coca cola",
            price = 4200,
            originalPrice = 4800,
            discountPercent = 12,
            unit = "1.5 L",
            unitPriceText = "$2.80 / ml",
            isCold = true,
            isOutsourced = false,
            category = "Beverages",
            section = "Sodas",
            iconEmoji = "🥤",
            description = "Original taste Coca-Cola chilled bottle."
        ),
        Product(
            id = "soda_3",
            name = "Quatro Toronja Botella",
            brand = "Quatro",
            price = 3800,
            originalPrice = 4500,
            discountPercent = 15,
            unit = "1.5 L",
            unitPriceText = "$2.53 / ml",
            isCold = true,
            isOutsourced = false,
            category = "Beverages",
            section = "Sodas",
            iconEmoji = "🍊",
            description = "Crisp and tangy grapefruit flavored sparkling drink."
        ),
        Product(
            id = "soda_4",
            name = "Pony Malta Pet",
            brand = "Pony malta",
            price = 3200,
            originalPrice = 3600,
            discountPercent = 11,
            unit = "1 L",
            unitPriceText = "$3.20 / ml",
            isCold = false,
            isOutsourced = false,
            category = "Beverages",
            section = "Sodas",
            iconEmoji = "🍺",
            description = "Nutritious malt beverage rich in vitamins."
        ),
        Product(
            id = "soda_5",
            name = "Ginger Ale Canada Dry",
            brand = "Canada Dry",
            price = 4500,
            originalPrice = null,
            discountPercent = null,
            unit = "1.5 L",
            unitPriceText = "$3.00 / ml",
            isCold = true,
            isOutsourced = false,
            category = "Beverages",
            section = "Sodas",
            iconEmoji = "🥂",
            description = "Premium ginger ale beverage."
        ),
        Product(
            id = "soda_6",
            name = "Sprite Lima Limón",
            brand = "Sprite",
            price = 3900,
            originalPrice = 4600,
            discountPercent = 15,
            unit = "1.5 L",
            unitPriceText = "$2.60 / ml",
            isCold = true,
            isOutsourced = true,
            category = "Beverages",
            section = "Sodas",
            iconEmoji = "🍋",
            description = "Lemon-lime flavored carbonated soda."
        )
    )

    val fruverProducts = listOf(
        Product(
            id = "f_1",
            name = "Aguacate Hass Maduro",
            brand = "Campo Fresco",
            price = 4900,
            originalPrice = 6500,
            discountPercent = 24,
            unit = "500 g",
            unitPriceText = "$9.80 / g",
            category = "Frutas",
            section = "Frutas",
            iconEmoji = "🥑",
            description = "Ready-to-eat ripe Hass avocado with rich creamy texture."
        ),
        Product(
            id = "f_2",
            name = "Banano Criollo Fresco",
            brand = "Frutas del Valle",
            price = 2800,
            originalPrice = 3500,
            discountPercent = 20,
            unit = "1 kg",
            unitPriceText = "$2.80 / g",
            category = "Frutas",
            section = "Frutas",
            iconEmoji = "🍌",
            description = "Sweet and fresh local bananas."
        ),
        Product(
            id = "f_3",
            name = "Manzana Roja Royal Gala",
            brand = "Importada",
            price = 6200,
            originalPrice = 7800,
            discountPercent = 20,
            unit = "4 Und",
            unitPriceText = "$1.550 / und",
            category = "Frutas",
            section = "Frutas",
            iconEmoji = "🍎",
            description = "Crisp sweet Royal Gala apples."
        ),
        Product(
            id = "f_4",
            name = "Tomate Chonto Seleccionado",
            brand = "Campo Fresco",
            price = 3400,
            originalPrice = 4200,
            discountPercent = 19,
            unit = "1 kg",
            unitPriceText = "$3.40 / g",
            category = "Verduras",
            section = "Verduras",
            iconEmoji = "🍅",
            description = "Firm and juicy Chonto tomatoes for cooking and salads."
        ),
        Product(
            id = "f_5",
            name = "Cebolla Cabezona Roja",
            brand = "Campo Fresco",
            price = 2900,
            originalPrice = null,
            discountPercent = null,
            unit = "1 kg",
            unitPriceText = "$2.90 / g",
            category = "Verduras",
            section = "Verduras",
            iconEmoji = "🧅",
            description = "Fresh red onions with sharp aroma."
        ),
        Product(
            id = "f_6",
            name = "Papa Pastusa Lavada",
            brand = "Andina",
            price = 4500,
            originalPrice = 5500,
            discountPercent = 18,
            unit = "2 kg",
            unitPriceText = "$2.25 / g",
            category = "Tubérculos",
            section = "Tubérculos",
            iconEmoji = "🥔",
            description = "High quality washed Colombian potatoes."
        ),
    )

    val burgerKingStore = Store(
        id = "store_bk",
        name = "Burger King - Calle 127",
        logoText = "BK",
        rating = 3.9f,
        ratingCount = 526,
        deliveryTimeMin = 12,
        deliveryFeeText = "FREE",
        isFreeDelivery = true,
        category = "Burgers & Fast Food",
        closingSoonText = "Closes in 14 minutes"
    )

    val burgerKingCombos = listOf(
        Product(
            id = "bk_1",
            name = "Combo Doble con Queso + Papas Medianas + Bebida 16oz",
            brand = "Burger King",
            price = 18900,
            originalPrice = 32900,
            discountPercent = 42,
            unit = "Combo",
            unitPriceText = "Includes fries & drink",
            category = "Combos",
            section = "Deals",
            iconEmoji = "🍔",
            description = "Two flame-grilled beef patties with melted American cheese, pickles, mustard, and ketchup served with golden fries."
        ),
        Product(
            id = "bk_2",
            name = "Combo Whopper Jr Doble + Papas Medianas + Gaseosa",
            brand = "Burger King",
            price = 21900,
            originalPrice = 39900,
            discountPercent = 45,
            unit = "Combo",
            unitPriceText = "Includes fries & drink",
            category = "Combos",
            section = "Deals",
            iconEmoji = "🍔",
            description = "Double flame-grilled beef with fresh lettuce, ripe tomatoes, mayo, pickles and onions on a toasted sesame seed bun."
        ),
        Product(
            id = "bk_3",
            name = "King Box 4 Productos (Whopper + Nuggets x4 + Papas + Bebida)",
            brand = "Burger King",
            price = 27900,
            originalPrice = 42000,
            discountPercent = 33,
            unit = "King Box",
            unitPriceText = "Full meal box",
            category = "King Box",
            section = "Most Ordered",
            iconEmoji = "👑",
            description = "The ultimate feast: classic Whopper, crispy golden nuggets, medium fries, and ice-cold beverage."
        ),
        Product(
            id = "bk_4",
            name = "Nuggets de Pollo x10 + Salsa BBQ",
            brand = "Burger King",
            price = 12900,
            originalPrice = 16900,
            discountPercent = 23,
            unit = "10 Und",
            unitPriceText = "Includes sauce",
            category = "Sides",
            section = "Sides",
            iconEmoji = "🍗",
            description = "Ten tender crispy chicken nuggets served with signature sweet & smoky barbecue sauce."
        )
    )

    val turboFeaturedProducts = listOf(
        sodaProducts[0],
        sodaProducts[1],
        fruverProducts[0],
        fruverProducts[1],
        Product(
            id = "turbo_1",
            name = "Leche Entera Alquería 1.1L",
            brand = "Alquería",
            price = 4600,
            originalPrice = 5200,
            discountPercent = 11,
            unit = "1.1 L",
            unitPriceText = "$4.18 / ml",
            isCold = true,
            iconEmoji = "🥛",
            description = "Fresh pasteurized whole milk in convenient pouch."
        ),
        Product(
            id = "turbo_2",
            name = "Huevos Santa Anita AA x30",
            brand = "Santa Anita",
            price = 18900,
            originalPrice = 22000,
            discountPercent = 14,
            unit = "30 Und",
            unitPriceText = "$630 / und",
            iconEmoji = "🥚",
            description = "Fresh farm grade AA brown eggs panal."
        ),
        Product(
            id = "turbo_3",
            name = "Pan Tajado Bimbo Blanco Artesano",
            brand = "Bimbo",
            price = 7800,
            originalPrice = 8900,
            discountPercent = 12,
            unit = "500 g",
            unitPriceText = "$15.60 / g",
            iconEmoji = "🍞",
            description = "Soft thick-cut artisan white bread slices."
        )
    )
}
