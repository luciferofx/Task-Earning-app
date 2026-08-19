package com.example.data.repository

import com.example.data.local.dao.TaskDao
import com.example.data.local.dao.TransactionDao
import com.example.data.local.dao.UserProfileDao
import com.example.data.local.entity.TaskEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.Currency
import com.example.data.model.PayoutMethod
import com.example.data.model.PayoutRequest
import com.example.data.model.PayoutResponse
import com.example.data.model.SubmitProofRequest
import com.example.data.model.SubmitProofResponse
import com.example.data.model.TaskCategory
import com.example.data.model.TaskStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.UUID

class TaskEarnRepository(
    private val taskDao: TaskDao,
    private val transactionDao: TransactionDao,
    private val userProfileDao: UserProfileDao
) {
    val tasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
    val transactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val userProfile: Flow<UserProfileEntity?> = userProfileDao.getUserProfile()

    fun observeTask(taskId: String): Flow<TaskEntity?> = taskDao.observeTaskById(taskId)

    suspend fun initializeSeedData() = withContext(Dispatchers.IO) {
        val existingTasks = taskDao.getAllTasks().firstOrNull()
        if (existingTasks.isNullOrEmpty()) {
            val initialTasks = createSeedTasks()
            taskDao.insertTasks(initialTasks)
        }

        val existingProfile = userProfileDao.getUserProfileOnce()
        if (existingProfile == null) {
            val defaultProfile = UserProfileEntity(
                userId = "usr_${UUID.randomUUID().toString().take(8)}",
                name = "Alex Rivera",
                email = "alex.rivera@example.com",
                avatarUrl = "",
                balancePoints = 4850,
                totalEarnedPoints = 14500,
                totalWithdrawnPoints = 9650,
                streakDays = 4,
                lastCheckInTimestamp = System.currentTimeMillis() - (18 * 3600 * 1000), // Checked in yesterday
                referralCode = "EARN99X",
                completedTasksCount = 7
            )
            userProfileDao.insertProfile(defaultProfile)

            // Add sample transaction history
            val initialTransactions = listOf(
                TransactionEntity(
                    id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
                    title = "App Install: Nova Crypto Wallet",
                    type = "EARN",
                    points = 1200,
                    amountFormatted = "+1,200 pts ($1.20)",
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis() - 86400000L
                ),
                TransactionEntity(
                    id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
                    title = "PayPal Cash Withdrawal",
                    type = "PAYOUT",
                    points = -5000,
                    amountFormatted = "-5,000 pts ($5.00)",
                    status = "COMPLETED",
                    payoutMethod = "PayPal",
                    destinationAccount = "alex.rivera@example.com",
                    timestamp = System.currentTimeMillis() - (86400000L * 3)
                ),
                TransactionEntity(
                    id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
                    title = "Daily 7-Day Streak Bonus",
                    type = "BONUS",
                    points = 250,
                    amountFormatted = "+250 pts ($0.25)",
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis() - (86400000L * 2)
                ),
                TransactionEntity(
                    id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
                    title = "Survey: Tech Gadgets 2026",
                    type = "EARN",
                    points = 850,
                    amountFormatted = "+850 pts ($0.85)",
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis() - (86400000L * 4)
                )
            )
            transactionDao.insertTransactions(initialTransactions)
        }
    }

    suspend fun submitProof(request: SubmitProofRequest): SubmitProofResponse = withContext(Dispatchers.IO) {
        val task = taskDao.getTaskById(request.taskId)
            ?: return@withContext SubmitProofResponse(
                success = false,
                taskId = request.taskId,
                status = "ERROR",
                message = "Task not found."
            )

        val timestamp = System.currentTimeMillis()
        taskDao.updateTaskSubmission(
            taskId = request.taskId,
            newStatus = TaskStatus.IN_REVIEW,
            proofUrl = request.proofUrl,
            note = request.proofNote,
            timestamp = timestamp
        )

        // Record a pending transaction
        val tx = TransactionEntity(
            id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
            title = "Task Submission: ${task.title}",
            type = "EARN",
            points = task.pointsReward,
            amountFormatted = "+${task.pointsReward} pts ($${String.format("%.2f", task.pointsReward / 1000.0)})",
            status = "PENDING",
            timestamp = timestamp
        )
        transactionDao.insertTransaction(tx)

        SubmitProofResponse(
            success = true,
            taskId = request.taskId,
            status = "IN_REVIEW",
            message = "Proof submitted successfully! Our moderators will review and approve within 15-30 minutes."
        )
    }

    suspend fun approveAndCreditTask(taskId: String) = withContext(Dispatchers.IO) {
        val task = taskDao.getTaskById(taskId) ?: return@withContext
        taskDao.updateTaskSubmission(
            taskId = taskId,
            newStatus = TaskStatus.COMPLETED,
            proofUrl = task.submittedProofUrl ?: "https://example.com/proof",
            note = task.submittedNote ?: "Auto-verified",
            timestamp = System.currentTimeMillis()
        )
        userProfileDao.addPoints(task.pointsReward, task.pointsReward, 1)

        val tx = TransactionEntity(
            id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
            title = "Task Approved: ${task.title}",
            type = "EARN",
            points = task.pointsReward,
            amountFormatted = "+${task.pointsReward} pts ($${String.format("%.2f", task.pointsReward / 1000.0)})",
            status = "COMPLETED",
            timestamp = System.currentTimeMillis()
        )
        transactionDao.insertTransaction(tx)
    }

    suspend fun requestPayout(request: PayoutRequest): PayoutResponse = withContext(Dispatchers.IO) {
        val profile = userProfileDao.getUserProfileOnce()
            ?: return@withContext PayoutResponse(
                success = false,
                transactionId = "",
                status = "FAILED",
                remainingBalancePoints = 0,
                message = "User session expired."
            )

        if (profile.balancePoints < request.pointsToRedeem) {
            return@withContext PayoutResponse(
                success = false,
                transactionId = "",
                status = "FAILED",
                remainingBalancePoints = profile.balancePoints,
                message = "Insufficient point balance. You have ${profile.balancePoints} points."
            )
        }

        userProfileDao.deductPointsForPayout(request.pointsToRedeem)

        val txId = "PO-${UUID.randomUUID().toString().take(7).uppercase()}"
        val symbol = when (request.currencyCode) {
            "INR" -> "₹"
            "EUR" -> "€"
            else -> "$"
        }
        val formattedAmount = "-${request.pointsToRedeem} pts ($symbol${String.format("%.2f", request.equivalentAmount)})"

        val tx = TransactionEntity(
            id = txId,
            title = "${request.payoutMethod} Withdrawal",
            type = "PAYOUT",
            points = -request.pointsToRedeem,
            amountFormatted = formattedAmount,
            status = "PROCESSING",
            payoutMethod = request.payoutMethod,
            destinationAccount = request.destinationAccount,
            timestamp = System.currentTimeMillis()
        )
        transactionDao.insertTransaction(tx)

        val newBalance = profile.balancePoints - request.pointsToRedeem
        PayoutResponse(
            success = true,
            transactionId = txId,
            status = "PROCESSING",
            remainingBalancePoints = newBalance,
            message = "Payout request placed! Funds will arrive in ${request.destinationAccount} within 2-6 hours."
        )
    }

    suspend fun claimDailyStreak(day: Int, rewardPoints: Int): Boolean = withContext(Dispatchers.IO) {
        val profile = userProfileDao.getUserProfileOnce() ?: return@withContext false
        val now = System.currentTimeMillis()
        val nextStreak = if (day >= 7) 1 else day

        userProfileDao.updateDailyStreak(nextStreak, now, rewardPoints)

        val tx = TransactionEntity(
            id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
            title = "Day $day Daily Streak Reward",
            type = "BONUS",
            points = rewardPoints,
            amountFormatted = "+$rewardPoints pts ($${String.format("%.2f", rewardPoints / 1000.0)})",
            status = "COMPLETED",
            timestamp = now
        )
        transactionDao.insertTransaction(tx)
        true
    }

    suspend fun updateUserProfile(name: String, email: String) = withContext(Dispatchers.IO) {
        val profile = userProfileDao.getUserProfileOnce()
        if (profile != null) {
            userProfileDao.updateProfile(profile.copy(name = name, email = email))
        }
    }

    private fun createSeedTasks(): List<TaskEntity> {
        return listOf(
            TaskEntity(
                id = "task_01",
                title = "Install & Register: CyberTrade Crypto",
                description = "Download CyberTrade, complete KYC level 1 verification and hold for 24 hours.",
                category = TaskCategory.APP_DOWNLOAD,
                pointsReward = 2400,
                durationMinutes = 5,
                status = TaskStatus.AVAILABLE,
                iconType = "crypto",
                isFeatured = true,
                instructions = "1. Click start and download CyberTrade from Google Play.\n2. Open app and complete basic account sign up.\n3. Verify your email address.\n4. Take a screenshot of the Profile page showing your User ID.\n5. Upload screenshot below to claim your 2,400 points.",
                proofRequirement = "Screenshot of your CyberTrade account profile screen showing verified email & UID."
            ),
            TaskEntity(
                id = "task_02",
                title = "Consumer Opinions: AI & Smart Devices 2026",
                description = "Answer 12 quick questions about AI tools and smart home gadgets you use.",
                category = TaskCategory.SURVEY,
                pointsReward = 1500,
                durationMinutes = 8,
                status = TaskStatus.AVAILABLE,
                iconType = "survey",
                isFeatured = true,
                instructions = "1. Answer each multiple-choice question honestly.\n2. Complete all 12 survey segments without rushing.\n3. At the end, copy the Survey Completion ID code.\n4. Paste the completion code and submit proof.",
                proofRequirement = "Survey completion verification code (e.g. SURV-9982)."
            ),
            TaskEntity(
                id = "task_03",
                title = "Watch: Next-Gen Smartphone Trailer",
                description = "Watch the full 60-second official product launch teaser and click explore.",
                category = TaskCategory.VIDEO_ADS,
                pointsReward = 450,
                durationMinutes = 2,
                status = TaskStatus.AVAILABLE,
                iconType = "video",
                isFeatured = false,
                instructions = "1. Watch the high-definition product trailer from start to finish.\n2. Do not minimize or skip the video playback.\n3. Tap on the Learn More link at the end.\n4. Paste the final landing URL as proof.",
                proofRequirement = "Landing page URL or confirmation code displayed at end."
            ),
            TaskEntity(
                id = "task_04",
                title = "Follow @TaskEarnApp on Instagram & Like",
                description = "Follow our official channel and like our latest giveaway announcement post.",
                category = TaskCategory.SOCIAL,
                pointsReward = 600,
                durationMinutes = 3,
                status = TaskStatus.AVAILABLE,
                iconType = "instagram",
                isFeatured = false,
                instructions = "1. Follow instagram.com/taskearnapp\n2. Like the pinned giveaway post.\n3. Leave a helpful comment.\n4. Enter your Instagram username in the submission box.",
                proofRequirement = "Your Instagram username handle (e.g. @alex_gamer99)."
            ),
            TaskEntity(
                id = "task_05",
                title = "Daily Tech Poll: Favorite Mobile OS Feature",
                description = "Cast your vote in today's community poll and view instant live results.",
                category = TaskCategory.QUICK_POLL,
                pointsReward = 200,
                durationMinutes = 1,
                status = TaskStatus.AVAILABLE,
                iconType = "poll",
                isFeatured = false,
                instructions = "1. Select your preferred mobile productivity feature.\n2. Submit your vote.\n3. Instant reward will be credited directly to your wallet.",
                proofRequirement = "Select your vote option to confirm."
            ),
            TaskEntity(
                id = "task_06",
                title = "Play & Reach Level 10: Kingdom Rush Clash",
                description = "Experience the thrilling strategy game, build castle and reach level 10.",
                category = TaskCategory.APP_DOWNLOAD,
                pointsReward = 3800,
                durationMinutes = 20,
                status = TaskStatus.AVAILABLE,
                iconType = "game",
                isFeatured = true,
                instructions = "1. Download and install Kingdom Rush Clash.\n2. Play the campaign mode and defeat Boss 3 to reach Level 10.\n3. Take a screenshot showing your castle Town Hall at Level 10.\n4. Submit proof image.",
                proofRequirement = "Screenshot of in-game town hall showing Level 10 badge."
            ),
            TaskEntity(
                id = "task_07",
                title = "Financial Health & Savings Habits Study",
                description = "Brief 5-minute academic research questionnaire on personal budgeting apps.",
                category = TaskCategory.SURVEY,
                pointsReward = 1100,
                durationMinutes = 6,
                status = TaskStatus.AVAILABLE,
                iconType = "finance",
                isFeatured = false,
                instructions = "1. Open the research survey.\n2. Share your feedback on savings goals and fintech features.\n3. Reach the thank-you screen and copy the completion token.",
                proofRequirement = "Screenshot of thank-you screen or completion token."
            )
        )
    }
}
