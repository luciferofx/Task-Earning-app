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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Videocam
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.ui.theme.GeoBgDark
import com.example.ui.theme.GeoBorderDark
import com.example.ui.theme.GeoCategoryDailyBg
import com.example.ui.theme.GeoCategoryDailyFg
import com.example.ui.theme.GeoCategoryGamingBg
import com.example.ui.theme.GeoCategoryGamingFg
import com.example.ui.theme.GeoCategorySocialBg
import com.example.ui.theme.GeoCategorySocialFg
import com.example.ui.theme.GeoCategorySurveyBg
import com.example.ui.theme.GeoCategorySurveyFg
import com.example.ui.theme.GeoCategoryVideoBg
import com.example.ui.theme.GeoCategoryVideoFg
import com.example.ui.theme.GeoDangerRed
import com.example.ui.theme.GeoGoldAccent
import com.example.ui.theme.GeoOnPrimaryContainer
import com.example.ui.theme.GeoPrimary
import com.example.ui.theme.GeoPrimaryContainer
import com.example.ui.theme.GeoPrimaryDark
import com.example.ui.theme.GeoSuccessGreen
import com.example.ui.theme.GeoSuccessContainer
import com.example.ui.theme.GeoSurfaceDark
import com.example.ui.theme.GeoSurfaceElevated
import com.example.ui.theme.GeoTextMuted
import com.example.ui.theme.GeoTextPrimary
import com.example.ui.theme.GeoTextWhite

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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
        border = BorderStroke(
            1.dp,
            if (task.isFeatured) GeoPrimary.copy(alpha = 0.6f) else GeoBorderDark
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
                // Category & Icon in Geometric Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(getCategoryBgColor(task.category)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getCategoryIcon(task.category),
                            contentDescription = task.category.displayName,
                            tint = getCategoryFgColor(task.category),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = GeoTextWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = GeoTextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Geometric Points Display
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "+${task.pointsReward}",
                        fontWeight = FontWeight.Black,
                        color = GeoPrimary,
                        fontSize = 17.sp,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "pts",
                        style = MaterialTheme.typography.labelSmall,
                        color = GeoPrimary.copy(alpha = 0.75f),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = GeoBorderDark.copy(alpha = 0.6f))

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
                        modifier = Modifier.size(14.dp),
                        tint = GeoTextMuted
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "~${task.durationMinutes} mins • ${task.category.displayName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GeoTextMuted
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
            GeoPrimaryContainer.copy(alpha = 0.2f),
            GeoPrimary,
            Icons.Default.Bolt
        )
        TaskStatus.IN_REVIEW -> Triple(
            GeoGoldAccent.copy(alpha = 0.2f),
            GeoGoldAccent,
            Icons.Default.HourglassTop
        )
        TaskStatus.COMPLETED -> Triple(
            GeoSuccessGreen.copy(alpha = 0.2f),
            GeoSuccessGreen,
            Icons.Default.CheckCircle
        )
        TaskStatus.REJECTED -> Triple(
            GeoDangerRed.copy(alpha = 0.2f),
            GeoDangerRed,
            Icons.Default.Warning
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = status.displayName,
                tint = textColor,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
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
    val points = userProfile?.balancePoints ?: 2450
    val convertedAmount = when (selectedCurrency) {
        Currency.USD -> points / 100.0
        Currency.INR -> points / 1.2
        Currency.EUR -> points / 108.0
    }
    val symbol = selectedCurrency.symbol

    // Daily Goal progress calculation
    val dailyGoal = 3000
    val dailyEarned = 1850
    val goalProgress = (dailyEarned.toFloat() / dailyGoal.toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("balance_hero_card"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = GeoPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Top row: Balance Title + Converted Cash Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Current Balance",
                        style = MaterialTheme.typography.labelLarge,
                        color = GeoPrimaryDark.copy(alpha = 0.85f),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "%,d".format(points),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            color = GeoPrimaryDark,
                            fontSize = 38.sp,
                            letterSpacing = (-1).sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "pts",
                            style = MaterialTheme.typography.titleMedium,
                            color = GeoPrimaryDark.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                }

                // Geometric Pill for Fiat Conversion
                Surface(
                    shape = CircleShape,
                    color = GeoPrimaryDark.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, GeoPrimaryDark.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "≈ $symbol${String.format("%.2f", convertedAmount)}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = GeoPrimaryDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sleek Goal Progress Bar (From Geometric Balance design)
            LinearProgressIndicator(
                progress = { goalProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = GeoPrimaryDark,
                trackColor = GeoPrimaryDark.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Goal Progress Caption
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DAILY GOAL",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = GeoPrimaryDark.copy(alpha = 0.8f),
                    letterSpacing = 1.5.sp,
                    fontSize = 10.sp
                )
                Text(
                    text = "%,d / %,d pts".format(dailyEarned, dailyGoal),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = GeoPrimaryDark,
                    letterSpacing = 1.sp,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Currency Selector & Payout Redeem CTA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Currency Switcher
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = GeoPrimaryDark.copy(alpha = 0.12f)
                ) {
                    Row(modifier = Modifier.padding(3.dp)) {
                        Currency.entries.forEach { curr ->
                            val isSelected = curr == selectedCurrency
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) GeoPrimaryDark else Color.Transparent)
                                    .clickable { onCurrencySelect(curr) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = curr.code,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                    color = if (isSelected) GeoPrimary else GeoPrimaryDark
                                )
                            }
                        }
                    }
                }

                // Withdraw Button
                Button(
                    onClick = onWithdrawClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GeoPrimaryDark,
                        contentColor = GeoPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("withdraw_payout_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Withdraw",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
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
        colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
        border = BorderStroke(1.dp, GeoBorderDark)
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
                        tint = GeoPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Daily Reward Streak",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GeoTextWhite
                    )
                }

                Text(
                    text = "$currentStreak / 7 Days",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = GeoPrimary
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

                    val itemBg = when {
                        isCurrent -> GeoPrimary
                        isPast -> GeoSuccessContainer
                        else -> GeoSurfaceElevated
                    }
                    val textColor = when {
                        isCurrent -> GeoPrimaryDark
                        isPast -> GeoSuccessGreen
                        else -> GeoTextMuted
                    }

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = itemBg),
                        border = BorderStroke(
                            1.dp,
                            if (isCurrent) GeoPrimaryContainer else GeoBorderDark
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
                                tint = if (isCurrent) GeoPrimaryDark else if (isPast) GeoSuccessGreen else GeoPrimary.copy(alpha = 0.6f),
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
                label = {
                    Text(
                        category.displayName,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) GeoPrimaryDark else GeoTextPrimary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = getCategoryIcon(category),
                        contentDescription = category.displayName,
                        modifier = Modifier.size(16.dp),
                        tint = if (isSelected) GeoPrimaryDark else GeoPrimary
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GeoPrimary,
                    containerColor = GeoSurfaceDark
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = if (isSelected) GeoPrimary else GeoBorderDark
                ),
                shape = RoundedCornerShape(14.dp),
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
        shape = RoundedCornerShape(16.dp),
        color = GeoSurfaceElevated,
        border = BorderStroke(1.dp, GeoBorderDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Anti-Fraud Protection Active",
                    tint = GeoPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isSafe) "Anti-Fraud Protection Active" else "Anti-Fraud Alert ($score%)",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = GeoTextWhite
                    )
                    Text(
                        text = if (isSafe) "Device verified • Integrity score $score%" else "Risk detected • Payout verification locked",
                        style = MaterialTheme.typography.bodySmall,
                        color = GeoTextMuted
                    )
                }
            }

            // Green active status dot
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (isSafe) GeoSuccessGreen else GeoDangerRed)
            )
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
            colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
            border = BorderStroke(1.dp, GeoBorderDark),
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
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GeoPrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.UploadFile,
                            contentDescription = null,
                            tint = GeoPrimaryDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Submit Task Proof",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )
                        Text(
                            text = "+${task.pointsReward} Points Reward",
                            style = MaterialTheme.typography.labelSmall,
                            color = GeoPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = GeoSurfaceElevated,
                    border = BorderStroke(1.dp, GeoBorderDark)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Requirement:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextMuted
                        )
                        Text(
                            text = task.proofRequirement,
                            style = MaterialTheme.typography.bodySmall,
                            color = GeoTextWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Screenshot Attacher / Picker simulation
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = GeoSurfaceElevated,
                    border = BorderStroke(1.dp, GeoBorderDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { hasSimulatedScreenshot = !hasSimulatedScreenshot }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (hasSimulatedScreenshot) Icons.Default.CheckCircle else Icons.Default.UploadFile,
                                contentDescription = null,
                                tint = if (hasSimulatedScreenshot) GeoSuccessGreen else GeoPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (hasSimulatedScreenshot) "Screenshot Attached (task_proof.png)" else "Attach Proof Screenshot",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = if (hasSimulatedScreenshot) GeoSuccessGreen else GeoTextWhite
                            )
                        }

                        Text(
                            text = if (hasSimulatedScreenshot) "Change" else "Browse",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = GeoPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = proofUrl,
                    onValueChange = { proofUrl = it },
                    label = { Text("Proof Link / Username / Tx ID") },
                    placeholder = { Text("https://example.com/proof") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("proof_url_field"),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GeoPrimary,
                        unfocusedBorderColor = GeoBorderDark,
                        focusedLabelColor = GeoPrimary,
                        unfocusedContainerColor = GeoSurfaceElevated,
                        focusedContainerColor = GeoSurfaceElevated
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = proofNote,
                    onValueChange = { proofNote = it },
                    label = { Text("Additional Verification Notes (Optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("proof_note_field"),
                    maxLines = 3,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GeoPrimary,
                        unfocusedBorderColor = GeoBorderDark,
                        focusedLabelColor = GeoPrimary,
                        unfocusedContainerColor = GeoSurfaceElevated,
                        focusedContainerColor = GeoSurfaceElevated
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = GeoTextMuted)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val screenshot = if (hasSimulatedScreenshot) "simulated_proof_screenshot.png" else null
                            onSubmit(proofUrl, proofNote, screenshot)
                        },
                        enabled = !isSubmitting,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GeoPrimary,
                            contentColor = GeoPrimaryDark
                        ),
                        modifier = Modifier.testTag("submit_proof_confirm_btn")
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = GeoPrimaryDark
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
    onRequest: (method: PayoutMethod, accountDetail: String, pointsToWithdraw: Int) -> Unit
) {
    var selectedMethod by remember { mutableStateOf(PayoutMethod.PAYPAL) }
    var accountInput by remember { mutableStateOf("") }
    var pointsAmount by remember { mutableIntStateOf(5000) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
            border = BorderStroke(1.dp, GeoBorderDark),
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
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GeoPrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = GeoPrimaryDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Redeem Reward Payout",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )
                        Text(
                            text = "Available: %,d pts".format(userBalance),
                            style = MaterialTheme.typography.labelSmall,
                            color = GeoPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select Payment Destination:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextMuted
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(PayoutMethod.entries) { method ->
                        val isSelected = method == selectedMethod
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedMethod = method
                                pointsAmount = method.minPoints
                            },
                            label = { Text(method.title, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GeoPrimary,
                                selectedLabelColor = GeoPrimaryDark
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = accountInput,
                    onValueChange = { accountInput = it },
                    label = {
                        Text(
                            when (selectedMethod) {
                                PayoutMethod.PAYPAL -> "PayPal Email Address"
                                PayoutMethod.UPI -> "UPI Virtual Payment Address (e.g. name@okhdfcbank)"
                                PayoutMethod.AMAZON_GIFT -> "Email for Amazon e-Gift Code"
                                PayoutMethod.PLAY_STORE -> "Email for Google Play Gift Code"
                                PayoutMethod.CRYPTO_USDT -> "USDT (TRC20 / BEP20) Wallet Address"
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("payout_account_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GeoPrimary,
                        unfocusedBorderColor = GeoBorderDark,
                        focusedLabelColor = GeoPrimary,
                        unfocusedContainerColor = GeoSurfaceElevated,
                        focusedContainerColor = GeoSurfaceElevated
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Points Selection Row
                Text(
                    text = "Points to Redeem (Min %,d pts):".format(selectedMethod.minPoints),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextMuted
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val presets = listOf(5000, 10000, 25000)
                    presets.forEach { pts ->
                        val isSelected = pointsAmount == pts
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) GeoPrimary else GeoSurfaceElevated,
                            border = BorderStroke(1.dp, if (isSelected) GeoPrimaryContainer else GeoBorderDark),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { pointsAmount = pts }
                        ) {
                            Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "%,d pts".format(pts),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) GeoPrimaryDark else GeoTextWhite
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = GeoTextMuted)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onRequest(selectedMethod, accountInput, pointsAmount) },
                        enabled = !isRequesting && accountInput.isNotBlank() && userBalance >= pointsAmount,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GeoPrimary,
                            contentColor = GeoPrimaryDark
                        ),
                        modifier = Modifier.testTag("confirm_payout_button")
                    ) {
                        if (isRequesting) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = GeoPrimaryDark)
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
    var name by remember { mutableStateOf("Alex Rivera") }
    var email by remember { mutableStateOf("alex.rivera@example.com") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
            border = BorderStroke(1.dp, GeoBorderDark),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("auth_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Account Authentication",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextWhite
                )
                Text(
                    text = "Sign in to keep your task earnings and payouts synced.",
                    style = MaterialTheme.typography.bodySmall,
                    color = GeoTextMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Google One Tap Button
                Button(
                    onClick = { onLogin("Alex Rivera (Google)", "alex.rivera@gmail.com", true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("google_signin_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GeoPrimaryContainer,
                        contentColor = GeoOnPrimaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = "Google Sign In",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign in with Google", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = GeoBorderDark)
                    Text(
                        text = " OR ",
                        style = MaterialTheme.typography.labelSmall,
                        color = GeoTextMuted,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = GeoBorderDark)
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Display Name") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GeoPrimary,
                        unfocusedBorderColor = GeoBorderDark,
                        unfocusedContainerColor = GeoSurfaceElevated,
                        focusedContainerColor = GeoSurfaceElevated
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GeoPrimary,
                        unfocusedBorderColor = GeoBorderDark,
                        unfocusedContainerColor = GeoSurfaceElevated,
                        focusedContainerColor = GeoSurfaceElevated
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = GeoTextMuted)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onLogin(name, email, false) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GeoPrimary,
                            contentColor = GeoPrimaryDark
                        )
                    ) {
                        Text("Continue", fontWeight = FontWeight.Bold)
                    }
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
    val audit = auditResult ?: SecurityAuditResult(
        isRooted = false,
        isVpnOrProxy = false,
        deviceId = "GEOM-FGR-99238",
        integrityScore = 100,
        isSafeToEarn = true,
        statusSummary = "All device security checks passed. Environment is safe."
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
            border = BorderStroke(1.dp, GeoBorderDark),
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
                        tint = if (audit.isSafeToEarn) GeoSuccessGreen else GeoDangerRed,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Anti-Fraud & Security Audit",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )
                        Text(
                            text = "Integrity Score: ${audit.integrityScore}/100",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (audit.isSafeToEarn) GeoSuccessGreen else GeoDangerRed,
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
                    color = if (audit.isSafeToEarn) GeoSuccessGreen else GeoDangerRed,
                    trackColor = GeoSurfaceElevated
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
                    color = GeoSurfaceElevated
                ) {
                    Text(
                        text = audit.statusSummary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp),
                        color = GeoTextWhite
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GeoPrimary,
                        contentColor = GeoPrimaryDark
                    )
                ) {
                    Text("Done", fontWeight = FontWeight.Bold)
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
        color = if (isPass) GeoSuccessGreen.copy(alpha = 0.08f) else GeoDangerRed.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, if (isPass) GeoSuccessGreen.copy(alpha = 0.25f) else GeoDangerRed.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isPass) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                tint = if (isPass) GeoSuccessGreen else GeoDangerRed,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextWhite
                )
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = GeoTextMuted
                )
            }
        }
    }
}

fun getCategoryIcon(category: TaskCategory): ImageVector {
    return when (category) {
        TaskCategory.ALL -> Icons.Default.Bolt
        TaskCategory.APP_DOWNLOAD -> Icons.Default.Gamepad
        TaskCategory.VIDEO_ADS -> Icons.Default.Videocam
        TaskCategory.SURVEY -> Icons.Default.Poll
        TaskCategory.SOCIAL -> Icons.Default.Share
        TaskCategory.QUICK_POLL -> Icons.Default.CheckCircle
    }
}

fun getCategoryBgColor(category: TaskCategory): Color {
    return when (category) {
        TaskCategory.ALL -> GeoPrimaryContainer
        TaskCategory.APP_DOWNLOAD -> GeoCategoryGamingBg
        TaskCategory.VIDEO_ADS -> GeoCategoryVideoBg
        TaskCategory.SURVEY -> GeoCategorySurveyBg
        TaskCategory.SOCIAL -> GeoCategorySocialBg
        TaskCategory.QUICK_POLL -> GeoCategoryDailyBg
    }
}

fun getCategoryFgColor(category: TaskCategory): Color {
    return when (category) {
        TaskCategory.ALL -> GeoPrimaryDark
        TaskCategory.APP_DOWNLOAD -> GeoCategoryGamingFg
        TaskCategory.VIDEO_ADS -> GeoCategoryVideoFg
        TaskCategory.SURVEY -> GeoCategorySurveyFg
        TaskCategory.SOCIAL -> GeoCategorySocialFg
        TaskCategory.QUICK_POLL -> GeoCategoryDailyFg
    }
}

fun getCategoryColor(category: TaskCategory): Color = getCategoryFgColor(category)
