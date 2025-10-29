// ============================================
// FILE: MainActivity.kt (UPDATED)
// Replace your existing MainActivity.kt with this
// ============================================

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
    object Categories : Route("categories")
    object Insights : Route("insights")
}

// ============================================
// Main App Navigation
// ============================================

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Shared state to store onboarding data
    var userName by remember { mutableStateOf("") }
    var userAge by remember { mutableStateOf("") }
    var userUniversity by remember { mutableStateOf("") }
    var loanAmount by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = Route.Onboarding1.route
    ) {
        // Onboarding Screen 1
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

        // Onboarding Screen 2
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

        // Onboarding Screen 3
        composable(Route.Onboarding3.route) {
            OnboardingScreen3(
                totalAmount = loanAmount.toDoubleOrNull() ?: 45000.0,
                onComplete = { categories ->
                    // Save all data (will connect to database later)
                    // For now, just navigate to dashboard
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

        // Main App (with bottom navigation)
        composable(Route.Dashboard.route) {
            MainApp(navController)
        }
        composable(Route.Transactions.route) {
            MainApp(navController)
        }
        composable(Route.Categories.route) {
            MainApp(navController)
        }
        composable(Route.Insights.route) {
            MainApp(navController)
        }
    }
}

// ============================================
// Main App with Bottom Navigation
// ============================================

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Home)
    object Transactions : Screen("transactions", "Transactions", Icons.Default.List)
    object Categories : Screen("categories", "Categories", Icons.Default.Category)
    object Insights : Screen("insights", "Insights", Icons.Default.BarChart)
}

@Composable
fun MainApp(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = { BottomNavigationBar(currentRoute, navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentRoute) {
                Route.Dashboard.route -> DashboardScreen()
                Route.Transactions.route -> TransactionsScreen()
                Route.Categories.route -> CategoriesScreen()
                Route.Insights.route -> InsightsScreen()
                else -> DashboardScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(currentRoute: String?, navController: NavHostController) {
    val items = listOf(
        Screen.Dashboard,
        Screen.Transactions,
        Screen.Categories,
        Screen.Insights
    )

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Route.Dashboard.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

// ============================================
// Dashboard Screen (from before)
// ============================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val totalLoanAmount = 45000.0
    val currentBalance = 32500.0
    val daysLeft = 67
    val dailyBudget = currentBalance / daysLeft

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HELB Loan Manager") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WelcomeCard(daysLeft)
            BalanceCard(currentBalance, totalLoanAmount)
            DailyBudgetCard(dailyBudget)
            QuickStatsRow()
            RecentTransactionsCard()
        }
    }
}

@Composable
fun WelcomeCard(daysLeft: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Welcome Back! 👋",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$daysLeft days left in semester",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun BalanceCard(currentBalance: Double, totalAmount: Double) {
    val progress = (currentBalance / totalAmount).toFloat()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Current Balance", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "KES ${currentBalance.toInt()}",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp)
            )
        }
    }
}

@Composable
fun DailyBudgetCard(dailyBudget: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Daily Budget", style = MaterialTheme.typography.titleMedium)
                Text("You can spend today")
            }
            Text(
                text = "KES ${dailyBudget.toInt()}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun QuickStatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard("This Week", "KES 3,450", Modifier.weight(1f))
        StatCard("This Month", "KES 12,500", Modifier.weight(1f))
    }
}

@Composable
fun StatCard(title: String, amount: String, modifier: Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(amount, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RecentTransactionsCard() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Recent Transactions", fontWeight = FontWeight.Bold)
                TextButton(onClick = { }) { Text("View All") }
            }
            Spacer(modifier = Modifier.height(8.dp))
            TransactionItem("🍽️", "Lunch at Cafeteria", "KES 200", "Today")
            TransactionItem("🚌", "Matatu to Town", "KES 100", "Today")
        }
    }
}

@Composable
fun TransactionItem(icon: String, title: String, amount: String, date: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(icon, style = MaterialTheme.typography.headlineMedium)
            Column {
                Text(title)
                Text(date, style = MaterialTheme.typography.bodySmall)
            }
        }
        Text(amount, fontWeight = FontWeight.Bold)
    }
}

// Placeholder Screens
@Composable
fun TransactionsScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.List, null, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(16.dp))
            Text("Transactions Screen", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
fun CategoriesScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Category, null, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(16.dp))
            Text("Categories Screen", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
fun InsightsScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.BarChart, null, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(16.dp))
            Text("Insights Screen", style = MaterialTheme.typography.headlineMedium)
        }
    }
}