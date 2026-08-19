package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.TaskEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.Currency
import com.example.data.model.PayoutMethod
import com.example.data.model.SecurityAuditResult
import com.example.data.model.TaskCategory
import com.example.data.model.TaskStatus
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.EmeraldSuccessLight
import com.example.ui.theme.GoldReward
import com.example.ui.theme.GoldRewardDark
import com.example.ui.theme.GoldRewardLight
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.PrimaryIndigoLight
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.VioletAccent

@Composable
fun TaskItemCard(
    task: TaskEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("task_card_${task.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            1.dp,
            if (task.isFeatured) GoldReward.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category & Icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                getCategoryColor(task.category).copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getCategoryIcon(task.category),
                            contentDescription = task.category.displayName,
                            tint = getCategoryColor(task.category),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = task.category.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                        if (task.isFeatured) {
                            Text(
                                text = "🔥 Featured Bonus",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldRewardDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Points Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = GoldRewardLight.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, GoldReward.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = "Points",
                            tint = GoldRewardDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+${task.pointsReward} pts",
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldRewardDark,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Duration",
                        modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "~${task.durationMinutes} mins",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                StatusBadge(status = task.status)
            }
        }
    }
}

@Composable
fun StatusBadge(status: TaskStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor, icon) = when (status) {
        TaskStatus.AVAILABLE -> Triple(
            PrimaryIndigo.copy(alpha = 0.12f),
            PrimaryIndigo,
            Icons.Default.Bolt
        )
        TaskStatus.IN_REVIEW -> Triple(
            GoldReward.copy(alpha = 0.18f),
            GoldRewardDark,
            Icons.Default.HourglassTop
        )
        TaskStatus.COMPLETED -> Triple(
            EmeraldSuccess.copy(alpha = 0.18f),
            EmeraldSuccess,
            Icons.Default.CheckCircle
        )
        TaskStatus.REJECTED -> Triple(
            RoseDanger.copy(alpha = 0.18f),
            RoseDanger,
            Icons.Default.Warning
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = status.displayName,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BalanceHeroCard(
    userProfile: UserProfileEntity?,
    selectedCurrency: Currency,
    onCurrencySelect: (Currency) -> Unit,
    onWithdrawClick: () -> Unit,
    onStreakClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val points = userProfile?.balancePoints ?: 0
    val convertedAmount = when (selectedCurrency) {
        Currency.USD -> points / 1000.0
        Currency.INR -> points / 12.0
        Currency.EUR -> points / 1080.0
    }
    val symbol = selectedCurrency.symbol

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("balance_hero_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            PrimaryIndigo,
                            Color(0xFF3730A3),
                            Color(0xFF1E1B4B)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "AVAILABLE BALANCE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.75f),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = "Points",
                                tint = GoldRewardLight,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "%,d".format(points),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "pts",
                                style = MaterialTheme.typography.titleMedium,
                                color = GoldRewardLight,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Currency Switcher
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.15f)
                    ) {
                        Row(modifier = Modifier.padding(4.dp)) {
                            Currency.entries.forEach { curr ->
                                val isSelected = curr == selectedCurrency
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) Color.White else Color.Transparent)
                                        .clickable { onCurrencySelect(curr) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = curr.code,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) PrimaryIndigo else Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Real-time Cash Valuation
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "≈ $symbol${String.format("%.2f", convertedAmount)} ${selectedCurrency.code}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSuccessLight
                    )

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        modifier = Modifier.clickable { onStreakClick() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Streak",
                                tint = GoldRewardLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Day ${userProfile?.streakDays ?: 1} Streak",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Payout CTA Button
                Button(
                    onClick = onWithdrawClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("withdraw_payout_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldReward,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Withdraw",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Redeem & Withdraw Payout",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StreakTrackerCard(
    currentStreak: Int,
    onClaimClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val streakDays = listOf(
        Pair(1, 50),
        Pair(2, 75),
        Pair(3, 100),
        Pair(4, 150),
        Pair(5, 200),
        Pair(6, 250),
        Pair(7, 500)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("streak_tracker_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Daily Streak",
                        tint = GoldReward,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Daily 7-Day Reward Streak",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "$currentStreak / 7 Days",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryIndigoLight
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                items(streakDays) { (day, pts) ->
                    val isPast = day < currentStreak
                    val isCurrent = day == currentStreak
                    val isLocked = day > currentStreak

                    val itemBg = when {
                        isCurrent -> PrimaryIndigo
                        isPast -> EmeraldSuccess.copy(alpha = 0.2f)
                        else -> MaterialTheme.colorScheme.surface
                    }
                    val textColor = when {
                        isCurrent -> Color.White
                        isPast -> EmeraldSuccess
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = itemBg),
                        border = BorderStroke(
                            1.dp,
                            if (isCurrent) GoldReward else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .width(52.dp)
                            .clickable(enabled = isCurrent) {
                                onClaimClick(day, pts)
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "D$day",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Icon(
                                imageVector = if (isPast) Icons.Default.CheckCircle else Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = if (isCurrent) GoldRewardLight else if (isPast) EmeraldSuccess else GoldReward,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "+$pts",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = textColor,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryFilterRow(
    selectedCategory: TaskCategory,
    onCategorySelected: (TaskCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(TaskCategory.entries) { category ->
            val isSelected = category == selectedCategory
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = { Text(category.displayName, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                leadingIcon = {
                    Icon(
                        imageVector = getCategoryIcon(category),
                        contentDescription = category.displayName,
                        modifier = Modifier.size(16.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryIndigo,
                    selectedLabelColor = Color.White,
                    selectedLeadingIconColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("filter_chip_${category.name}")
            )
        }
    }
}

@Composable
fun SecurityStatusBanner(
    auditResult: SecurityAuditResult?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSafe = auditResult?.isSafeToEarn ?: true
    val score = auditResult?.integrityScore ?: 100

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("security_status_banner"),
        shape = RoundedCornerShape(14.dp),
        color = if (isSafe) EmeraldSuccess.copy(alpha = 0.12f) else RoseDanger.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, if (isSafe) EmeraldSuccess.copy(alpha = 0.4f) else RoseDanger.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = if (isSafe) Icons.Default.VerifiedUser else Icons.Default.Warning,
                    contentDescription = "Security Audit",
                    tint = if (isSafe) EmeraldSuccess else RoseDanger,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isSafe) "Device Integrity Passed ($score%)" else "Anti-Fraud Alert ($score%)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isSafe) EmeraldSuccess else RoseDanger
                    )
                    Text(
                        text = if (isSafe) "No root or VPN proxy detected • Rewards active" else "VPN/Root active • Payout verification locked",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            TextButton(onClick = onClick) {
                Text("Audit", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProofSubmitDialog(
    task: TaskEntity,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (proofUrl: String, proofNote: String, screenshotPath: String?) -> Unit
) {
    var proofUrl by remember { mutableStateOf("") }
    var proofNote by remember { mutableStateOf("") }
    var hasSimulatedScreenshot by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("proof_submission_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.UploadFile,
                        contentDescription = null,
                        tint = PrimaryIndigo,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Submit Task Proof",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "+${task.pointsReward} Points Reward",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldRewardDark,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Requirement:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = task.proofRequirement,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Screenshot Attacher / Picker simulation
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { hasSimulatedScreenshot = !hasSimulatedScreenshot }
                        .testTag("upload_screenshot_box"),
                    color = if (hasSimulatedScreenshot) EmeraldSuccess.copy(alpha = 0.15f) else PrimaryIndigo.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, if (hasSimulatedScreenshot) EmeraldSuccess else PrimaryIndigo.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (hasSimulatedScreenshot) Icons.Default.CheckCircle else Icons.Default.UploadFile,
                            contentDescription = "Attach Screenshot",
                            tint = if (hasSimulatedScreenshot) EmeraldSuccess else PrimaryIndigo,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (hasSimulatedScreenshot) "Screenshot Attached (task_proof_01.png)" else "Tap to attach screenshot proof",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (hasSimulatedScreenshot) EmeraldSuccess else PrimaryIndigo
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = proofUrl,
                    onValueChange = { proofUrl = it },
                    label = { Text("Proof Link / Username / Code") },
                    placeholder = { Text("e.g. https://... or @username or code") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("proof_url_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = proofNote,
                    onValueChange = { proofNote = it },
                    label = { Text("Additional Notes (Optional)") },
                    placeholder = { Text("Completed KYC and verified profile.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("proof_note_input"),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSubmit(
                                if (proofUrl.isBlank()) "https://taskearn.app/proof/auto_${task.id}" else proofUrl,
                                proofNote,
                                if (hasSimulatedScreenshot) "local/images/task_proof_01.png" else null
                            )
                        },
                        enabled = !isSubmitting,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                        modifier = Modifier.testTag("submit_proof_confirm_btn")
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Submit for Review", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PayoutRequestDialog(
    userBalance: Int,
    currency: Currency,
    isRequesting: Boolean,
    onDismiss: () -> Unit,
    onRequest: (method: PayoutMethod, account: String, points: Int) -> Unit
) {
    var selectedMethod by remember { mutableStateOf(PayoutMethod.PAYPAL) }
    var accountAddress by remember { mutableStateOf("") }
    var selectedPoints by remember { mutableIntStateOf(selectedMethod.minPoints) }

    val equivalent = when (currency) {
        Currency.USD -> selectedPoints / 1000.0
        Currency.INR -> selectedPoints / 12.0
        Currency.EUR -> selectedPoints / 1080.0
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("payout_request_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = GoldReward,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Request Payout",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Balance: %,d pts".format(userBalance),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select Payout Method:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(PayoutMethod.entries) { method ->
                        val isSelected = method == selectedMethod
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedMethod = method
                                if (selectedPoints < method.minPoints) {
                                    selectedPoints = method.minPoints
                                }
                            },
                            label = { Text(method.title.split(" ")[0], fontSize = 12.sp) },
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = accountAddress,
                    onValueChange = { accountAddress = it },
                    label = {
                        Text(
                            when (selectedMethod) {
                                PayoutMethod.PAYPAL -> "PayPal Email Address"
                                PayoutMethod.UPI -> "UPI VPA ID (e.g. name@okhdfcbank)"
                                PayoutMethod.AMAZON_GIFT -> "Email for Amazon Voucher"
                                PayoutMethod.PLAY_STORE -> "Email for Play Store Code"
                                PayoutMethod.CRYPTO_USDT -> "USDT (TRC20) Wallet Address"
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("payout_account_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select Points to Redeem:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                val pointTiers = listOf(
                    selectedMethod.minPoints,
                    selectedMethod.minPoints * 2,
                    selectedMethod.minPoints * 5
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pointTiers.forEach { pts ->
                        val isSelected = pts == selectedPoints
                        val canAfford = userBalance >= pts

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable(enabled = canAfford) { selectedPoints = pts },
                            color = if (isSelected) PrimaryIndigo else if (canAfford) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            border = BorderStroke(1.dp, if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "%,d".format(pts),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${currency.symbol}${String.format("%.2f", when (currency) {
                                        Currency.USD -> pts / 1000.0
                                        Currency.INR -> pts / 12.0
                                        Currency.EUR -> pts / 1080.0
                                    })}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) GoldRewardLight else EmeraldSuccess
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Summary Box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EmeraldSuccess.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "You Will Receive:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${currency.symbol}${String.format("%.2f", equivalent)} ${currency.code}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldSuccess
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val account = if (accountAddress.isBlank()) "user.payout@example.com" else accountAddress
                            onRequest(selectedMethod, account, selectedPoints)
                        },
                        enabled = !isRequesting && userBalance >= selectedPoints,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldReward, contentColor = Color.Black),
                        modifier = Modifier.testTag("confirm_payout_btn")
                    ) {
                        if (isRequesting) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.Black, strokeWidth = 2.dp)
                        } else {
                            Text("Confirm Withdrawal", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AuthDialog(
    onDismiss: () -> Unit,
    onLogin: (name: String, email: String, isGoogle: Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailMode by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("auth_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(PrimaryIndigo.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = PrimaryIndigo,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Welcome to TaskEarn",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Sign in to securely sync your reward wallet and submit proofs.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Google One Tap Button
                Button(
                    onClick = {
                        onLogin("Alex Rivera (Google)", "alex.rivera@gmail.com", true)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("google_signin_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = "Google",
                        tint = PrimaryIndigo,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Continue with Google One Tap", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        text = "  OR  ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (isEmailMode) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val loginEmail = if (email.isBlank()) "user@example.com" else email
                        val loginName = if (name.isBlank()) "Task Earner" else name
                        onLogin(loginName, loginEmail, false)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("email_auth_submit_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
                ) {
                    Text(if (isEmailMode) "Create Account" else "Sign In with Email", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { isEmailMode = !isEmailMode }) {
                    Text(
                        text = if (isEmailMode) "Already have an account? Sign In" else "New earner? Create an account",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun SecurityAuditDialog(
    auditResult: SecurityAuditResult?,
    onDismiss: () -> Unit
) {
    val audit = auditResult ?: return

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("security_audit_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = if (audit.isSafeToEarn) EmeraldSuccess else RoseDanger,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Anti-Fraud & Security Audit",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Integrity Score: ${audit.integrityScore}/100",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (audit.isSafeToEarn) EmeraldSuccess else RoseDanger,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { audit.integrityScore / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = if (audit.isSafeToEarn) EmeraldSuccess else RoseDanger,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                SecurityCheckItem(
                    title = "Root Access Detection",
                    detail = if (audit.isRooted) "Superuser / su binaries found (Risk)" else "Clean genuine Android environment",
                    isPass = !audit.isRooted
                )

                Spacer(modifier = Modifier.height(10.dp))

                SecurityCheckItem(
                    title = "VPN / Proxy Tunnel Check",
                    detail = if (audit.isVpnOrProxy) "Active VPN transport detected (Restricted)" else "Direct legitimate ISP connection",
                    isPass = !audit.isVpnOrProxy
                )

                Spacer(modifier = Modifier.height(10.dp))

                SecurityCheckItem(
                    title = "Hardware Fingerprint Token",
                    detail = "Device ID: ${audit.deviceId} (Multi-account prevention OK)",
                    isPass = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = audit.statusSummary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
                ) {
                    Text("Done")
                }
            }
        }
    }
}

@Composable
fun SecurityCheckItem(
    title: String,
    detail: String,
    isPass: Boolean
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isPass) EmeraldSuccess.copy(alpha = 0.08f) else RoseDanger.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, if (isPass) EmeraldSuccess.copy(alpha = 0.25f) else RoseDanger.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isPass) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                tint = if (isPass) EmeraldSuccess else RoseDanger,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun getCategoryIcon(category: TaskCategory): ImageVector {
    return when (category) {
        TaskCategory.ALL -> Icons.Default.Bolt
        TaskCategory.APP_DOWNLOAD -> Icons.Default.Download
        TaskCategory.VIDEO_ADS -> Icons.Default.PlayCircle
        TaskCategory.SURVEY -> Icons.Default.Poll
        TaskCategory.SOCIAL -> Icons.Default.Share
        TaskCategory.QUICK_POLL -> Icons.Default.CheckCircle
    }
}

fun getCategoryColor(category: TaskCategory): Color {
    return when (category) {
        TaskCategory.ALL -> PrimaryIndigo
        TaskCategory.APP_DOWNLOAD -> CyanAccent
        TaskCategory.VIDEO_ADS -> RoseDanger
        TaskCategory.SURVEY -> VioletAccent
        TaskCategory.SOCIAL -> PrimaryIndigoLight
        TaskCategory.QUICK_POLL -> EmeraldSuccess
    }
}
