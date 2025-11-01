package com.example.chumzplan.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen1(
    onNext: (name: String, age: String, university: String) -> Unit,
    onSkip: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var university by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E4D5C))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))
            StepIndicator(currentStep = 1, totalSteps = 3)

            Spacer(modifier = Modifier.height(15.dp))


            Text(
                text = "Tell us about\nyourself",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(14.dp))


            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color(0xFF4DD0E1),
                modifier = Modifier.size(42.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))


            OnboardingTextField(
                value = name,
                onValueChange = { name = it },
                label = "Your Name",
                placeholder = "e.g Alvin Muriithi",
                leadingIcon = Icons.Default.Person,
                )

            Spacer(modifier = Modifier.height(14.dp))

            OnboardingTextField(
                value = age,
                onValueChange = { age = it },
                label = "Your Age",
                placeholder = "20",
                leadingIcon = Icons.Default.Cake,
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(14.dp))

            OnboardingTextField(
                value = university,
                onValueChange = { university = it },
                label = "University/College",
                placeholder = "e.g University of Nairobi",
                leadingIcon = Icons.Default.School
            )

            Spacer(modifier = Modifier.height(14.dp))


            Button(
                onClick = { onNext(name, age, university) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4DD0E1) // Cyan
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = name.isNotBlank() && age.isNotBlank() && university.isNotBlank()
            ) {
                Text(
                    text = "Next",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            TextButton(onClick = onSkip) {
                Text(
                    text = "Skip for now",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen2(
    onNext: (loanAmount: String, startDate: String, endDate: String) -> Unit,
    onSkip: () -> Unit
) {
    var loanAmount by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("August 20, 2024") }
    var endDate by remember { mutableStateOf("December 15, 2024") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E4D5C)) // Light blue background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))
            StepIndicator(currentStep = 2, totalSteps = 3)

            Spacer(modifier = Modifier.height(40.dp))


            Text(
                text = "Tell us about your\nsemester",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(32.dp))


            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = Color(0xFF4DD0E1),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Total HELB Loan Amount",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1E4D5C)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = loanAmount,
                            onValueChange = { loanAmount = it },
                            placeholder = { Text("KES 45,000") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4DD0E1),
                                unfocusedBorderColor = Color.LightGray
                            )
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Semester Start Date
            LightDateField(
                label = "Semester Start Date",
                value = startDate,
                onValueChange = { startDate = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Semester End Date
            LightDateField(
                label = "Semester End Date",
                value = endDate,
                onValueChange = { endDate = it }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Next Button
            Button(
                onClick = { onNext(loanAmount, startDate, endDate) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4DD0E1)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = loanAmount.isNotBlank()
            ) {
                Text(
                    text = "Next",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            TextButton(onClick = onSkip) {
                Text(
                    text = "Skip for now",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ============================================
// SCREEN 3: Budget Categories
// ============================================

data class BudgetCategory(
    val id: Int,
    val name: String,
    val icon: String,
    var amount: Double,
    var isSelected: Boolean = true
)

@Composable
fun OnboardingScreen3(
    totalAmount: Double = 45000.0,
    onComplete: (categories: List<BudgetCategory>) -> Unit,
    onSkip: () -> Unit
) {
    var categories by remember {
        mutableStateOf(
            listOf(
                BudgetCategory(1, "Accommodation", "🏠", 8000.0, true),
                BudgetCategory(2, "Food", "🍽️", 0.0, true),
                BudgetCategory(3, "Transport", "🚌", 6000.0, true),
                BudgetCategory(4, "Books & Stationery", "📚", 4000.0, true),
                BudgetCategory(5, "Airtime & Internet", "📱", 2000.0, true)
            )
        )
    }

    val totalAllocated = categories.filter { it.isSelected }.sumOf { it.amount }
    val remainingFunds = totalAmount - totalAllocated

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E4D5C))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step Indicator
            Spacer(modifier = Modifier.height(32.dp))
            StepIndicator(currentStep = 3, totalSteps = 3)

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Set Your Priority\nCategories",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Allocated: KES ${totalAllocated.toInt()}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Remaining Funds: KES ${remainingFunds.toInt()}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (remainingFunds >= 0) Color(0xFF4CAF50) else Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { (totalAllocated / totalAmount).toFloat().coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color(0xFF4DD0E1),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Categories List
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                categories.forEachIndexed { index, category ->
                    CategoryCard(
                        category = category,
                        onAmountChange = { newAmount ->
                            categories = categories.toMutableList().apply {
                                this[index] = category.copy(amount = newAmount)
                            }
                        },
                        onToggle = {
                            categories = categories.toMutableList().apply {
                                this[index] = category.copy(isSelected = !category.isSelected)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Custom Category Button
            OutlinedButton(
                onClick = { /* Add custom category */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF4DD0E1)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF4DD0E1))
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Custom Category")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Complete Button
            Button(
                onClick = { onComplete(categories.filter { it.isSelected }) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4DD0E1)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Skip Button
            TextButton(onClick = onSkip) {
                Text(
                    text = "Skip for now",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
            }
        }
    }
}

// ============================================
// REUSABLE COMPONENTS
// ============================================

@Composable
fun StepIndicator(currentStep: Int, totalSteps: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Step",
            color = Color.White,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        repeat(totalSteps) { index ->
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = if (index + 1 == currentStep) Color(0xFF4DD0E1) else Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(6.dp)
                    )
            )
            if (index < totalSteps - 1) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "of $totalSteps",
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF1E4D5C)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4DD0E1),
                    unfocusedBorderColor = Color.LightGray
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightDateField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null,
                tint = Color(0xFF4DD0E1),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E4D5C)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    category: BudgetCategory,
    onAmountChange: (Double) -> Unit,
    onToggle: () -> Unit
) {
    var amountText by remember { mutableStateOf(category.amount.toInt().toString()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (category.isSelected) Color.White else Color.White.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.icon,
                fontSize = 28.sp,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                if (category.isSelected) {
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = {
                            amountText = it
                            onAmountChange(it.toDoubleOrNull() ?: 0.0)
                        },
                        placeholder = { Text("KES 0") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                } else {
                    Text(
                        text = "KES ${category.amount.toInt()}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (category.isSelected) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}