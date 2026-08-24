package com.example.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.CartManager
import com.example.data.UserManager
import com.example.domain.model.Product
import com.example.feature.auth.WelcomeScreen
import com.example.feature.checkout.CheckoutScreen
import com.example.feature.deals.DealsScreen
import com.example.feature.fruver.FruverScreen
import com.example.feature.home.HomeScreen
import com.example.feature.orders.OrdersScreen
import com.example.feature.product.ProductDetailSheet
import com.example.feature.profile.ProfileScreen
import com.example.feature.search.SearchScreen
import com.example.feature.store.StoreScreen
import com.example.feature.tracking.OrderTrackingScreen
import com.example.feature.tracking.OrderTrackingViewModel
import com.example.feature.turbo.TurboScreen

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Home : Screen("home")
    object Turbo : Screen("turbo")
    object Fruver : Screen("fruver")
    object Store : Screen("store")
    object Search : Screen("search")
    object Tracking : Screen("tracking")
    object Checkout : Screen("checkout")
    object Offers : Screen("offers")
    object Orders : Screen("orders")
    object Account : Screen("account")
}

@Composable
fun RappiNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    val trackingViewModel: OrderTrackingViewModel = viewModel()
    var selectedProductForDetail by remember { mutableStateOf<Product?>(null) }
    val isLoggedIn by UserManager.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { fadeIn(tween(250)) },
        exitTransition = { fadeOut(tween(200)) },
        popEnterTransition = { fadeIn(tween(250)) },
        popExitTransition = { fadeOut(tween(200)) }
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onContinue = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToTurbo = { navController.navigate(Screen.Turbo.route) },
                onNavigateToFruver = { navController.navigate(Screen.Fruver.route) },
                onNavigateToStore = { navController.navigate(Screen.Store.route) },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToTracking = { navController.navigate(Screen.Tracking.route) },
                onNavigateToOffers = { navController.navigate(Screen.Offers.route) },
                onNavigateToOrders = { navController.navigate(Screen.Orders.route) },
                onNavigateToAccount = { navController.navigate(Screen.Account.route) },
                onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) },
                onProductClick = { product -> selectedProductForDetail = product }
            )
        }

        composable(Screen.Turbo.route) {
            TurboScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTracking = { navController.navigate(Screen.Tracking.route) },
                onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) },
                onProductClick = { product -> selectedProductForDetail = product }
            )
        }

        composable(Screen.Fruver.route) {
            FruverScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTracking = { navController.navigate(Screen.Tracking.route) },
                onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) },
                onProductClick = { product -> selectedProductForDetail = product }
            )
        }

        composable(Screen.Store.route) {
            StoreScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTracking = { navController.navigate(Screen.Tracking.route) },
                onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) },
                onProductClick = { product -> selectedProductForDetail = product }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTracking = { navController.navigate(Screen.Tracking.route) },
                onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) },
                onProductClick = { product -> selectedProductForDetail = product }
            )
        }

        composable(Screen.Tracking.route) {
            OrderTrackingScreen(
                viewModel = trackingViewModel,
                onBackToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(
                onNavigateBack = { navController.popBackStack() },
                onOrderPlaced = {
                    navController.navigate(Screen.Tracking.route) {
                        popUpTo(Screen.Checkout.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Offers.route) {
            DealsScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToOrders = { navController.navigate(Screen.Orders.route) },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToAccount = { navController.navigate(Screen.Account.route) },
                onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) },
                onProductClick = { product -> selectedProductForDetail = product }
            )
        }

        composable(Screen.Orders.route) {
            OrdersScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToOffers = { navController.navigate(Screen.Offers.route) },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToAccount = { navController.navigate(Screen.Account.route) },
                onNavigateToTracking = { navController.navigate(Screen.Tracking.route) },
                onReorderToCheckout = { navController.navigate(Screen.Checkout.route) }
            )
        }

        composable(Screen.Account.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToOffers = { navController.navigate(Screen.Offers.route) },
                onNavigateToOrders = { navController.navigate(Screen.Orders.route) },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToTracking = { navController.navigate(Screen.Tracking.route) },
                onLogout = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }

    // Modal Product Details Sheet (Accessible everywhere)
    selectedProductForDetail?.let { product ->
        ProductDetailSheet(
            product = product,
            onAddToCart = { prod, qty, size, pack ->
                CartManager.addProduct(prod, qty, size, pack)
                selectedProductForDetail = null
            },
            onDismiss = { selectedProductForDetail = null }
        )
    }
}
