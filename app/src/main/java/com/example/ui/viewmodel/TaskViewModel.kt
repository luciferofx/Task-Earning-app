package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.TaskEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.Currency
import com.example.data.model.PayoutMethod
import com.example.data.model.PayoutRequest
import com.example.data.model.SecurityAuditResult
import com.example.data.model.SubmitProofRequest
import com.example.data.model.TaskCategory
import com.example.data.model.TaskStatus
import com.example.data.model.UserRole
import com.example.data.repository.TaskEarnRepository
import com.example.data.security.SecurityChecker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class TaskUiState(
    val isLoggedIn: Boolean = false,
    val currentRole: UserRole = UserRole.USER,
    val tasks: List<TaskEntity> = emptyList(),
    val filteredTasks: List<TaskEntity> = emptyList(),
    val selectedCategory: TaskCategory = TaskCategory.ALL,
    val selectedStatusFilter: TaskStatus? = null,
    val searchQuery: String = "",
    val selectedTaskId: String? = null,
    val currentSelectedTask: TaskEntity? = null,
    val userProfile: UserProfileEntity? = null,
    val transactions: List<TransactionEntity> = emptyList(),
    val selectedCurrency: Currency = Currency.INR, // Default to Indian Rupee ₹
    val securityAudit: SecurityAuditResult? = null,
    val isLoading: Boolean = false,
    val isSubmittingProof: Boolean = false,
    val isRequestingPayout: Boolean = false,
    val bannerMessage: String? = null,
    val showAuthDialog: Boolean = false,
    val showPayoutDialog: Boolean = false,
    val showProofDialog: Boolean = false,
    val showSecurityDialog: Boolean = false,
    val showStreakCelebration: Boolean = false,
    val showAdminCreateTaskDialog: Boolean = false
)

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = TaskEarnRepository(db.taskDao(), db.transactionDao(), db.userProfileDao())

    private val _uiState = MutableStateFlow(TaskUiState(isLoading = false))
    val uiState: StateFlow<TaskUiState> = _uiState

    init {
        viewModelScope.launch {
            repository.initializeSeedData()
            refreshSecurityAudit()
        }

        viewModelScope.launch {
            combine(
                repository.tasks,
                repository.transactions,
                repository.userProfile
            ) { tasks, txs, profile ->
                Triple(tasks, txs, profile)
            }.collect { (tasks, txs, profile) ->
                _uiState.update { current ->
                    val filtered = filterTasks(tasks, current.selectedCategory, current.selectedStatusFilter, current.searchQuery)
                    val activeTask = if (current.selectedTaskId != null) {
                        tasks.find { it.id == current.selectedTaskId }
                    } else null

                    current.copy(
                        tasks = tasks,
                        filteredTasks = filtered,
                        transactions = txs,
                        userProfile = profile,
                        currentSelectedTask = activeTask,
                        isLoading = false
                    )
                }
            }
        }
    }

    // Unified Login with Admin Detection
    fun login(emailOrPhone: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(400) // Brief animation feel

            val cleanInput = emailOrPhone.trim().lowercase()
            val cleanPass = password.trim()

            val isAdmin = (cleanInput in listOf("admin", "admin@taskearn.in", "admin@taskearn.com", "admin@gmail.com", "root") &&
                    cleanPass in listOf("admin", "admin123", "admin@123", "password", "adminpass")) ||
                    (cleanPass == "admin123" && cleanInput.contains("admin")) ||
                    (cleanPass == "admin" && cleanInput == "admin")

            if (isAdmin) {
                // Route to Admin Dashboard
                repository.loginUser(emailOrPhone, "ADMIN")
                _uiState.update {
                    it.copy(
                        isLoggedIn = true,
                        currentRole = UserRole.ADMIN,
                        isLoading = false,
                        bannerMessage = "🛡️ Logged in as Admin! Welcome to TaskEarn Admin Console."
                    )
                }
            } else {
                // Regular Indian Earner Login
                val profile = repository.loginUser(emailOrPhone, "USER")
                _uiState.update {
                    it.copy(
                        isLoggedIn = true,
                        currentRole = UserRole.USER,
                        isLoading = false,
                        bannerMessage = "Welcome back, ${profile.name}! Ready to earn ₹ today?"
                    )
                }
            }
        }
    }

    // Register Indian User with Welcome Bonus
    fun register(name: String, email: String, phone: String, password: String, referral: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(500)

            val profile = repository.registerUser(
                name = name.ifBlank { "Rahul Sharma" },
                email = email.ifBlank { "rahul@gmail.com" },
                phone = phone.ifBlank { "+91 98765 43210" },
                referral = referral
            )

            val bonusText = if (referral.isNotBlank()) "₹50 Referral Bonus" else "₹25 Signup Bonus"
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    currentRole = UserRole.USER,
                    isLoading = false,
                    bannerMessage = "🎉 Account registered! $bonusText credited to your wallet."
                )
            }
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                isLoggedIn = false,
                currentRole = UserRole.USER,
                bannerMessage = "You have been logged out."
            )
        }
    }

    fun switchRole(newRole: UserRole) {
        _uiState.update {
            it.copy(
                currentRole = newRole,
                bannerMessage = if (newRole == UserRole.ADMIN) "🛡️ Switched to Admin Dashboard" else "👤 Switched to Earner View"
            )
        }
    }

    // Admin Actions
    fun adminApproveProof(taskId: String) {
        viewModelScope.launch {
            repository.adminApproveProof(taskId)
            _uiState.update {
                it.copy(bannerMessage = "✅ Task submission approved & points credited to user!")
            }
        }
    }

    fun adminRejectProof(taskId: String, reason: String = "Incomplete submission") {
        viewModelScope.launch {
            repository.adminRejectProof(taskId, reason)
            _uiState.update {
                it.copy(bannerMessage = "❌ Submission rejected ($reason).")
            }
        }
    }

    fun adminCreateTask(
        title: String,
        description: String,
        category: TaskCategory,
        points: Int,
        minutes: Int,
        instructions: String,
        proofReq: String
    ) {
        viewModelScope.launch {
            val newTask = TaskEntity(
                id = "in_task_${UUID.randomUUID().toString().take(6)}",
                title = title,
                description = description,
                category = category,
                pointsReward = points,
                durationMinutes = minutes,
                status = TaskStatus.AVAILABLE,
                iconType = category.iconName,
                isFeatured = true,
                instructions = instructions.ifBlank { "1. Open the task link.\n2. Complete the required steps.\n3. Submit proof." },
                proofRequirement = proofReq.ifBlank { "Screenshot or confirmation ID." }
            )
            repository.adminCreateTask(newTask)
            _uiState.update {
                it.copy(
                    showAdminCreateTaskDialog = false,
                    bannerMessage = "🚀 New task created and published live!"
                )
            }
        }
    }

    fun adminDeleteTask(taskId: String) {
        viewModelScope.launch {
            repository.adminDeleteTask(taskId)
            _uiState.update {
                it.copy(bannerMessage = "🗑️ Task deleted successfully.")
            }
        }
    }

    fun adminApprovePayout(transactionId: String) {
        viewModelScope.launch {
            repository.adminApprovePayout(transactionId)
            _uiState.update {
                it.copy(bannerMessage = "💸 UPI Payout approved & marked as COMPLETED!")
            }
        }
    }

    fun adminRejectPayout(transactionId: String, refundPoints: Int = 2500) {
        viewModelScope.launch {
            repository.adminRejectPayout(transactionId, refundPoints)
            _uiState.update {
                it.copy(bannerMessage = "Payout request rejected and $refundPoints pts refunded.")
            }
        }
    }

    fun toggleAdminCreateTaskDialog(show: Boolean) {
        _uiState.update { it.copy(showAdminCreateTaskDialog = show) }
    }

    fun selectCategory(category: TaskCategory) {
        _uiState.update { current ->
            val filtered = filterTasks(current.tasks, category, current.selectedStatusFilter, current.searchQuery)
            current.copy(selectedCategory = category, filteredTasks = filtered)
        }
    }

    fun selectStatusFilter(status: TaskStatus?) {
        _uiState.update { current ->
            val filtered = filterTasks(current.tasks, current.selectedCategory, status, current.searchQuery)
            current.copy(selectedStatusFilter = status, filteredTasks = filtered)
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { current ->
            val filtered = filterTasks(current.tasks, current.selectedCategory, current.selectedStatusFilter, query)
            current.copy(searchQuery = query, filteredTasks = filtered)
        }
    }

    fun selectTask(taskId: String?) {
        _uiState.update { current ->
            val task = current.tasks.find { it.id == taskId }
            current.copy(selectedTaskId = taskId, currentSelectedTask = task)
        }
    }

    fun setCurrency(currency: Currency) {
        _uiState.update { it.copy(selectedCurrency = currency) }
    }

    fun submitTaskProof(
        taskId: String,
        proofUrl: String,
        proofNote: String,
        screenshotPath: String? = null
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingProof = true) }
            val profile = _uiState.value.userProfile
            val deviceId = _uiState.value.securityAudit?.deviceId ?: "IN-DEV-901"

            val req = SubmitProofRequest(
                taskId = taskId,
                userId = profile?.userId ?: "usr_rahul",
                proofUrl = proofUrl,
                proofNote = proofNote,
                screenshotLocalPath = screenshotPath,
                deviceFingerprint = deviceId
            )

            val res = repository.submitProof(req)
            _uiState.update {
                it.copy(
                    isSubmittingProof = false,
                    showProofDialog = false,
                    bannerMessage = res.message
                )
            }
        }
    }

    fun requestPayout(
        method: PayoutMethod,
        account: String,
        points: Int
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRequestingPayout = true) }
            val profile = _uiState.value.userProfile
            val currency = _uiState.value.selectedCurrency
            val equivalent = when (currency) {
                Currency.INR -> points / 10.0
                Currency.USD -> points / 1000.0
                Currency.EUR -> points / 1080.0
            }

            val req = PayoutRequest(
                userId = profile?.userId ?: "usr_rahul",
                payoutMethod = method.title,
                destinationAccount = account,
                pointsToRedeem = points,
                currencyCode = currency.code,
                equivalentAmount = equivalent
            )

            val res = repository.requestPayout(req)
            _uiState.update {
                it.copy(
                    isRequestingPayout = false,
                    showPayoutDialog = false,
                    bannerMessage = res.message
                )
            }
        }
    }

    fun claimDailyStreak(day: Int, points: Int) {
        viewModelScope.launch {
            val success = repository.claimDailyStreak(day, points)
            if (success) {
                _uiState.update {
                    it.copy(
                        showStreakCelebration = true,
                        bannerMessage = "⭐ Day $day bonus claimed! +$points pts (₹${points / 10}) added."
                    )
                }
            }
        }
    }

    fun refreshSecurityAudit() {
        viewModelScope.launch {
            val audit = SecurityChecker.performFullSecurityAudit(getApplication())
            _uiState.update { it.copy(securityAudit = audit) }
        }
    }

    fun toggleAuthDialog(show: Boolean) {
        _uiState.update { it.copy(showAuthDialog = show) }
    }

    fun togglePayoutDialog(show: Boolean) {
        _uiState.update { it.copy(showPayoutDialog = show) }
    }

    fun toggleProofDialog(show: Boolean) {
        _uiState.update { it.copy(showProofDialog = show) }
    }

    fun toggleSecurityDialog(show: Boolean) {
        _uiState.update { it.copy(showSecurityDialog = show) }
    }

    fun dismissCelebration() {
        _uiState.update { it.copy(showStreakCelebration = false) }
    }

    fun dismissBannerMessage() {
        _uiState.update { it.copy(bannerMessage = null) }
    }

    private fun filterTasks(
        tasks: List<TaskEntity>,
        category: TaskCategory,
        status: TaskStatus?,
        query: String
    ): List<TaskEntity> {
        return tasks.filter { task ->
            val matchCategory = category == TaskCategory.ALL || task.category == category
            val matchStatus = status == null || task.status == status
            val matchQuery = query.isBlank() ||
                    task.title.contains(query, ignoreCase = true) ||
                    task.description.contains(query, ignoreCase = true) ||
                    task.category.displayName.contains(query, ignoreCase = true)
            matchCategory && matchStatus && matchQuery
        }
    }
}
