package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.Currency
import com.example.data.model.PayoutMethod
import com.example.ui.components.BalanceHeroCard
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
import com.example.ui.theme.GeoPrimary
import com.example.ui.theme.GeoPrimaryContainer
import com.example.ui.theme.GeoPrimaryDark
import com.example.ui.theme.GeoSuccessGreen
import com.example.ui.theme.GeoSurfaceDark
import com.example.ui.theme.GeoSurfaceElevated
import com.example.ui.theme.GeoTextMuted
import com.example.ui.theme.GeoTextPrimary
import com.example.ui.theme.GeoTextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WalletScreen(
    userProfile: UserProfileEntity?,
    transactions: List<TransactionEntity>,
    selectedCurrency: Currency,
    onCurrencySelect: (Currency) -> Unit,
    onRequestPayoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTxFilter by remember { mutableStateOf("ALL") }

    val filteredTransactions = remember(transactions, selectedTxFilter) {
        when (selectedTxFilter) {
            "EARN" -> transactions.filter { it.type == "EARN" }
            "PAYOUT" -> transactions.filter { it.type == "PAYOUT" }
            "BONUS" -> transactions.filter { it.type == "BONUS" }
            else -> transactions
        }
    }

    val balance = userProfile?.balancePoints ?: 2450
    val minThreshold = 5000
    val progress = (balance.toFloat() / minThreshold.toFloat()).coerceIn(0f, 1f)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBgDark)
            .testTag("wallet_screen_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "REWARD WALLET",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = GeoPrimary,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Balance & Withdrawals",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextWhite
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Balance Card
                BalanceHeroCard(
                    userProfile = userProfile,
                    selectedCurrency = selectedCurrency,
                    onCurrencySelect = onCurrencySelect,
                    onWithdrawClick = onRequestPayoutClick,
                    onStreakClick = {}
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Payout Progress Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                    border = BorderStroke(1.dp, GeoBorderDark)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Next Payout Threshold ($5.00)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = GeoTextWhite
                            )
                            Text(
                                text = "%,d / %,d pts".format(balance, minThreshold),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (balance >= minThreshold) GeoSuccessGreen else GeoPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = if (balance >= minThreshold) GeoSuccessGreen else GeoPrimary,
                            trackColor = GeoSurfaceElevated
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (balance >= minThreshold)
                                "You have reached the minimum withdrawal threshold."
                            else
                                "Earn %,d more points to unlock your next payout.".format(minThreshold - balance),
                            style = MaterialTheme.typography.bodySmall,
                            color = GeoTextMuted
                        )
                    }
                }
            }
        }

        // Payout Visual Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                border = BorderStroke(1.dp, GeoBorderDark)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.wallet_payout_art_1787159451154),
                        contentDescription = "Payout Methods Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // Payout Methods Selection
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                Text(
                    text = "SUPPORTED PAYOUT OPTIONS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = GeoPrimary,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                PayoutMethod.entries.forEach { method ->
                    PayoutMethodItem(
                        method = method,
                        canAfford = balance >= method.minPoints,
                        onClick = onRequestPayoutClick
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Transaction History Header
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TRANSACTION HISTORY (${filteredTransactions.size})",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = GeoPrimary,
                        letterSpacing = 1.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val filters = listOf("ALL" to "All", "EARN" to "Earned", "PAYOUT" to "Payouts", "BONUS" to "Bonuses")
                    items(filters) { (key, label) ->
                        val isSelected = selectedTxFilter == key
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTxFilter = key },
                            label = {
                                Text(
                                    label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) GeoPrimaryDark else GeoTextPrimary
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
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        // Transaction List
        if (filteredTransactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                    border = BorderStroke(1.dp, GeoBorderDark)
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No transactions yet in this category.", style = MaterialTheme.typography.bodyMedium, color = GeoTextMuted)
                    }
                }
            }
        } else {
            items(filteredTransactions, key = { it.id }) { tx ->
                TransactionRowItem(
                    tx = tx,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun PayoutMethodItem(
    method: PayoutMethod,
    canAfford: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("payout_method_${method.name}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
        border = BorderStroke(1.dp, GeoBorderDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when (method) {
                                PayoutMethod.UPI_INSTANT -> GeoCategoryDailyBg
                                PayoutMethod.PAYTM_WALLET -> GeoCategoryGamingBg
                                PayoutMethod.AMAZON_PAY_IN -> GeoPrimaryContainer
                                PayoutMethod.GOOGLE_PLAY_IN -> GeoCategorySocialBg
                                PayoutMethod.BANK_IMPS -> GeoCategoryDailyBg
                                PayoutMethod.CRYPTO_USDT -> GeoCategorySurveyBg
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (method) {
                            PayoutMethod.UPI_INSTANT -> Icons.Default.AccountBalance
                            PayoutMethod.PAYTM_WALLET -> Icons.Default.Payment
                            PayoutMethod.AMAZON_PAY_IN -> Icons.Default.CardGiftcard
                            PayoutMethod.GOOGLE_PLAY_IN -> Icons.Default.Shop
                            PayoutMethod.BANK_IMPS -> Icons.Default.AccountBalance
                            PayoutMethod.CRYPTO_USDT -> Icons.Default.CurrencyBitcoin
                        },
                        contentDescription = method.title,
                        tint = when (method) {
                            PayoutMethod.UPI_INSTANT -> GeoCategoryDailyFg
                            PayoutMethod.PAYTM_WALLET -> GeoCategoryGamingFg
                            PayoutMethod.AMAZON_PAY_IN -> GeoPrimaryDark
                            PayoutMethod.GOOGLE_PLAY_IN -> GeoCategorySocialFg
                            PayoutMethod.BANK_IMPS -> GeoCategoryDailyFg
                            PayoutMethod.CRYPTO_USDT -> GeoCategorySurveyFg
                        },
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = method.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GeoTextWhite
                    )
                    Text(
                        text = "Min. %,d pts (₹${method.minPoints / 10})".format(method.minPoints),
                        style = MaterialTheme.typography.labelSmall,
                        color = GeoTextMuted
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (canAfford) GeoSuccessGreen.copy(alpha = 0.15f) else GeoSurfaceElevated,
                border = BorderStroke(1.dp, if (canAfford) GeoSuccessGreen.copy(alpha = 0.4f) else GeoBorderDark)
            ) {
                Text(
                    text = if (canAfford) "Ready" else "Locked",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (canAfford) GeoSuccessGreen else GeoTextMuted,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun TransactionRowItem(
    tx: TransactionEntity,
    modifier: Modifier = Modifier
) {
    val isCredit = tx.points >= 0
    val dateFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
    val dateString = dateFormat.format(Date(tx.timestamp))

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
        border = BorderStroke(1.dp, GeoBorderDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isCredit) GeoSuccessGreen.copy(alpha = 0.15f) else GeoDangerRed.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (tx.type) {
                            "EARN" -> Icons.Default.MonetizationOn
                            "PAYOUT" -> Icons.Default.AccountBalanceWallet
                            "BONUS" -> Icons.Default.EmojiEvents
                            else -> Icons.Default.CheckCircle
                        },
                        contentDescription = null,
                        tint = if (isCredit) GeoSuccessGreen else GeoDangerRed,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = tx.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = GeoTextWhite,
                        maxLines = 1
                    )
                    Text(
                        text = dateString,
                        style = MaterialTheme.typography.labelSmall,
                        color = GeoTextMuted
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = tx.amountFormatted,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isCredit) GeoSuccessGreen else GeoDangerRed
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (tx.status) {
                        "COMPLETED" -> GeoSuccessGreen.copy(alpha = 0.15f)
                        "PROCESSING" -> GeoPrimary.copy(alpha = 0.15f)
                        else -> GeoSurfaceElevated
                    }
                ) {
                    Text(
                        text = tx.status,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = when (tx.status) {
                            "COMPLETED" -> GeoSuccessGreen
                            "PROCESSING" -> GeoPrimary
                            else -> GeoTextMuted
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
