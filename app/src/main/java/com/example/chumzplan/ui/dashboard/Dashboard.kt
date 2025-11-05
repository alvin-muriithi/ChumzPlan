package com.example.chumzplan.ui.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


object ChumzPlanColors {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChumzPlanDashboard(
    onAddExpense: () -> Unit = {},
    onAddIncome: () -> Unit = {},
    onBalanceClick: () -> Unit = {}
) {
    
    val semesterName = "HELB Semester Saver"
    val semesterDate = "September 25, 2024"
    val budgetHealth = "Good"
    val budgetProgress = 0.72f // 72%
    val daysLeft = 50
    val dailyAllowance = 750.0
    val currentBalance = 15200.0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ChumzPlanColors.DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Space for bottom nav
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

                // Circular Progress Ring
                BudgetHealthRing(
                    progress = budgetProgress,
                    budgetHealth = budgetHealth,
                    daysLeft = daysLeft
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
        // App Logo
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = ChumzPlanColors.TextWhite)) {
                    append("stretch ")
                }
                withStyle(SpanStyle(color = ChumzPlanColors.AccentGreen)) {
                    append("it")
                }
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // Semester Info
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = semesterName,
                color = ChumzPlanColors.TextWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = semesterDate,
                color = ChumzPlanColors.TextGray,
                fontSize = 11.sp
            )
        }

        // Menu Icon
        IconButton(onClick = { /* Open menu */ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Menu",
                tint = ChumzPlanColors.TextWhite
            )
        }
    }
}

@Composable
fun BudgetHealthRing(
    progress: Float,
    budgetHealth: String,
    daysLeft: Int
) {
    // Animated progress
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val healthColor = when {
        progress > 0.6f -> ChumzPlanColors.GoodGreen
        progress > 0.3f -> ChumzPlanColors.WarningOrange
        else -> ChumzPlanColors.DangerRed
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(260.dp)
    ) {
        // Background Circle
        Canvas(modifier = Modifier.size(260.dp)) {
            drawArc(
                color = ChumzPlanColors.CardBackground,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
        }

        // Progress Arc
        Canvas(modifier = Modifier.size(260.dp)) {
            drawArc(
                brush = Brush.linearGradient(
                    colors = listOf(
                        healthColor,
                        healthColor.copy(alpha = 0.6f)
                    )
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
        }

        // Center Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Budget Health:",
                color = ChumzPlanColors.TextGray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = budgetHealth,
                    color = ChumzPlanColors.TextWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "😊",
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Days Left: $daysLeft",
                color = ChumzPlanColors.TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Decorative dots (pagination dots from mockup)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == 0) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == 0) ChumzPlanColors.AccentGreen
                            else ChumzPlanColors.TextGray.copy(alpha = 0.3f)
                        )
                )
            }
        }
    }
}

@Composable
fun DailyAllowanceCard(amount: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = ChumzPlanColors.WhiteCard
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "You can spend Allowance",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "KES ${amount.toInt()}",
                        color = Color.Black,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "today 🔥",
                        color = Color.Black,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CurrentBalanceCard(
    balance: Double,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        colors = CardDefaults.cardColors(
            containerColor = ChumzPlanColors.WhiteCard
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
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
fun ActionButtonsRow(
    onAddExpense: () -> Unit,
    onAddIncome: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = ChumzPlanColors.WhiteCard
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Add Expense Button
            TextButton(
                onClick = onAddExpense,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = ChumzPlanColors.AccentGreen
                )
            ) {
                Text(
                    text = "Add Expense",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Center FAB
            FloatingActionButton(
                onClick = onAddExpense,
                containerColor = ChumzPlanColors.AccentGreen,
                contentColor = Color.White,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(32.dp)
                )
            }

            // Add Income Button
            TextButton(
                onClick = onAddIncome,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Add Income",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    selectedItem: Int = 0,
    onItemSelected: (Int) -> Unit = {}
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        containerColor = ChumzPlanColors.CardBackground,
        tonalElevation = 8.dp
    ) {
        // Dashboard
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Dashboard"
                )
            },
            label = {
                Text(
                    text = "Dashboard",
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ChumzPlanColors.AccentGreen,
                selectedTextColor = ChumzPlanColors.AccentGreen,
                unselectedIconColor = ChumzPlanColors.TextGray,
                unselectedTextColor = ChumzPlanColors.TextGray,
                indicatorColor = ChumzPlanColors.AccentGreen.copy(alpha = 0.1f)
            )
        )

        // Transactions
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = "Transactions"
                )
            },
            label = {
                Text(
                    text = "Transactions",
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ChumzPlanColors.AccentGreen,
                selectedTextColor = ChumzPlanColors.AccentGreen,
                unselectedIconColor = ChumzPlanColors.TextGray,
                unselectedTextColor = ChumzPlanColors.TextGray,
                indicatorColor = ChumzPlanColors.AccentGreen.copy(alpha = 0.1f)
            )
        )

        // Reports
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) },
            icon = {
                Icon(
                    imageVector = Icons.Default.PieChart,
                    contentDescription = "Reports"
                )
            },
            label = {
                Text(
                    text = "Reports",
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ChumzPlanColors.AccentGreen,
                selectedTextColor = ChumzPlanColors.AccentGreen,
                unselectedIconColor = ChumzPlanColors.TextGray,
                unselectedTextColor = ChumzPlanColors.TextGray,
                indicatorColor = ChumzPlanColors.AccentGreen.copy(alpha = 0.1f)
            )
        )

        // Profile
        NavigationBarItem(
            selected = selectedItem == 3,
            onClick = { onItemSelected(3) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            },
            label = {
                Text(
                    text = "Profile",
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ChumzPlanColors.AccentGreen,
                selectedTextColor = ChumzPlanColors.AccentGreen,
                unselectedIconColor = ChumzPlanColors.TextGray,
                unselectedTextColor = ChumzPlanColors.TextGray,
                indicatorColor = ChumzPlanColors.AccentGreen.copy(alpha = 0.1f)
            )
        )
    }
}


@Composable
fun PreviewChumzPlanDashboard() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ChumzPlanColors.DarkBackground)
    ) {
        ChumzPlanDashboard()
    }
}