package com.example.data.model

enum class TaskCategory(val displayName: String, val iconName: String) {
    ALL("All Tasks", "all"),
    APP_DOWNLOAD("App Install", "download"),
    VIDEO_ADS("Watch & Earn", "video"),
    SURVEY("Surveys", "poll"),
    SOCIAL("Social Follow", "share"),
    QUICK_POLL("Daily Polls", "check_circle")
}

enum class TaskStatus(val displayName: String) {
    AVAILABLE("Available"),
    IN_REVIEW("In Review"),
    COMPLETED("Completed"),
    REJECTED("Rejected")
}

enum class PayoutMethod(val title: String, val subtitle: String, val minPoints: Int, val iconType: String) {
    PAYPAL("PayPal Transfer", "Instant cash to PayPal email", 5000, "paypal"),
    UPI("UPI / Direct Bank", "Instant transfer to any UPI VPA", 3000, "upi"),
    AMAZON_GIFT("Amazon Gift Card", "Voucher code sent to email", 5000, "amazon"),
    PLAY_STORE("Google Play Code", "Redeemable Google Play balance", 3000, "playstore"),
    CRYPTO_USDT("Crypto (USDT / TRC20)", "Direct to your USDT crypto wallet", 10000, "crypto")
}

enum class Currency(val code: String, val symbol: String, val pointsPerUnit: Double) {
    USD("USD", "$", 1000.0),      // 1,000 points = $1.00 USD
    INR("INR", "₹", 12.0),        // 12 points = ₹1.00 INR (approx ₹83 per 1000 pts)
    EUR("EUR", "€", 1080.0)       // 1,080 points = €1.00 EUR
}

data class TaskStepItem(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false
)

// API Request/Response Schemas for Backend Integration (Node.js / Retrofit)
data class FetchTasksResponse(
    val success: Boolean,
    val tasks: List<TaskDto>,
    val totalCount: Int,
    val message: String? = null
)

data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val pointsReward: Int,
    val durationMinutes: Int,
    val status: String,
    val instructions: String,
    val proofRequirement: String,
    val isFeatured: Boolean = false
)

data class SubmitProofRequest(
    val taskId: String,
    val userId: String,
    val proofUrl: String,
    val proofNote: String,
    val screenshotLocalPath: String? = null,
    val deviceFingerprint: String
)

data class SubmitProofResponse(
    val success: Boolean,
    val taskId: String,
    val status: String,
    val message: String,
    val pointsAwardedIfInstant: Int = 0
)

data class PayoutRequest(
    val userId: String,
    val payoutMethod: String,
    val destinationAccount: String,
    val pointsToRedeem: Int,
    val currencyCode: String,
    val equivalentAmount: Double
)

data class PayoutResponse(
    val success: Boolean,
    val transactionId: String,
    val status: String,
    val remainingBalancePoints: Int,
    val message: String
)

data class SecurityAuditResult(
    val isRooted: Boolean,
    val isVpnOrProxy: Boolean,
    val deviceId: String,
    val isSafeToEarn: Boolean,
    val statusSummary: String,
    val integrityScore: Int // 0 - 100
)
