package com.example.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import com.example.ui.components.AuthDialog
import com.example.ui.components.PayoutRequestDialog
import com.example.ui.components.ProofSubmitDialog
import com.example.ui.components.SecurityAuditDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.TaskDetailScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldReward
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.viewmodel.TaskViewModel

sealed class AppTab(val route: String, val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    data object Home : AppTab("home", "Earn Tasks", Icons.Filled.Home, Icons.Outlined.Home)
    data object Wallet : AppTab("wallet", "Wallet", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet)
    data object Profile : AppTab("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (activeDetailTaskId == null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("main_bottom_nav")
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
                                    text = tab.title,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryIndigo,
                                selectedTextColor = PrimaryIndigo,
                                indicatorColor = PrimaryIndigo.copy(alpha = 0.12f)
                            ),
                            modifier = Modifier.testTag("nav_item_${tab.route}")
                        )
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
                            onOpenSecurityDialog = { viewModel.toggleSecurityDialog(true) }
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

            if (uiState.showAuthDialog) {
                AuthDialog(
                    onDismiss = { viewModel.toggleAuthDialog(false) },
                    onLogin = { name, email, isGoogle ->
                        viewModel.loginUser(name, email, isGoogle)
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
