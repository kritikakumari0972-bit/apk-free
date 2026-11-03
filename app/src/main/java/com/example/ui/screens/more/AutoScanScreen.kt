package com.example.ui.screens.more

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HonestyNotice
import com.example.ui.components.MetricGauge
import com.example.ui.components.NovaButton
import com.example.ui.components.NovaCard
import com.example.ui.components.StatRow
import com.example.ui.components.StatusBadge
import com.example.ui.theme.NovaAmber
import com.example.ui.theme.NovaBorder
import com.example.ui.theme.NovaDarkBackground
import com.example.ui.theme.NovaElectricBlue
import com.example.ui.theme.NovaEmerald
import com.example.ui.theme.NovaRose
import com.example.ui.theme.NovaTextMuted
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary
import com.example.ui.viewmodel.NovaMainViewModel

@Composable
fun AutoScanScreen(
    viewModel: NovaMainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val autoScanReport by viewModel.autoScanReport.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NovaDarkBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_auto_scan")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = NovaElectricBlue
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Auto Scan Diagnostic Engine",
                        color = NovaTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Automated 8-point system telemetry verification",
                        color = NovaTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Action Trigger Card
        item {
            NovaCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Central System Audit",
                            color = NovaTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Runs complete non-destructive diagnostics across all hardware subsystems",
                            color = NovaTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    NovaButton(
                        text = if (autoScanReport?.isScanning == true) "Scanning..." else "Run Auto Scan",
                        onClick = { viewModel.runAutoScan() },
                        testTag = "execute_auto_scan_button",
                        icon = if (autoScanReport?.isScanning == true) null else Icons.Default.Speed,
                        enabled = autoScanReport?.isScanning != true
                    )
                }

                if (autoScanReport != null && !autoScanReport!!.isScanning) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = NovaBorder)
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MetricGauge(
                            percent = autoScanReport!!.overallHealthScore,
                            label = "Device Health",
                            subLabel = "Calculated",
                            sizeDp = 100,
                            accentColor = if (autoScanReport!!.overallHealthScore >= 80) NovaEmerald else NovaAmber
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Audit Summary Findings:",
                                color = NovaElectricBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            autoScanReport!!.keyFindings.forEach { finding ->
                                Text(text = "• $finding", color = NovaTextSecondary, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // Scan Step Cards
        if (autoScanReport != null) {
            val report = autoScanReport!!
            item {
                Text(
                    text = "DIAGNOSTIC SUBSYSTEM CHECKLIST",
                    color = NovaElectricBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            items(report.steps) { step ->
                NovaCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (step.isComplete) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = NovaEmerald, modifier = Modifier.size(18.dp))
                            } else {
                                CircularProgressIndicator(color = NovaElectricBlue, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = step.title, color = NovaTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        StatusBadge(text = step.status, color = if (step.isComplete) NovaElectricBlue else NovaAmber)
                    }
                    if (step.details.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = step.details, color = NovaTextSecondary, fontSize = 11.sp)
                    }
                }
            }
        }

        item {
            HonestyNotice(
                title = "Safe Read-Only Guarantee",
                message = "The Auto Scan diagnostic engine only performs inspections and audits. It never deletes files, never closes background apps, and never changes system settings without explicit user authorization."
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
