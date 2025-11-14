// ============================================
// FILE: DashboardScreen.kt (UPDATED)
// Place in: app/src/main/java/com/example/chumzplan/ui/dashboard/
// ============================================

package com.example.chumzplan.ui.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================
// Color Scheme
// ============================================

object StretchItColors {
    val DarkBackground = Color(0xFF1A2332)
    val CardBackground = Color(0xFF252E3F)
    val WhiteCard = Color(0xFFFFFFFF)
    val AccentGreen = Color(0xFF00D9A3)
    val AccentCyan = Color(0xFF00E5CC)
    val TextWhite = Color(0xFFFFFFFF)
    val TextGray = Color(0xFF8A93A6)
    val GoodGreen = Color(0xFF00D9A3)
    val WarningOrange = Color(0xFFFF9500)
    val DangerRed = Color(0xFFFF3B30)
}

// ============================================
// Main Dashboard Screen
// ============================================

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StretchItDashboard(
    onAddExpense: () -> Unit = {},
    onAddIncome: () -> Unit = {},
    onBalanceClick: () -> Unit = {}
) {
    // Mock data
    val semesterName = "HELB Semester Saver"
    val semesterDate = "September 25, 2024"
    val budgetProgress = 0.72f
    val daysLeft = 50
    val dailyAllowance = 750.0
    val currentBalance = 15200.0
    val weeklySpending = 3450.0
    val monthlySpending = 12500.0
    val topCategory = "Food"
    val topCategoryAmount = 4200.0
    val savingsGoal = 5000.0
    val currentSavings = 3200.0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StretchItColors.DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            // Top Bar
            DashboardTopBar(
                semesterName = semesterName,
                semesterDate = semesterDate
            )

            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Scrollable Central Feature (4 cards)
                CentralFeatureCarousel(
                    budgetProgress = budgetProgress,
                    daysLeft = daysLeft,
                    weeklySpending = weeklySpending,
                    monthlySpending = monthlySpending,
                    topCategory = topCategory,
                    topCategoryAmount = topCategoryAmount,
                    savingsGoal = savingsGoal,
                    currentSavings = currentSavings
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Daily Allowance Card
                DailyAllowanceCard(amount = dailyAllowance)

                Spacer(modifier = Modifier.height(16.dp))

                // Current Balance Card
                CurrentBalanceCard(
                    balance = currentBalance,
                    onClick = onBalanceClick
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                ActionButtonsRow(
                    onAddExpense = onAddExpense,
                    onAddIncome = onAddIncome
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // Bottom Navigation
        BottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// ============================================
// Central Feature Carousel (NEW)
// ============================================

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CentralFeatureCarousel(
    budgetProgress: Float,
    daysLeft: Int,
    weeklySpending: Double,
    monthlySpending: Double,
    topCategory: String,
    topCategoryAmount: Double,
    savingsGoal: Double,
    currentSavings: Double
) {
    val pagerState = rememberPagerState(pageCount = { 4 })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Horizontal Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) { page ->
            when (page) {
                0 -> BudgetHealthCard(budgetProgress, daysLeft)
                1 -> SpendingOverviewCard(weeklySpending, monthlySpending)
                2 -> TopCategoryCard(topCategory, topCategoryAmount)
                3 -> SavingsProgressCard(savingsGoal, currentSavings)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Page Indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index)
                                StretchItColors.AccentGreen
                            else
                                StretchItColors.TextGray.copy(alpha = 0.3f)
                        )
                )
            }
        }
    }
}

// ============================================
// Card 1: Budget Health Ring
// ============================================

@Composable
fun BudgetHealthCard(
    progress: Float,
    daysLeft: Int
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val healthColor = when {
        progress > 0.6f -> StretchItColors.GoodGreen
        progress > 0.3f -> StretchItColors.WarningOrange
        else -> StretchItColors.DangerRed
    }

    val healthText = when {
        progress > 0.6f -> "Good"
        progress > 0.3f -> "Fair"
        else -> "Critical"
    }

    val healthEmoji = when {
        progress > 0.6f -> "😊"
        progress > 0.3f -> "😐"
        else -> "😰"
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Circle
        Canvas(modifier = Modifier.size(240.dp)) {
            drawArc(
                color = StretchItColors.CardBackground,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
        }

        // Progress Arc
        Canvas(modifier = Modifier.size(240.dp)) {
            drawArc(
                brush = Brush.linearGradient(
                    colors = listOf(healthColor, healthColor.copy(alpha = 0.6f))
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
        }

        // Center Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Budget Health:",
                color = StretchItColors.TextGray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = healthText,
                    color = StretchItColors.TextWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = healthEmoji, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Days Left: $daysLeft",
                color = StretchItColors.TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ============================================
// Card 2: Spending Overview
// ============================================

@Composable
fun SpendingOverviewCard(
    weeklySpending: Double,
    monthlySpending: Double
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = null,
                tint = StretchItColors.AccentGreen,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Spending Overview",
                color = StretchItColors.TextWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Weekly Spending
            SpendingInfoRow(
                label = "This Week",
                amount = weeklySpending,
                icon = Icons.Default.CalendarToday
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Monthly Spending
            SpendingInfoRow(
                label = "This Month",
                amount = monthlySpending,
                icon = Icons.Default.DateRange
            )
        }
    }
}

@Composable
fun SpendingInfoRow(
    label: String,
    amount: Double,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = StretchItColors.AccentGreen,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                color = StretchItColors.TextGray,
                fontSize = 16.sp
            )
        }
        Text(
            text = "KES ${amount.toInt()}",
            color = StretchItColors.TextWhite,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ============================================
// Card 3: Top Spending Category
// ============================================

@Composable
fun TopCategoryCard(
    categoryName: String,
    amount: Double
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Category Icon (emoji based on name)
            val categoryEmoji = when (categoryName.lowercase()) {
                "food" -> "🍽️"
                "transport" -> "🚌"
                "accommodation" -> "🏠"
                "books" -> "📚"
                "airtime" -> "📱"
                else -> "💰"
            }

            Text(
                text = categoryEmoji,
                fontSize = 64.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Top Spending Category",
                color = StretchItColors.TextGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = categoryName,
                color = StretchItColors.TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Amount with circular background
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(StretchItColors.CardBackground)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "KES ${amount.toInt()}",
                    color = StretchItColors.AccentGreen,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "spent so far",
                color = StretchItColors.TextGray,
                fontSize = 12.sp
            )
        }
    }
}

// ============================================
// Card 4: Savings Progress
// ============================================

@Composable
fun SavingsProgressCard(
    savingsGoal: Double,
    currentSavings: Double
) {
    val progress = (currentSavings / savingsGoal).toFloat().coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000),
        label = "savings_progress"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Savings,
                contentDescription = null,
                tint = StretchItColors.AccentGreen,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Savings Goal",
                color = StretchItColors.TextWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Savings amounts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Current",
                        color = StretchItColors.TextGray,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "KES ${currentSavings.toInt()}",
                        color = StretchItColors.AccentGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Goal",
                        color = StretchItColors.TextGray,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "KES ${savingsGoal.toInt()}",
                        color = StretchItColors.TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Progress Bar
            Column(modifier = Modifier.fillMaxWidth(0.85f)) {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = StretchItColors.AccentGreen,
                    trackColor = StretchItColors.CardBackground,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${(progress * 100).toInt()}% achieved",
                    color = StretchItColors.TextGray,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

// ============================================
// Top Bar Component
// ============================================

@Composable
fun DashboardTopBar(
    semesterName: String,
    semesterDate: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = StretchItColors.TextWhite)) {
                    append("stretch ")
                }
                withStyle(SpanStyle(color = StretchItColors.AccentGreen)) {
                    append("it")
                }
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = semesterName,
                color = StretchItColors.TextWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = semesterDate,
                color = StretchItColors.TextGray,
                fontSize = 11.sp
            )
        }

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Menu",
                tint = StretchItColors.TextWhite
            )
        }
    }
}

// ============================================
// Daily Allowance Card
// ============================================

@Composable
fun DailyAllowanceCard(amount: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(containerColor = StretchItColors.WhiteCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "You can spend Allowance",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "KES ${amount.toInt()}",
                        color = Color.Black,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "today 🔥", color = Color.Black, fontSize = 24.sp)
                }
            }
        }
    }
}

// ============================================
// Current Balance Card
// ============================================

@Composable
fun CurrentBalanceCard(balance: Double, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(70.dp),
        colors = CardDefaults.cardColors(containerColor = StretchItColors.WhiteCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Current Balance",
                    color = Color.Black.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "KES ${balance.toInt()}",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View details",
                tint = Color.Black.copy(alpha = 0.4f)
            )
        }
    }
}

// ============================================
// Action Buttons Row
// ============================================

@Composable
fun ActionButtonsRow(onAddExpense: () -> Unit, onAddIncome: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = StretchItColors.WhiteCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onAddExpense,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = StretchItColors.AccentGreen
                )
            ) {
                Text("Add Expense", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            FloatingActionButton(
                onClick = onAddExpense,
                containerColor = StretchItColors.AccentGreen,
                contentColor = Color.White,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(32.dp))
            }

            TextButton(
                onClick = onAddIncome,
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
            ) {
                Text("Add Income", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// ============================================
// Bottom Navigation Bar
// ============================================

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    selectedItem: Int = 0,
    onItemSelected: (Int) -> Unit = {}
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth().height(80.dp),
        containerColor = StretchItColors.CardBackground,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text("Dashboard", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = StretchItColors.AccentGreen,
                selectedTextColor = StretchItColors.AccentGreen,
                unselectedIconColor = StretchItColors.TextGray,
                unselectedTextColor = StretchItColors.TextGray,
                indicatorColor = StretchItColors.AccentGreen.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) },
            icon = { Icon(Icons.Default.Receipt, contentDescription = "Transactions") },
            label = { Text("Transactions", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = StretchItColors.AccentGreen,
                selectedTextColor = StretchItColors.AccentGreen,
                unselectedIconColor = StretchItColors.TextGray,
                unselectedTextColor = StretchItColors.TextGray,
                indicatorColor = StretchItColors.AccentGreen.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) },
            icon = { Icon(Icons.Default.PieChart, contentDescription = "Reports") },
            label = { Text("Reports", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = StretchItColors.AccentGreen,
                selectedTextColor = StretchItColors.AccentGreen,
                unselectedIconColor = StretchItColors.TextGray,
                unselectedTextColor = StretchItColors.TextGray,
                indicatorColor = StretchItColors.AccentGreen.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            selected = selectedItem == 3,
            onClick = { onItemSelected(3) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = StretchItColors.AccentGreen,
                selectedTextColor = StretchItColors.AccentGreen,
                unselectedIconColor = StretchItColors.TextGray,
                unselectedTextColor = StretchItColors.TextGray,
                indicatorColor = StretchItColors.AccentGreen.copy(alpha = 0.1f)
            )
        )
    }
}