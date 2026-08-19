package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Currency
import com.example.data.model.TaskCategory
import com.example.data.model.TaskStatus
import com.example.ui.components.BalanceHeroCard
import com.example.ui.components.CategoryFilterRow
import com.example.ui.components.SecurityStatusBanner
import com.example.ui.components.StreakTrackerCard
import com.example.ui.components.TaskItemCard
import com.example.ui.theme.GeoBgDark
import com.example.ui.theme.GeoBorderDark
import com.example.ui.theme.GeoBorderMuted
import com.example.ui.theme.GeoPrimary
import com.example.ui.theme.GeoPrimaryDark
import com.example.ui.theme.GeoSurfaceDark
import com.example.ui.theme.GeoSurfaceElevated
import com.example.ui.theme.GeoTextMuted
import com.example.ui.theme.GeoTextPrimary
import com.example.ui.theme.GeoTextWhite
import com.example.ui.viewmodel.TaskUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: TaskUiState,
    onCategorySelect: (TaskCategory) -> Unit,
    onStatusFilterSelect: (TaskStatus?) -> Unit,
    onSearchChange: (String) -> Unit,
    onTaskClick: (String) -> Unit,
    onCurrencySelect: (Currency) -> Unit,
    onWithdrawClick: () -> Unit,
    onStreakClaim: (Int, Int) -> Unit,
    onSecurityAuditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userName = uiState.userProfile?.name ?: "Alex Rivera"
    val userInitials = rememberInitials(userName)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBgDark)
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Geometric Balance Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "GOOD MORNING",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextMuted,
                            letterSpacing = 1.5.sp,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )
                    }

                    // Avatar Circle (w-11 h-11 rounded-full bg-[#49454F] border border-[#938F99])
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(GeoSurfaceElevated)
                            .clickable { onSecurityAuditClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.Transparent,
                            border = BorderStroke(1.dp, GeoBorderMuted),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = userInitials,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Black,
                                    color = GeoPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Geometric Balance Hero Card
                BalanceHeroCard(
                    userProfile = uiState.userProfile,
                    selectedCurrency = uiState.selectedCurrency,
                    onCurrencySelect = onCurrencySelect,
                    onWithdrawClick = onWithdrawClick,
                    onStreakClick = onSecurityAuditClick
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Streak Tracker
                StreakTrackerCard(
                    currentStreak = uiState.userProfile?.streakDays ?: 1,
                    onClaimClick = onStreakClaim
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Anti-Fraud Protection Status Bar (matching design HTML bottom shield card)
                SecurityStatusBanner(
                    auditResult = uiState.securityAudit,
                    onClick = onSecurityAuditClick
                )
            }
        }

        // Promotional Campaign Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                border = BorderStroke(1.dp, GeoBorderDark)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.hero_earn_banner_1787159437235),
                        contentDescription = "Earn Rewards Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(125.dp),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(125.dp)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(16.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = GeoPrimary
                            ) {
                                Text(
                                    text = "SPECIAL CAMPAIGN",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = GeoPrimaryDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Earn up to 10,000 pts ($10) today!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Search and Available Tasks Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                // Search field
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .testTag("task_search_input"),
                    placeholder = { Text("Search tasks, surveys, games...", color = GeoTextMuted) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = GeoTextMuted
                        )
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = GeoTextMuted)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GeoPrimary,
                        unfocusedBorderColor = GeoBorderDark,
                        unfocusedContainerColor = GeoSurfaceDark,
                        focusedContainerColor = GeoSurfaceDark
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Category Chips
                CategoryFilterRow(
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = onCategorySelect
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Task List Section Title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AVAILABLE TASKS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = GeoPrimary,
                        letterSpacing = 2.sp,
                        fontSize = 12.sp
                    )

                    Row {
                        TextButton(
                            onClick = { onStatusFilterSelect(null) }
                        ) {
                            Text(
                                text = if (uiState.selectedStatusFilter == null) "See all" else "Clear Filter",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = GeoTextMuted
                            )
                        }
                    }
                }
            }
        }

        // Task Items List
        if (uiState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GeoPrimary)
                }
            }
        } else if (uiState.filteredTasks.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                    border = BorderStroke(1.dp, GeoBorderDark)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = GeoTextMuted,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No tasks found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )
                        Text(
                            text = "Try switching categories or clearing search filters.",
                            style = MaterialTheme.typography.bodySmall,
                            color = GeoTextMuted
                        )
                    }
                }
            }
        } else {
            items(uiState.filteredTasks, key = { it.id }) { task ->
                TaskItemCard(
                    task = task,
                    onClick = { onTaskClick(task.id) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
        }
    }
}

fun rememberInitials(name: String): String {
    val parts = name.trim().split(" ").filter { it.isNotBlank() }
    return when {
        parts.size >= 2 -> "${parts[0].first().uppercaseChar()}${parts[1].first().uppercaseChar()}"
        parts.isNotEmpty() -> parts[0].take(2).uppercase()
        else -> "AR"
    }
}
