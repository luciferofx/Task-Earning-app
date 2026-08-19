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
import com.example.data.repository.TaskEarnRepository
import com.example.data.security.SecurityChecker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val filteredTasks: List<TaskEntity> = emptyList(),
    val selectedCategory: TaskCategory = TaskCategory.ALL,
    val selectedStatusFilter: TaskStatus? = null,
    val searchQuery: String = "",
    val selectedTaskId: String? = null,
    val currentSelectedTask: TaskEntity? = null,
    val userProfile: UserProfileEntity? = null,
    val transactions: List<TransactionEntity> = emptyList(),
    val selectedCurrency: Currency = Currency.USD,
    val securityAudit: SecurityAuditResult? = null,
    val isLoading: Boolean = false,
    val isSubmittingProof: Boolean = false,
    val isRequestingPayout: Boolean = false,
    val bannerMessage: String? = null,
    val showAuthDialog: Boolean = false,
    val showPayoutDialog: Boolean = false,
    val showProofDialog: Boolean = false,
    val showSecurityDialog: Boolean = false,
    val showStreakCelebration: Boolean = false
)

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = TaskEarnRepository(db.taskDao(), db.transactionDao(), db.userProfileDao())

    private val _uiState = MutableStateFlow(TaskUiState(isLoading = true))
    val uiState: StateFlow<TaskUiState> = _uiState

    init {
        viewModelScope.launch {
            repository.initializeSeedData()
            refreshSecurityAudit()
        }

        // Combine reactive Room flows into StateFlow
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
            val deviceId = _uiState.value.securityAudit?.deviceId ?: "DEV-AUTO-901"

            val req = SubmitProofRequest(
                taskId = taskId,
                userId = profile?.userId ?: "usr_anon",
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

            // Simulate automatic moderator verification after 5 seconds for interactive satisfaction
            delay(5000)
            repository.approveAndCreditTask(taskId)
            _uiState.update {
                it.copy(bannerMessage = "🎉 Hooray! Your submitted task was verified & points credited to your wallet!")
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
                Currency.USD -> points / 1000.0
                Currency.INR -> points / 12.0
                Currency.EUR -> points / 1080.0
            }

            val req = PayoutRequest(
                userId = profile?.userId ?: "usr_anon",
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
                        bannerMessage = "⭐ Day $day bonus claimed! +$points points added."
                    )
                }
            }
        }
    }

    fun refreshSecurityAudit() {
        val audit = SecurityChecker.performFullSecurityAudit(getApplication())
        _uiState.update { it.copy(securityAudit = audit) }
    }

    fun loginUser(name: String, email: String, isGoogle: Boolean) {
        viewModelScope.launch {
            repository.updateUserProfile(name, email)
            _uiState.update {
                it.copy(
                    showAuthDialog = false,
                    bannerMessage = "Signed in as $name ($email)"
                )
            }
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
