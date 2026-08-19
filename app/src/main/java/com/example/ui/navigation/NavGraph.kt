package com.example.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.UserRole
import com.example.ui.components.PayoutRequestDialog
import com.example.ui.components.ProofSubmitDialog
import com.example.ui.components.SecurityAuditDialog
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.TaskDetailScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.GeoBorderDark
import com.example.ui.theme.GeoNavBg
import com.example.ui.theme.GeoOnPrimaryContainer
import com.example.ui.theme.GeoPrimary
import com.example.ui.theme.GeoPrimaryContainer
import com.example.ui.theme.GeoTextMuted
import com.example.ui.viewmodel.TaskViewModel

sealed class AppTab(val route: String, val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    data object Home : AppTab("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    data object Wallet : AppTab("wallet", "Wallet", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet)
    data object Profile : AppTab("profile", "Account", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun TaskEarnApp(viewModel: TaskViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentTab by remember { mutableStateOf<AppTab>(AppTab.Home) }
    var activeDetailTaskId by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.bannerMessage) {
        uiState.bannerMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.dismissBannerMessage()
        }
    }

    // Role-based top routing: Not Logged in -> Auth Screen
    if (!uiState.isLoggedIn) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AuthScreen(
                    isLoading = uiState.isLoading,
                    onLogin = { id, pass -> viewModel.login(id, pass) },
                    onRegister = { name, email, phone, pass, ref -> viewModel.register(name, email, phone, pass, ref) }
                )
            }
        }
        return
    }

    // If Admin Role -> Show Admin Dashboard
    if (uiState.currentRole == UserRole.ADMIN) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AdminDashboardScreen(
                    tasks = uiState.tasks,
                    transactions = uiState.transactions,
                    securityAudit = uiState.securityAudit,
                    onApproveProof = { viewModel.adminApproveProof(it) },
                    onRejectProof = { id, reason -> viewModel.adminRejectProof(id, reason) },
                    onCreateTask = { title, desc, cat, pts, mins, instr, proofReq ->
                        viewModel.adminCreateTask(title, desc, cat, pts, mins, instr, proofReq)
                    },
                    onDeleteTask = { viewModel.adminDeleteTask(it) },
                    onApprovePayout = { viewModel.adminApprovePayout(it) },
                    onRejectPayout = { id, refund -> viewModel.adminRejectPayout(id, refund) },
                    onSwitchToUserMode = { viewModel.switchRole(UserRole.USER) },
                    onLogout = { viewModel.logout() }
                )
            }
        }
        return
    }

    // Standard Earner Screen
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (activeDetailTaskId == null) {
                Surface(
                    color = GeoNavBg,
                    border = BorderStroke(1.dp, GeoBorderDark),
                    tonalElevation = 6.dp
                ) {
                    NavigationBar(
                        containerColor = GeoNavBg,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("main_bottom_nav")
                    ) {
                        val tabs = listOf(AppTab.Home, AppTab.Wallet, AppTab.Profile)
                        tabs.forEach { tab ->
                            val isSelected = currentTab == tab
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { currentTab = tab },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.title
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.title.uppercase(),
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                        fontSize = 10.sp,
                                        letterSpacing = 1.sp
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = GeoOnPrimaryContainer,
                                    selectedTextColor = GeoPrimary,
                                    indicatorColor = GeoPrimaryContainer,
                                    unselectedIconColor = GeoTextMuted.copy(alpha = 0.6f),
                                    unselectedTextColor = GeoTextMuted.copy(alpha = 0.6f)
                                ),
                                modifier = Modifier.testTag("nav_item_${tab.route}")
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (activeDetailTaskId != null) {
                val task = uiState.tasks.find { it.id == activeDetailTaskId }
                TaskDetailScreen(
                    task = task,
                    currency = uiState.selectedCurrency,
                    onBack = { activeDetailTaskId = null },
                    onSubmitProofClick = { viewModel.toggleProofDialog(true) }
                )
            } else {
                when (currentTab) {
                    AppTab.Home -> {
                        HomeScreen(
                            uiState = uiState,
                            onCategorySelect = { viewModel.selectCategory(it) },
                            onStatusFilterSelect = { viewModel.selectStatusFilter(it) },
                            onSearchChange = { viewModel.onSearchQueryChange(it) },
                            onTaskClick = { taskId ->
                                activeDetailTaskId = taskId
                                viewModel.selectTask(taskId)
                            },
                            onCurrencySelect = { viewModel.setCurrency(it) },
                            onWithdrawClick = { viewModel.togglePayoutDialog(true) },
                            onStreakClaim = { day, pts -> viewModel.claimDailyStreak(day, pts) },
                            onSecurityAuditClick = { viewModel.toggleSecurityDialog(true) }
                        )
                    }
                    AppTab.Wallet -> {
                        WalletScreen(
                            userProfile = uiState.userProfile,
                            transactions = uiState.transactions,
                            selectedCurrency = uiState.selectedCurrency,
                            onCurrencySelect = { viewModel.setCurrency(it) },
                            onRequestPayoutClick = { viewModel.togglePayoutDialog(true) }
                        )
                    }
                    AppTab.Profile -> {
                        ProfileScreen(
                            userProfile = uiState.userProfile,
                            securityAudit = uiState.securityAudit,
                            onOpenAuthDialog = { viewModel.toggleAuthDialog(true) },
                            onOpenSecurityDialog = { viewModel.toggleSecurityDialog(true) },
                            onSwitchToAdmin = { viewModel.switchRole(UserRole.ADMIN) },
                            onLogout = { viewModel.logout() }
                        )
                    }
                }
            }

            // Dialogs
            if (uiState.showProofDialog) {
                val task = uiState.tasks.find { it.id == activeDetailTaskId }
                if (task != null) {
                    ProofSubmitDialog(
                        task = task,
                        isSubmitting = uiState.isSubmittingProof,
                        onDismiss = { viewModel.toggleProofDialog(false) },
                        onSubmit = { url, note, img ->
                            viewModel.submitTaskProof(task.id, url, note, img)
                        }
                    )
                }
            }

            if (uiState.showPayoutDialog) {
                PayoutRequestDialog(
                    userBalance = uiState.userProfile?.balancePoints ?: 0,
                    currency = uiState.selectedCurrency,
                    isRequesting = uiState.isRequestingPayout,
                    onDismiss = { viewModel.togglePayoutDialog(false) },
                    onRequest = { method, account, points ->
                        viewModel.requestPayout(method, account, points)
                    }
                )
            }

            if (uiState.showSecurityDialog) {
                SecurityAuditDialog(
                    auditResult = uiState.securityAudit,
                    onDismiss = { viewModel.toggleSecurityDialog(false) }
                )
            }
        }
    }
}
