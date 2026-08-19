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
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldReward
import com.example.ui.theme.GoldRewardDark
import com.example.ui.theme.GoldRewardLight
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.PrimaryIndigoLight
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.VioletAccent
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

    val balance = userProfile?.balancePoints ?: 0
    val minThreshold = 5000
    val progress = (balance.toFloat() / minThreshold.toFloat()).coerceIn(0f, 1f)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("wallet_screen_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "My Reward Wallet 💳",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Redeem points directly to PayPal, UPI, or Gift Cards.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

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
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Next Payout Threshold ($5.00)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "%,d / %,d pts".format(balance, minThreshold),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (balance >= minThreshold) EmeraldSuccess else PrimaryIndigo
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(CircleShape),
                            color = if (balance >= minThreshold) EmeraldSuccess else GoldReward,
                            trackColor = MaterialTheme.colorScheme.surface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (balance >= minThreshold)
                                "🎉 You have reached the minimum withdrawal requirement!"
                            else
                                "Earn %,d more points to unlock your next payout.".format(minThreshold - balance),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Payout Visual Asset Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.wallet_payout_art_1787159451154),
                        contentDescription = "Payout Methods Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(115.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // Payout Methods Selection
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = "Supported Payout Options",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
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
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            tint = PrimaryIndigo,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Transaction History (${filteredTransactions.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val filters = listOf("ALL" to "All", "EARN" to "Earned", "PAYOUT" to "Payouts", "BONUS" to "Bonuses")
                    items(filters) { (key, label) ->
                        val isSelected = selectedTxFilter == key
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTxFilter = key },
                            label = { Text(label, fontSize = 12.sp) },
                            shape = RoundedCornerShape(10.dp)
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
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No transactions yet in this category.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        } else {
            items(filteredTransactions, key = { it.id }) { tx ->
                TransactionRowItem(
                    tx = tx,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
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
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
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
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            when (method) {
                                PayoutMethod.PAYPAL -> PrimaryIndigo.copy(alpha = 0.15f)
                                PayoutMethod.UPI -> EmeraldSuccess.copy(alpha = 0.15f)
                                PayoutMethod.AMAZON_GIFT -> GoldReward.copy(alpha = 0.15f)
                                PayoutMethod.PLAY_STORE -> CyanAccent.copy(alpha = 0.15f)
                                PayoutMethod.CRYPTO_USDT -> VioletAccent.copy(alpha = 0.15f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (method) {
                            PayoutMethod.PAYPAL -> Icons.Default.Payment
                            PayoutMethod.UPI -> Icons.Default.AccountBalance
                            PayoutMethod.AMAZON_GIFT -> Icons.Default.CardGiftcard
                            PayoutMethod.PLAY_STORE -> Icons.Default.Shop
                            PayoutMethod.CRYPTO_USDT -> Icons.Default.CurrencyBitcoin
                        },
                        contentDescription = method.title,
                        tint = when (method) {
                            PayoutMethod.PAYPAL -> PrimaryIndigo
                            PayoutMethod.UPI -> EmeraldSuccess
                            PayoutMethod.AMAZON_GIFT -> GoldRewardDark
                            PayoutMethod.PLAY_STORE -> CyanAccent
                            PayoutMethod.CRYPTO_USDT -> VioletAccent
                        },
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = method.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Min. %,d pts ($${String.format("%.2f", method.minPoints / 1000.0)})".format(method.minPoints),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (canAfford) EmeraldSuccess.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = if (canAfford) "Ready" else "Locked",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (canAfford) EmeraldSuccess else MaterialTheme.colorScheme.onSurfaceVariant,
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
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
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
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isCredit) EmeraldSuccess.copy(alpha = 0.15f) else RoseDanger.copy(alpha = 0.15f)),
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
                        tint = if (isCredit) EmeraldSuccess else RoseDanger,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = tx.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                    Text(
                        text = dateString,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = tx.amountFormatted,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isCredit) EmeraldSuccess else RoseDanger
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (tx.status) {
                        "COMPLETED" -> EmeraldSuccess.copy(alpha = 0.15f)
                        "PROCESSING" -> GoldReward.copy(alpha = 0.15f)
                        else -> PrimaryIndigo.copy(alpha = 0.15f)
                    }
                ) {
                    Text(
                        text = tx.status,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = when (tx.status) {
                            "COMPLETED" -> EmeraldSuccess
                            "PROCESSING" -> GoldRewardDark
                            else -> PrimaryIndigo
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
