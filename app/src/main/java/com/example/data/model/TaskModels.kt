package com.example.data.model

enum class UserRole {
    USER,
    ADMIN
}

data class AuthUser(
    val id: String,
    val name: String,
    val email: String,
    val phone: String = "",
    val role: UserRole = UserRole.USER,
    val avatarUrl: String = "",
    val referralCode: String = "EARN99X"
)

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

enum class PayoutMethod(
    val title: String,
    val subtitle: String,
    val minPoints: Int,
    val iconType: String,
    val isIndianFavorite: Boolean = true
) {
    UPI_INSTANT("UPI (GPay / PhonePe / Paytm)", "Instant bank deposit via UPI VPA (e.g. name@upi)", 1000, "upi", true),
    PAYTM_WALLET("Paytm Wallet Cash", "Direct transfer to 10-digit mobile number", 500, "paytm", true),
    AMAZON_PAY_IN("Amazon Pay India Voucher", "Instant e-gift code delivered to email/SMS", 2500, "amazon", true),
    GOOGLE_PLAY_IN("Google Play India Gift Code", "Play Store recharge code for game passes", 1000, "playstore", true),
    BANK_IMPS("Direct Bank Transfer (IMPS)", "Instant NEFT/IMPS to Indian bank account", 5000, "bank", true),
    CRYPTO_USDT("Crypto USDT (TRC20 / BEP20)", "For global / crypto withdrawals", 10000, "crypto", false)
}

enum class Currency(val code: String, val symbol: String, val pointsPerUnit: Double) {
    INR("INR", "₹", 10.0),        // 10 points = ₹1.00 INR (1,000 points = ₹100.00 INR)
    USD("USD", "$", 1000.0),      // 1,000 points = $1.00 USD
    EUR("EUR", "€", 1080.0)       // 1,080 points = €1.00 EUR
}

data class TaskStepItem(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false
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

data class AdminAnalytics(
    val totalUsers: Int = 1420,
    val totalEarningsDisbursedInr: Double = 64850.0,
    val totalTasksLive: Int = 12,
    val pendingProofSubmissions: Int = 3,
    val pendingPayoutRequests: Int = 2,
    val fraudAlertsCount: Int = 1
)
