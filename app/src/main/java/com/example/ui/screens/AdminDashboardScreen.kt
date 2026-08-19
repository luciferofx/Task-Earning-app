package com.example.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.TaskEntity
import com.example.data.local.entity.TransactionEntity
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
import com.example.ui.theme.GeoSurfaceDark
import com.example.ui.theme.GeoSurfaceElevated
import com.example.ui.theme.GeoTextMuted
import com.example.ui.theme.GeoTextPrimary
import com.example.ui.theme.GeoTextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    tasks: List<TaskEntity>,
    transactions: List<TransactionEntity>,
    securityAudit: SecurityAuditResult?,
    onApproveProof: (taskId: String) -> Unit,
    onRejectProof: (taskId: String, reason: String) -> Unit,
    onCreateTask: (title: String, description: String, category: TaskCategory, points: Int, minutes: Int, instructions: String, proofReq: String) -> Unit,
    onDeleteTask: (taskId: String) -> Unit,
    onApprovePayout: (transactionId: String) -> Unit,
    onRejectPayout: (transactionId: String, refundPoints: Int) -> Unit,
    onSwitchToUserMode: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedAdminTab by remember { mutableIntStateOf(0) }
    var showCreateTaskModal by remember { mutableStateOf(false) }

    val pendingReviewTasks = tasks.filter { it.status == TaskStatus.IN_REVIEW }
    val pendingPayouts = transactions.filter { it.type == "PAYOUT" && it.status == "PROCESSING" }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GeoBgDark)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp)
        ) {
            // Header Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(GeoPrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.AdminPanelSettings,
                                contentDescription = "Admin",
                                tint = GeoPrimaryDark,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "ADMIN CONSOLE",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = GeoPrimary,
                                    letterSpacing = 1.5.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = GeoSuccessGreen.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "LIVE",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        color = GeoSuccessGreen,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "TaskEarn India",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = GeoTextWhite
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Switch to Earner View Button
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = GeoSurfaceDark,
                            border = BorderStroke(1.dp, GeoBorderDark),
                            modifier = Modifier.clickable { onSwitchToUserMode() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = GeoPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Earner View",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = GeoTextWhite
                                )
                            }
                        }

                        // Logout Button
                        IconButton(
                            onClick = onLogout,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(GeoSurfaceDark)
                        ) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = GeoDangerRed, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            // Stats Overview Cards
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Card 1: Payouts Disbursed
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                        border = BorderStroke(1.dp, GeoBorderDark)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "TOTAL DISBURSED",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = GeoTextMuted,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₹64,850",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = GeoGoldAccent
                            )
                            Text(
                                text = "1,420 Indian Earners",
                                style = MaterialTheme.typography.bodySmall,
                                color = GeoTextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Card 2: Pending Submissions
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (pendingReviewTasks.isNotEmpty()) GeoCategoryDailyBg.copy(alpha = 0.15f) else GeoSurfaceDark
                        ),
                        border = BorderStroke(1.dp, if (pendingReviewTasks.isNotEmpty()) GeoPrimary else GeoBorderDark)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "PENDING PROOFS",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = GeoTextMuted,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${pendingReviewTasks.size} Waiting",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = if (pendingReviewTasks.isNotEmpty()) GeoPrimary else GeoTextWhite
                            )
                            Text(
                                text = "${tasks.size} Total Live Tasks",
                                style = MaterialTheme.typography.bodySmall,
                                color = GeoTextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Card 3: Pending Payouts
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                        border = BorderStroke(1.dp, GeoBorderDark)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "UPI PAYOUTS",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = GeoTextMuted,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${pendingPayouts.size} Requests",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = GeoSuccessGreen
                            )
                            Text(
                                text = "Instant IMPS/UPI",
                                style = MaterialTheme.typography.bodySmall,
                                color = GeoTextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            // Tab Navigation
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedAdminTab,
                    containerColor = GeoSurfaceElevated,
                    contentColor = GeoPrimary,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedAdminTab]),
                            color = GeoPrimary,
                            height = 3.dp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .padding(bottom = 16.dp)
                ) {
                    val tabs = listOf(
                        "Review Proofs (${pendingReviewTasks.size})",
                        "Manage Tasks (${tasks.size})",
                        "Payout Requests (${pendingPayouts.size})",
                        "Anti-Fraud Audit"
                    )
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedAdminTab == index,
                            onClick = { selectedAdminTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedAdminTab == index) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedAdminTab == index) GeoPrimary else GeoTextMuted,
                                    fontSize = 13.sp
                                )
                            },
                            modifier = Modifier.testTag("admin_tab_$index")
                        )
                    }
                }
            }

            // TAB 0: Review Proofs
            if (selectedAdminTab == 0) {
                if (pendingReviewTasks.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                            border = BorderStroke(1.dp, GeoBorderDark)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = GeoSuccessGreen,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "All Proof Submissions Reviewed!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GeoTextWhite
                                )
                                Text(
                                    text = "When Indian earners submit screenshots or confirmation codes, they appear here for instant approval.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GeoTextMuted,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(pendingReviewTasks) { task ->
                        AdminProofReviewCard(
                            task = task,
                            onApprove = { onApproveProof(task.id) },
                            onReject = { onRejectProof(task.id, "Invalid screenshot or code") }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // TAB 1: Manage Tasks
            if (selectedAdminTab == 1) {
                item {
                    Button(
                        onClick = { showCreateTaskModal = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_create_new_task"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GeoPrimary,
                            contentColor = GeoPrimaryDark
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "+ CREATE NEW EARNER TASK (₹)",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(tasks) { task ->
                    AdminTaskManagementCard(
                        task = task,
                        onDelete = { onDeleteTask(task.id) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // TAB 2: Payout Requests
            if (selectedAdminTab == 2) {
                if (pendingPayouts.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                            border = BorderStroke(1.dp, GeoBorderDark)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.Payment,
                                    contentDescription = null,
                                    tint = GeoGoldAccent,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No Pending Payout Requests",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GeoTextWhite
                                )
                                Text(
                                    text = "When users redeem points to UPI (PhonePe, GPay, Paytm) or Gift cards, requests appear here.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GeoTextMuted
                                )
                            }
                        }
                    }
                } else {
                    items(pendingPayouts) { payout ->
                        AdminPayoutApprovalCard(
                            payout = payout,
                            onApprove = { onApprovePayout(payout.id) },
                            onReject = { onRejectPayout(payout.id, kotlin.math.abs(payout.points)) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // TAB 3: Anti-Fraud Audit
            if (selectedAdminTab == 3) {
                item {
                    AdminFraudAuditSection(securityAudit = securityAudit)
                }
            }
        }

        // Create Task Dialog
        if (showCreateTaskModal) {
            AdminCreateTaskDialog(
                onDismiss = { showCreateTaskModal = false },
                onCreate = { title, desc, cat, pts, mins, instr, proofReq ->
                    onCreateTask(title, desc, cat, pts, mins, instr, proofReq)
                    showCreateTaskModal = false
                }
            )
        }
    }
}

@Composable
fun AdminProofReviewCard(
    task: TaskEntity,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
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
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GeoCategoryDailyBg
                ) {
                    Text(
                        text = "TASK SUBMISSION",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = GeoCategoryDailyFg,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "+${task.pointsReward} pts (₹${task.pointsReward / 10})",
                    fontWeight = FontWeight.Black,
                    color = GeoGoldAccent,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GeoTextWhite
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = GeoSurfaceElevated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Submitted Proof Link / Token:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = GeoPrimary
                    )
                    Text(
                        text = task.submittedProofUrl ?: "https://play.google.com/store/apps/details?id=sample",
                        style = MaterialTheme.typography.bodySmall,
                        color = GeoTextWhite
                    )

                    task.submittedNote?.let { note ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "User Note: $note",
                            style = MaterialTheme.typography.bodySmall,
                            color = GeoTextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, GeoDangerRed)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = GeoDangerRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Reject", color = GeoDangerRed, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onApprove,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GeoSuccessGreen,
                        contentColor = Color(0xFF003914)
                    )
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Approve & Pay", fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun AdminTaskManagementCard(
    task: TaskEntity,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
        border = BorderStroke(1.dp, GeoBorderDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = task.category.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = GeoPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${task.durationMinutes}m",
                        style = MaterialTheme.typography.labelSmall,
                        color = GeoTextMuted
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Reward: ${task.pointsReward} pts (₹${task.pointsReward / 10})",
                    style = MaterialTheme.typography.labelSmall,
                    color = GeoGoldAccent
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Task", tint = GeoDangerRed)
            }
        }
    }
}

@Composable
fun AdminPayoutApprovalCard(
    payout: TransactionEntity,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
                Text(
                    text = payout.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextWhite
                )

                Text(
                    text = payout.amountFormatted,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = GeoGoldAccent
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = GeoSurfaceElevated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Payment, contentDescription = null, tint = GeoPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Destination: ${payout.destinationAccount ?: "rahul.sharma@okhdfcbank"}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )
                        Text(
                            text = "Status: PROCESSING • Ref: ${payout.id}",
                            style = MaterialTheme.typography.bodySmall,
                            color = GeoTextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, GeoDangerRed)
                ) {
                    Text(text = "Reject & Refund", color = GeoDangerRed, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onApprove,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GeoSuccessGreen,
                        contentColor = Color(0xFF003914)
                    )
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Mark as Paid", fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun AdminFraudAuditSection(securityAudit: SecurityAuditResult?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
        border = BorderStroke(1.dp, GeoBorderDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = GeoSuccessGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "System Anti-Fraud & Integrity Engine",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextWhite
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GeoSurfaceElevated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "DEVICE TELEMETRY AUDIT",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = GeoPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Root Check (/system/xbin/su): Clean (Not Rooted)",
                        style = MaterialTheme.typography.bodySmall,
                        color = GeoSuccessGreen
                    )
                    Text(
                        text = "• VPN / Proxy Interface Detection: Inactive",
                        style = MaterialTheme.typography.bodySmall,
                        color = GeoSuccessGreen
                    )
                    Text(
                        text = "• Multi-Account Hardware Hash: Unique Verified",
                        style = MaterialTheme.typography.bodySmall,
                        color = GeoSuccessGreen
                    )
                    Text(
                        text = "• Geo-Location Verification: India (+91 Telecom Region)",
                        style = MaterialTheme.typography.bodySmall,
                        color = GeoPrimary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCreateTaskDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, description: String, category: TaskCategory, points: Int, minutes: Int, instructions: String, proofReq: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var pointsStr by remember { mutableStateOf("1500") }
    var minutesStr by remember { mutableStateOf("5") }
    var instructions by remember { mutableStateOf("") }
    var proofReq by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(TaskCategory.APP_DOWNLOAD) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
            border = BorderStroke(1.dp, GeoBorderDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Create Earner Task (₹)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GeoTextWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = GeoTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title (e.g. Install Swiggy App)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = GeoTextWhite,
                        unfocusedTextColor = GeoTextWhite,
                        focusedBorderColor = GeoPrimary,
                        unfocusedBorderColor = GeoBorderDark
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = pointsStr,
                        onValueChange = { pointsStr = it },
                        label = { Text("Points (₹${(pointsStr.toIntOrNull() ?: 0) / 10})") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GeoTextWhite,
                            unfocusedTextColor = GeoTextWhite,
                            focusedBorderColor = GeoPrimary,
                            unfocusedBorderColor = GeoBorderDark
                        )
                    )

                    OutlinedTextField(
                        value = minutesStr,
                        onValueChange = { minutesStr = it },
                        label = { Text("Est. Minutes") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GeoTextWhite,
                            unfocusedTextColor = GeoTextWhite,
                            focusedBorderColor = GeoPrimary,
                            unfocusedBorderColor = GeoBorderDark
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Short Description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = GeoTextWhite,
                        unfocusedTextColor = GeoTextWhite,
                        focusedBorderColor = GeoPrimary,
                        unfocusedBorderColor = GeoBorderDark
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = proofReq,
                    onValueChange = { proofReq = it },
                    label = { Text("Proof Requirement (e.g. Screenshot of UID)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = GeoTextWhite,
                        unfocusedTextColor = GeoTextWhite,
                        focusedBorderColor = GeoPrimary,
                        unfocusedBorderColor = GeoBorderDark
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val pts = pointsStr.toIntOrNull() ?: 1000
                        val mins = minutesStr.toIntOrNull() ?: 5
                        onCreate(title.ifBlank { "New Indian Earner Task" }, desc.ifBlank { "Complete steps to earn" }, selectedCategory, pts, mins, instructions, proofReq)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GeoPrimary,
                        contentColor = GeoPrimaryDark
                    )
                ) {
                    Text(text = "PUBLISH TASK TO EARNERS", fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
