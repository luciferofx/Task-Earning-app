package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.TaskEntity
import com.example.data.model.Currency
import com.example.data.model.TaskStatus
import com.example.ui.components.StatusBadge
import com.example.ui.components.getCategoryBgColor
import com.example.ui.components.getCategoryFgColor
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.GeoBgDark
import com.example.ui.theme.GeoBorderDark
import com.example.ui.theme.GeoDangerRed
import com.example.ui.theme.GeoPrimary
import com.example.ui.theme.GeoPrimaryContainer
import com.example.ui.theme.GeoPrimaryDark
import com.example.ui.theme.GeoSuccessGreen
import com.example.ui.theme.GeoSurfaceDark
import com.example.ui.theme.GeoSurfaceElevated
import com.example.ui.theme.GeoTextMuted
import com.example.ui.theme.GeoTextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    task: TaskEntity?,
    currency: Currency,
    onBack: () -> Unit,
    onSubmitProofClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (task == null) {
        Box(modifier = Modifier.fillMaxSize().background(GeoBgDark), contentAlignment = Alignment.Center) {
            Text("Task not found.", color = GeoTextMuted)
        }
        return
    }

    val cashValuation = when (currency) {
        Currency.USD -> task.pointsReward / 100.0
        Currency.INR -> task.pointsReward / 1.2
        Currency.EUR -> task.pointsReward / 108.0
    }

    Scaffold(
        containerColor = GeoBgDark,
        topBar = {
            TopAppBar(
                title = { Text("Task Details", fontWeight = FontWeight.Bold, color = GeoTextWhite) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GeoTextWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GeoBgDark
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("task_detail_screen"),
            contentPadding = PaddingValues(20.dp)
        ) {
            // Task Header Card
            item {
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(getCategoryBgColor(task.category)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getCategoryIcon(task.category),
                                    contentDescription = task.category.displayName,
                                    tint = getCategoryFgColor(task.category),
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            StatusBadge(status = task.status)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextWhite
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = GeoTextMuted
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Reward Geometric Box
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = GeoPrimary,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "TASK REWARD",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = GeoPrimaryDark.copy(alpha = 0.8f),
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "+${task.pointsReward} Points",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Black,
                                        color = GeoPrimaryDark
                                    )
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = GeoPrimaryDark.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = "≈ ${currency.symbol}${String.format("%.2f", cashValuation)}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = GeoPrimaryDark,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Instructions & Step-by-Step
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                    border = BorderStroke(1.dp, GeoBorderDark)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "HOW TO COMPLETE",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = GeoPrimary,
                            letterSpacing = 1.5.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val steps = task.instructions.lines().filter { it.isNotBlank() }.ifEmpty { listOf(task.instructions) }
                        steps.forEachIndexed { index, step ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(GeoPrimaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = GeoPrimaryDark
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = step,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = GeoTextWhite,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Proof Requirement Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GeoSurfaceDark),
                    border = BorderStroke(1.dp, GeoBorderDark)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "PROOF REQUIRED",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = GeoPrimary,
                            letterSpacing = 1.5.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = task.proofRequirement,
                            style = MaterialTheme.typography.bodyMedium,
                            color = GeoTextWhite
                        )

                        if (task.submittedProofUrl != null || task.submittedNote != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = GeoSurfaceElevated
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Your Submitted Proof:",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = GeoPrimary
                                    )
                                    task.submittedProofUrl?.let { url ->
                                        Text(text = "Link: $url", style = MaterialTheme.typography.bodySmall, color = GeoTextWhite)
                                    }
                                    task.submittedNote?.let { note ->
                                        Text(text = "Note: $note", style = MaterialTheme.typography.bodySmall, color = GeoTextMuted)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Action Buttons
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    when (task.status) {
                        TaskStatus.AVAILABLE -> {
                            Button(
                                onClick = onSubmitProofClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("submit_proof_cta"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GeoPrimary,
                                    contentColor = GeoPrimaryDark
                                )
                            ) {
                                Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Complete & Submit Proof", fontWeight = FontWeight.Bold)
                            }
                        }
                        TaskStatus.IN_REVIEW -> {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                color = GeoSurfaceDark,
                                border = BorderStroke(1.dp, GeoBorderDark)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Timer, contentDescription = null, tint = GeoPrimary)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Proof under automated & manual review (ETA: 2-4 hrs)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = GeoTextWhite
                                    )
                                }
                            }
                        }
                        TaskStatus.COMPLETED -> {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                color = GeoSuccessGreen.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, GeoSuccessGreen.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GeoSuccessGreen)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Task Completed • +${task.pointsReward} Points Credited",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = GeoSuccessGreen
                                    )
                                }
                            }
                        }
                        TaskStatus.REJECTED -> {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                color = GeoDangerRed.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, GeoDangerRed.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = GeoDangerRed)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Proof rejected. Please review requirements and try again.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = GeoDangerRed
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
