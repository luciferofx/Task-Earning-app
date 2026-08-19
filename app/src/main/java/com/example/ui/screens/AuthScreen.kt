package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GeoBgDark
import com.example.ui.theme.GeoBorderDark
import com.example.ui.theme.GeoBorderMuted
import com.example.ui.theme.GeoCategoryDailyBg
import com.example.ui.theme.GeoCategoryDailyFg
import com.example.ui.theme.GeoCategorySocialBg
import com.example.ui.theme.GeoCategorySocialFg
import com.example.ui.theme.GeoGoldAccent
import com.example.ui.theme.GeoOnPrimaryContainer
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
fun AuthScreen(
    isLoading: Boolean,
    onLogin: (emailOrPhone: String, password: String) -> Unit,
    onRegister: (name: String, email: String, phone: String, password: String, referral: String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Login, 1 = Register

    // Login Form States
    var loginIdentifier by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var isLoginPasswordVisible by remember { mutableStateOf(false) }

    // Register Form States
    var regName by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var regReferral by remember { mutableStateOf("") }
    var isRegPasswordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GeoBgDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Brand Logo & Title
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(GeoPrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Wallet,
                    contentDescription = "TaskEarn India",
                    tint = GeoPrimaryDark,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "TaskEarn India",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = GeoTextWhite,
                letterSpacing = 0.5.sp
            )

            Text(
                text = "Complete Easy Tasks • Earn Instant UPI Cash (₹)",
                style = MaterialTheme.typography.bodyMedium,
                color = GeoTextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Auth Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                border = BorderStroke(1.dp, GeoBorderDark)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Tab Selector
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = GeoSurfaceElevated,
                        contentColor = GeoPrimary,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = GeoPrimary,
                                height = 3.dp
                            )
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .border(BorderStroke(1.dp, GeoBorderDark), RoundedCornerShape(14.dp))
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = {
                                Text(
                                    text = "LOGIN",
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == 0) GeoPrimary else GeoTextMuted,
                                    letterSpacing = 1.sp
                                )
                            },
                            modifier = Modifier.testTag("tab_login")
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = {
                                Text(
                                    text = "REGISTER",
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == 1) GeoPrimary else GeoTextMuted,
                                    letterSpacing = 1.sp
                                )
                            },
                            modifier = Modifier.testTag("tab_register")
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (selectedTab == 0) {
                        // LOGIN FORM
                        Text(
                            text = "Email, Mobile (+91) or Admin ID",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = loginIdentifier,
                            onValueChange = { loginIdentifier = it },
                            placeholder = { Text("e.g. rahul@gmail.com or admin", color = GeoTextMuted) },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = GeoPrimary)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_identifier_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeoPrimary,
                                unfocusedBorderColor = GeoBorderDark,
                                focusedContainerColor = GeoSurfaceElevated,
                                unfocusedContainerColor = GeoSurfaceElevated,
                                focusedTextColor = GeoTextWhite,
                                unfocusedTextColor = GeoTextWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Password / Admin Passcode",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = loginPassword,
                            onValueChange = { loginPassword = it },
                            placeholder = { Text("Enter your password", color = GeoTextMuted) },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = GeoPrimary)
                            },
                            trailingIcon = {
                                IconButton(onClick = { isLoginPasswordVisible = !isLoginPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isLoginPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password visibility",
                                        tint = GeoTextMuted
                                    )
                                }
                            },
                            visualTransformation = if (isLoginPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_password_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeoPrimary,
                                unfocusedBorderColor = GeoBorderDark,
                                focusedContainerColor = GeoSurfaceElevated,
                                unfocusedContainerColor = GeoSurfaceElevated,
                                focusedTextColor = GeoTextWhite,
                                unfocusedTextColor = GeoTextWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                val id = loginIdentifier.ifBlank { "rahul.sharma@gmail.com" }
                                val pass = loginPassword.ifBlank { "password123" }
                                onLogin(id, pass)
                            },
                            enabled = !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("login_submit_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GeoPrimary,
                                contentColor = GeoPrimaryDark
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(22.dp), color = GeoPrimaryDark, strokeWidth = 2.5.dp)
                            } else {
                                Text(
                                    text = "LOGIN TO ACCOUNT",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quick Test Chips
                        Text(
                            text = "QUICK TEST AUTO-FILL",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextMuted,
                            letterSpacing = 1.2.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = GeoCategoryDailyBg,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        loginIdentifier = "rahul.sharma@gmail.com"
                                        loginPassword = "userpass123"
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "🇮🇳 User Demo",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = GeoCategoryDailyFg
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = GeoCategorySocialBg,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        loginIdentifier = "admin@taskearn.in"
                                        loginPassword = "admin"
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        Icons.Default.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = GeoCategorySocialFg,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "🛡️ Admin Demo",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = GeoCategorySocialFg
                                    )
                                }
                            }
                        }

                    } else {
                        // REGISTER FORM
                        // Welcome bonus banner
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = GeoCategoryDailyBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Redeem, contentDescription = null, tint = GeoCategoryDailyFg)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Instant ₹50 Joining Bonus!",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = GeoCategoryDailyFg
                                    )
                                    Text(
                                        text = "Get 500 points instantly credited to your Indian UPI wallet.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GeoCategoryDailyFg.copy(alpha = 0.85f),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Full Name",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = regName,
                            onValueChange = { regName = it },
                            placeholder = { Text("e.g. Rahul Sharma", color = GeoTextMuted) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = GeoPrimary) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_name_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeoPrimary,
                                unfocusedBorderColor = GeoBorderDark,
                                focusedContainerColor = GeoSurfaceElevated,
                                unfocusedContainerColor = GeoSurfaceElevated,
                                focusedTextColor = GeoTextWhite,
                                unfocusedTextColor = GeoTextWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Email Address",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = regEmail,
                            onValueChange = { regEmail = it },
                            placeholder = { Text("rahul.sharma@gmail.com", color = GeoTextMuted) },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = GeoPrimary) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_email_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeoPrimary,
                                unfocusedBorderColor = GeoBorderDark,
                                focusedContainerColor = GeoSurfaceElevated,
                                unfocusedContainerColor = GeoSurfaceElevated,
                                focusedTextColor = GeoTextWhite,
                                unfocusedTextColor = GeoTextWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Indian Mobile (+91)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = regPhone,
                            onValueChange = { regPhone = it },
                            placeholder = { Text("98765 43210", color = GeoTextMuted) },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = GeoPrimary) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_phone_input"),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeoPrimary,
                                unfocusedBorderColor = GeoBorderDark,
                                focusedContainerColor = GeoSurfaceElevated,
                                unfocusedContainerColor = GeoSurfaceElevated,
                                focusedTextColor = GeoTextWhite,
                                unfocusedTextColor = GeoTextWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Create Password",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = regPassword,
                            onValueChange = { regPassword = it },
                            placeholder = { Text("Minimum 6 characters", color = GeoTextMuted) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = GeoPrimary) },
                            trailingIcon = {
                                IconButton(onClick = { isRegPasswordVisible = !isRegPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isRegPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = GeoTextMuted
                                    )
                                }
                            },
                            visualTransformation = if (isRegPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_password_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeoPrimary,
                                unfocusedBorderColor = GeoBorderDark,
                                focusedContainerColor = GeoSurfaceElevated,
                                unfocusedContainerColor = GeoSurfaceElevated,
                                focusedTextColor = GeoTextWhite,
                                unfocusedTextColor = GeoTextWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Referral Code (Optional - Extra ₹25 Bonus)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = regReferral,
                            onValueChange = { regReferral = it.uppercase() },
                            placeholder = { Text("e.g. INDIA99X", color = GeoTextMuted) },
                            leadingIcon = { Icon(Icons.Default.Redeem, contentDescription = null, tint = GeoGoldAccent) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_referral_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeoGoldAccent,
                                unfocusedBorderColor = GeoBorderDark,
                                focusedContainerColor = GeoSurfaceElevated,
                                unfocusedContainerColor = GeoSurfaceElevated,
                                focusedTextColor = GeoTextWhite,
                                unfocusedTextColor = GeoTextWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                val name = regName.ifBlank { "Rahul Sharma" }
                                val email = regEmail.ifBlank { "rahul.sharma@gmail.com" }
                                val phone = regPhone.ifBlank { "9876543210" }
                                val pass = regPassword.ifBlank { "pass12345" }
                                onRegister(name, email, phone, pass, regReferral)
                            },
                            enabled = !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("register_submit_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GeoPrimary,
                                contentColor = GeoPrimaryDark
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(22.dp), color = GeoPrimaryDark, strokeWidth = 2.5.dp)
                            } else {
                                Text(
                                    text = "REGISTER & CLAIM ₹50",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Trust badge & note
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = GeoSuccessGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "100% Safe • Instant UPI / Paytm Withdrawals in India",
                    style = MaterialTheme.typography.labelSmall,
                    color = GeoTextMuted
                )
            }
        }
    }
}

