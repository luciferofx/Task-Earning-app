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
    val pendingPayouts: Flow<List<TransactionEntity>> = transactionDao.getPendingPayoutTransactions()

    fun observeTask(taskId: String): Flow<TaskEntity?> = taskDao.observeTaskById(taskId)

    suspend fun initializeSeedData() = withContext(Dispatchers.IO) {
        val existingTasks = taskDao.getAllTasks().firstOrNull()
        if (existingTasks.isNullOrEmpty()) {
            val initialTasks = createIndianSeedTasks()
            taskDao.insertTasks(initialTasks)
        }

        val existingProfile = userProfileDao.getUserProfileOnce()
        if (existingProfile == null) {
            val defaultProfile = UserProfileEntity(
                userId = "usr_${UUID.randomUUID().toString().take(8)}",
                name = "Rahul Sharma",
                email = "rahul.sharma@gmail.com",
                phoneNumber = "+91 98765 43210",
                avatarUrl = "",
                balancePoints = 4850, // ₹485.00 INR
                totalEarnedPoints = 14500, // ₹1,450.00 INR
                totalWithdrawnPoints = 9650, // ₹965.00 INR
                streakDays = 4,
                lastCheckInTimestamp = System.currentTimeMillis() - (18 * 3600 * 1000),
                referralCode = "INDIA99X",
                completedTasksCount = 8,
                role = "USER",
                upiVpa = "rahul.sharma@paytm"
            )
            userProfileDao.insertProfile(defaultProfile)

            // Seed Indian Transactions
            val initialTransactions = listOf(
                TransactionEntity(
                    id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
                    title = "App Install: AngelOne Demat",
                    type = "EARN",
                    points = 1500,
                    amountFormatted = "+1,500 pts (₹150.00)",
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis() - 86400000L
                ),
                TransactionEntity(
                    id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
                    title = "UPI Direct Bank Payout",
                    type = "PAYOUT",
                    points = -5000,
                    amountFormatted = "-5,000 pts (₹500.00)",
                    status = "COMPLETED",
                    payoutMethod = "UPI Transfer",
                    destinationAccount = "rahul.sharma@okhdfcbank",
                    timestamp = System.currentTimeMillis() - (86400000L * 3)
                ),
                TransactionEntity(
                    id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
                    title = "Daily 7-Day Indian Streak Bonus",
                    type = "BONUS",
                    points = 250,
                    amountFormatted = "+250 pts (₹25.00)",
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis() - (86400000L * 2)
                ),
                TransactionEntity(
                    id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
                    title = "Swiggy & Zomato Food Survey",
                    type = "EARN",
                    points = 850,
                    amountFormatted = "+850 pts (₹85.00)",
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis() - (86400000L * 4)
                ),
                TransactionEntity(
                    id = "PO-UPI-98214",
                    title = "Paytm Wallet Withdrawal",
                    type = "PAYOUT",
                    points = -2500,
                    amountFormatted = "-2,500 pts (₹250.00)",
                    status = "PROCESSING",
                    payoutMethod = "Paytm Wallet Cash",
                    destinationAccount = "+91 98765 43210",
                    timestamp = System.currentTimeMillis() - (3600000L * 2)
                )
            )
            transactionDao.insertTransactions(initialTransactions)
        }
    }

    suspend fun registerUser(name: String, email: String, phone: String, referral: String): UserProfileEntity = withContext(Dispatchers.IO) {
        val welcomeBonus = if (referral.isNotBlank()) 500 else 250 // ₹50 or ₹25 bonus for signing up
        val newProfile = UserProfileEntity(
            userId = "usr_${UUID.randomUUID().toString().take(8)}",
            name = name,
            email = email,
            phoneNumber = if (phone.startsWith("+91")) phone else "+91 $phone",
            avatarUrl = "",
            balancePoints = welcomeBonus,
            totalEarnedPoints = welcomeBonus,
            totalWithdrawnPoints = 0,
            streakDays = 1,
            lastCheckInTimestamp = System.currentTimeMillis(),
            referralCode = "EARN" + (1000..9999).random(),
            completedTasksCount = 0,
            role = "USER",
            upiVpa = if (phone.isNotBlank()) "${phone.filter { it.isDigit() }.takeLast(10)}@upi" else "user@upi"
        )
        userProfileDao.insertProfile(newProfile)

        val tx = TransactionEntity(
            id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
            title = "Welcome Signup Bonus (India)",
            type = "BONUS",
            points = welcomeBonus,
            amountFormatted = "+$welcomeBonus pts (₹${welcomeBonus / 10}.00)",
            status = "COMPLETED",
            timestamp = System.currentTimeMillis()
        )
        transactionDao.insertTransaction(tx)

        newProfile
    }

    suspend fun loginUser(emailOrPhone: String, role: String = "USER"): UserProfileEntity = withContext(Dispatchers.IO) {
        var existing = userProfileDao.getUserProfileOnce()
        if (existing == null) {
            existing = UserProfileEntity(
                userId = "usr_india_101",
                name = if (role == "ADMIN") "Administrator" else "Rahul Sharma",
                email = emailOrPhone,
                phoneNumber = "+91 98765 43210",
                role = role,
                balancePoints = 4850,
                totalEarnedPoints = 14500
            )
            userProfileDao.insertProfile(existing)
        } else {
            val updated = existing.copy(
                email = if (emailOrPhone.contains("@")) emailOrPhone else existing.email,
                role = role
            )
            userProfileDao.updateProfile(updated)
            existing = updated
        }
        existing
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
            title = "Task In Review: ${task.title}",
            type = "EARN",
            points = task.pointsReward,
            amountFormatted = "+${task.pointsReward} pts (₹${task.pointsReward / 10}.00)",
            status = "PENDING",
            timestamp = timestamp
        )
        transactionDao.insertTransaction(tx)

        SubmitProofResponse(
            success = true,
            taskId = request.taskId,
            status = "IN_REVIEW",
            message = "Task submitted! Admin will verify and credit ₹${task.pointsReward / 10} to your balance."
        )
    }

    // Admin Action: Approve task submission & credit points
    suspend fun adminApproveProof(taskId: String) = withContext(Dispatchers.IO) {
        val task = taskDao.getTaskById(taskId) ?: return@withContext
        taskDao.updateTaskSubmission(
            taskId = taskId,
            newStatus = TaskStatus.COMPLETED,
            proofUrl = task.submittedProofUrl ?: "https://taskearn.in/proof/verified",
            note = task.submittedNote ?: "Verified by Admin",
            timestamp = System.currentTimeMillis()
        )
        userProfileDao.addPoints(task.pointsReward, task.pointsReward, 1)

        val tx = TransactionEntity(
            id = "TX-${UUID.randomUUID().toString().take(6).uppercase()}",
            title = "Admin Approved: ${task.title}",
            type = "EARN",
            points = task.pointsReward,
            amountFormatted = "+${task.pointsReward} pts (₹${task.pointsReward / 10}.00)",
            status = "COMPLETED",
            timestamp = System.currentTimeMillis()
        )
        transactionDao.insertTransaction(tx)
    }

    // Admin Action: Reject task submission
    suspend fun adminRejectProof(taskId: String, reason: String) = withContext(Dispatchers.IO) {
        taskDao.updateTaskSubmission(
            taskId = taskId,
            newStatus = TaskStatus.REJECTED,
            proofUrl = "",
            note = "Rejected by Admin: $reason",
            timestamp = System.currentTimeMillis()
        )
    }

    // Admin Action: Create New Task for Earners
    suspend fun adminCreateTask(task: TaskEntity) = withContext(Dispatchers.IO) {
        taskDao.insertTask(task)
    }

    // Admin Action: Delete Task
    suspend fun adminDeleteTask(taskId: String) = withContext(Dispatchers.IO) {
        taskDao.deleteTaskById(taskId)
    }

    // Admin Action: Approve Payout Transfer
    suspend fun adminApprovePayout(transactionId: String) = withContext(Dispatchers.IO) {
        transactionDao.updateStatus(transactionId, "COMPLETED")
    }

    // Admin Action: Reject Payout Transfer & refund points
    suspend fun adminRejectPayout(transactionId: String, refundPoints: Int) = withContext(Dispatchers.IO) {
        transactionDao.updateStatus(transactionId, "REJECTED")
        if (refundPoints > 0) {
            userProfileDao.addPoints(refundPoints, 0, 0)
        }
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
                message = "Insufficient points balance. You have ${profile.balancePoints} pts (₹${profile.balancePoints / 10})."
            )
        }

        userProfileDao.deductPointsForPayout(request.pointsToRedeem)

        val txId = "PO-UPI-${UUID.randomUUID().toString().take(6).uppercase()}"
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
            message = "UPI Payout initiated! ₹${String.format("%.2f", request.equivalentAmount)} will be credited to ${request.destinationAccount} shortly."
        )
    }

    suspend fun claimDailyStreak(day: Int, rewardPoints: Int): Boolean = withContext(Dispatchers.IO) {
        val profile = userProfileDao.getUserProfileOnce() ?: return@withContext false
        val now = System.currentTimeMillis()
        val nextStreak = if (day >= 7) 1 else day

        userProfileDao.updateDailyStreak(nextStreak, now, rewardPoints)

        val tx = TransactionEntity(
            id = "TX-STRK-${UUID.randomUUID().toString().take(5).uppercase()}",
            title = "Day $day Indian Streak Bonus",
            type = "BONUS",
            points = rewardPoints,
            amountFormatted = "+$rewardPoints pts (₹${rewardPoints / 10}.00)",
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

    private fun createIndianSeedTasks(): List<TaskEntity> {
        return listOf(
            TaskEntity(
                id = "in_task_01",
                title = "Install & Register: AngelOne Demat Account",
                description = "Download AngelOne app from Play Store, complete mobile OTP register and open free demat.",
                category = TaskCategory.APP_DOWNLOAD,
                pointsReward = 2500, // ₹250.00
                durationMinutes = 5,
                status = TaskStatus.AVAILABLE,
                iconType = "finance",
                isFeatured = true,
                instructions = "1. Click Open App and install AngelOne from Google Play.\n2. Enter your Indian mobile number and verify OTP.\n3. Complete basic KYC or profile details.\n4. Take screenshot of AngelOne Client ID screen.\n5. Upload screenshot or Client ID to get ₹250 (2,500 pts).",
                proofRequirement = "Screenshot of AngelOne app profile showing your verified Client ID."
            ),
            TaskEntity(
                id = "in_task_02",
                title = "Watch: IPL 2026 Match Highlights & Teaser",
                description = "Watch 90 seconds of high-energy cricket highlight trailer and tap the promo link.",
                category = TaskCategory.VIDEO_ADS,
                pointsReward = 500, // ₹50.00
                durationMinutes = 2,
                status = TaskStatus.AVAILABLE,
                iconType = "video",
                isFeatured = true,
                instructions = "1. Watch full IPL cricket match trailer without skipping.\n2. Click the sponsor button at the end of video.\n3. Note down the 6-digit match voucher code displayed.\n4. Enter voucher code below.",
                proofRequirement = "Voucher code from video ending (e.g. IPL-2026-WIN)."
            ),
            TaskEntity(
                id = "in_task_03",
                title = "Swiggy vs Zomato Consumer Food Survey",
                description = "Answer 8 quick questions on your monthly online food ordering and dining preferences.",
                category = TaskCategory.SURVEY,
                pointsReward = 1200, // ₹120.00
                durationMinutes = 6,
                status = TaskStatus.AVAILABLE,
                iconType = "survey",
                isFeatured = true,
                instructions = "1. Answer each multiple-choice question honestly.\n2. Complete all questions regarding food delivery apps in India.\n3. Copy the Survey Confirmation ID at the final screen.\n4. Submit proof to claim 1,200 points (₹120).",
                proofRequirement = "Survey completion verification token (e.g. SWIG-8839)."
            ),
            TaskEntity(
                id = "in_task_04",
                title = "Join Official Indian Earners Telegram Channel",
                description = "Join our Telegram community for daily UPI giveaway codes, new task alerts & proof updates.",
                category = TaskCategory.SOCIAL,
                pointsReward = 600, // ₹60.00
                durationMinutes = 1,
                status = TaskStatus.AVAILABLE,
                iconType = "share",
                isFeatured = false,
                instructions = "1. Open t.me/taskearnin channel.\n2. Tap Join Channel.\n3. Copy your Telegram @username handle.\n4. Paste username here for instant verification.",
                proofRequirement = "Your Telegram username (e.g. @rahul_earner99)."
            ),
            TaskEntity(
                id = "in_task_05",
                title = "Flipkart Big Billion Days Poll: Shopping Plans",
                description = "Vote in today's shopping category poll (Electronics, Fashion, Grocery) & see live votes.",
                category = TaskCategory.QUICK_POLL,
                pointsReward = 200, // ₹20.00
                durationMinutes = 1,
                status = TaskStatus.AVAILABLE,
                iconType = "poll",
                isFeatured = false,
                instructions = "1. Select your top planned shopping category.\n2. Submit your vote.\n3. ₹20 (200 pts) will be credited instantly to your wallet.",
                proofRequirement = "Select your vote option to confirm."
            ),
            TaskEntity(
                id = "in_task_06",
                title = "Install & Sign Up: JioCinema Streaming App",
                description = "Download JioCinema, stream any 3-minute free show and rate 5 stars on Play Store.",
                category = TaskCategory.APP_DOWNLOAD,
                pointsReward = 1800, // ₹180.00
                durationMinutes = 4,
                status = TaskStatus.AVAILABLE,
                iconType = "download",
                isFeatured = false,
                instructions = "1. Install JioCinema app from Google Play.\n2. Login with any Indian mobile number.\n3. Watch 3 minutes of any free video.\n4. Upload screenshot of your JioCinema watch history.",
                proofRequirement = "Screenshot of JioCinema watch history or profile."
            ),
            TaskEntity(
                id = "in_task_07",
                title = "Subscribe & Like: Indian Tech Reviews YouTube",
                description = "Subscribe to our partner YouTube channel, like the latest video & leave a comment.",
                category = TaskCategory.SOCIAL,
                pointsReward = 750, // ₹75.00
                durationMinutes = 2,
                status = TaskStatus.AVAILABLE,
                iconType = "share",
                isFeatured = false,
                instructions = "1. Open the YouTube channel link.\n2. Click Subscribe and turn on notifications.\n3. Like the latest tech review video.\n4. Enter your YouTube channel name/handle.",
                proofRequirement = "Your YouTube handle / email used to subscribe."
            ),
            TaskEntity(
                id = "in_task_08",
                title = "Download BGMI & Register Esports Tournament",
                description = "Register your in-game UID for upcoming free weekend BGMI custom room cash tournament.",
                category = TaskCategory.APP_DOWNLOAD,
                pointsReward = 3500, // ₹350.00
                durationMinutes = 15,
                status = TaskStatus.AVAILABLE,
                iconType = "game",
                isFeatured = true,
                instructions = "1. Open BGMI game.\n2. Copy your BGMI Character ID (10 digits).\n3. Enter your Character ID in the tournament registration box.\n4. Submit proof to receive ₹350 (3,500 pts).",
                proofRequirement = "BGMI Character In-Game Name & Numeric ID (e.g. 5182938491)."
            )
        )
    }
}
