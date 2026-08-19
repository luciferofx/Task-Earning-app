package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.SecurityAuditResult
import com.example.ui.theme.GeoBgDark
import com.example.ui.theme.GeoBorderDark
import com.example.ui.theme.GeoBorderMuted
import com.example.ui.theme.GeoCategoryDailyBg
import com.example.ui.theme.GeoCategoryDailyFg
import com.example.ui.theme.GeoCategorySocialBg
import com.example.ui.theme.GeoCategorySocialFg
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

@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity?,
    securityAudit: SecurityAuditResult?,
    onOpenAuthDialog: () -> Unit,
    onOpenSecurityDialog: () -> Unit,
    onSwitchToAdmin: () -> Unit = {},
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val referralCode = userProfile?.referralCode ?: "INDIA99X"
    val userName = userProfile?.name ?: "Rahul Sharma"
    val userEmail = userProfile?.email ?: "rahul.sharma@gmail.com"
    val userPhone = userProfile?.phoneNumber ?: "+91 98765 43210"
    val userUpi = userProfile?.upiVpa ?: "rahul.sharma@paytm"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBgDark)
            .testTag("profile_screen_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Geometric Profile Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                border = BorderStroke(1.dp, GeoBorderDark)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(GeoSurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.Transparent,
                                border = BorderStroke(1.dp, GeoBorderMuted),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = rememberInitials(userName),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = GeoPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Black,
                                    color = GeoTextWhite
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified Indian Earner",
                                    tint = GeoSuccessGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = userEmail,
                                style = MaterialTheme.typography.bodySmall,
                                color = GeoTextMuted
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = GeoPrimary, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = userPhone,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GeoPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = GeoBorderDark, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProfileStatItem(
                            label = "BALANCE (₹)",
                            value = "₹${(userProfile?.balancePoints ?: 4850) / 10}",
                            color = GeoPrimary
                        )
                        ProfileStatItem(
                            label = "TOTAL EARNED",
                            value = "₹${(userProfile?.totalEarnedPoints ?: 14500) / 10}",
                            color = GeoGoldAccent
                        )
                        ProfileStatItem(
                            label = "TASKS DONE",
                            value = "${userProfile?.completedTasksCount ?: 8}",
                            color = GeoSuccessGreen
                        )
                    }
                }
            }
        }

        // UPI & Payment Info Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                border = BorderStroke(1.dp, GeoBorderDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(GeoCategoryDailyBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = GeoCategoryDailyFg, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Default Indian UPI ID",
                                style = MaterialTheme.typography.labelSmall,
                                color = GeoTextMuted,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = userUpi,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Black,
                                color = GeoTextWhite
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GeoCategoryDailyBg
                    ) {
                        Text(
                            text = "INSTANT UPI",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = GeoCategoryDailyFg,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Indian Referral Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                border = BorderStroke(1.dp, GeoBorderDark)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = null,
                                tint = GeoGoldAccent,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Refer & Earn ₹50 Per Friend",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = GeoTextWhite
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GeoPrimaryContainer
                        ) {
                            Text(
                                text = "+500 PTS",
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                color = GeoPrimaryDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GeoSurfaceElevated,
                        border = BorderStroke(1.dp, GeoBorderDark),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "YOUR REFERRAL CODE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GeoTextMuted,
                                    fontSize = 9.sp,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = referralCode,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = GeoPrimary,
                                    letterSpacing = 2.sp
                                )
                            }

                            Button(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                    val clip = ClipData.newPlainText("Referral Code", referralCode)
                                    clipboard?.setPrimaryClip(clip)
                                    Toast.makeText(context, "Referral code copied!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GeoPrimary,
                                    contentColor = GeoPrimaryDark
                                )
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Copy", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Security & Device Audit Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .clickable { onOpenSecurityDialog() },
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = GeoPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Anti-Fraud & Security Audit",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = GeoTextWhite
                                )
                                Text(
                                    text = "Integrity Score: ${securityAudit?.integrityScore ?: 100}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GeoSuccessGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = onOpenSecurityDialog,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, GeoBorderDark)
                        ) {
                            Text("Audit", fontSize = 12.sp, color = GeoPrimary)
                        }
                    }
                }
            }
        }

        // Admin Portal & Logout Actions
        item {
            Column(modifier = Modifier.padding(20.dp)) {
                // Quick Admin Switch Portal
                Button(
                    onClick = onSwitchToAdmin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("admin_portal_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GeoSurfaceElevated,
                        contentColor = GeoTextWhite
                    ),
                    border = BorderStroke(1.dp, GeoBorderDark)
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = GeoPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Admin Dashboard Portal", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Logout Button
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("logout_btn"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, GeoDangerRed.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GeoDangerRed)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = GeoDangerRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout of Account", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileStatItem(
    label: String,
    value: String,
    color: Color
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = GeoTextMuted,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = color
        )
    }
}

