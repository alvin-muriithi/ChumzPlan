package com.example.chumzplan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chumzplan.ui.dashboard.ChumzPlanDashboard
import com.example.chumzplan.ui.onboarding.*
import com.example.chumzplan.ui.theme.ChumzPlanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChumzPlanTheme {
                AppNavigation()
            }
        }
    }
}

// ============================================
// Navigation Routes
// ============================================

sealed class Route(val route: String) {
    object Onboarding1 : Route("onboarding1")
    object Onboarding2 : Route("onboarding2")
    object Onboarding3 : Route("onboarding3")
    object Dashboard : Route("dashboard")
    object Transactions : Route("transactions")
    object Reports : Route("reports")
    object Profile : Route("profile")
}

// ============================================
// Main App Navigation
// ============================================

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var currentScreen by remember { mutableStateOf(0) }

    // Shared state for onboarding data
    var userName by remember { mutableStateOf("") }
    var userAge by remember { mutableStateOf("") }
    var userUniversity by remember { mutableStateOf("") }
    var loanAmount by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        // Change this to Route.Dashboard.route to skip onboarding for testing
        startDestination = Route.Onboarding1.route
    ) {
        // Onboarding Screens
        composable(Route.Onboarding1.route) {
            OnboardingScreen1(
                onNext = { name, age, university ->
                    userName = name
                    userAge = age
                    userUniversity = university
                    navController.navigate(Route.Onboarding2.route)
                },
                onSkip = {
                    navController.navigate(Route.Dashboard.route) {
                        popUpTo(Route.Onboarding1.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.Onboarding2.route) {
            OnboardingScreen2(
                onNext = { loan, start, end ->
                    loanAmount = loan
                    startDate = start
                    endDate = end
                    navController.navigate(Route.Onboarding3.route)
                },
                onSkip = {
                    navController.navigate(Route.Dashboard.route) {
                        popUpTo(Route.Onboarding1.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.Onboarding3.route) {
            OnboardingScreen3(
                totalAmount = loanAmount.toDoubleOrNull() ?: 45000.0,
                onComplete = { categories ->
                    navController.navigate(Route.Dashboard.route) {
                        popUpTo(Route.Onboarding1.route) { inclusive = true }
                    }
                },
                onSkip = {
                    navController.navigate(Route.Dashboard.route) {
                        popUpTo(Route.Onboarding1.route) { inclusive = true }
                    }
                }
            )
        }

        // Main Dashboard
        composable(Route.Dashboard.route) {
            ChumzPlanDashboard(
                onAddExpense = {
                    // TODO: Navigate to add expense screen
                },
                onAddIncome = {
                    // TODO: Navigate to add income screen
                },
                onBalanceClick = {
                    // TODO: Navigate to balance details
                }
            )
        }

        // Other Screens (Placeholders for now)
        composable(Route.Transactions.route) {
            PlaceholderScreen(
                title = "Transactions",
                icon = Icons.Default.Receipt
            )
        }

        composable(Route.Reports.route) {
            PlaceholderScreen(
                title = "Reports",
                icon = Icons.Default.PieChart
            )
        }

        composable(Route.Profile.route) {
            PlaceholderScreen(
                title = "Profile",
                icon = Icons.Default.Person
            )
        }
    }
}

// ============================================
// Placeholder Screen for Other Tabs
// ============================================

@Composable
fun PlaceholderScreen(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$title Screen",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Coming soon...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}