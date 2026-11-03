package com.example.ui.screens.more

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HonestyNotice
import com.example.ui.components.NovaButton
import com.example.ui.components.NovaCard
import com.example.ui.components.StatRow
import com.example.ui.components.StatusBadge
import com.example.ui.theme.NovaAmber
import com.example.ui.theme.NovaBorder
import com.example.ui.theme.NovaDarkBackground
import com.example.ui.theme.NovaDarkSurfaceVariant
import com.example.ui.theme.NovaElectricBlue
import com.example.ui.theme.NovaEmerald
import com.example.ui.theme.NovaPurple
import com.example.ui.theme.NovaTextMuted
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary
import com.example.ui.viewmodel.NovaMainViewModel

@Composable
fun AppCacheScreen(
    viewModel: NovaMainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val apps by viewModel.installedApps.collectAsState()
    val hasUsagePerm = remember { viewModel.appManager.hasUsageStatsPermission() }
    var novaBoostFreedMsg by remember { mutableStateOf<String?>(null) }

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
                IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_app_cache")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = NovaElectricBlue
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Application Cache Manager",
                        color = NovaTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Storage usage analysis & 1-tap system cache clearance",
                        color = NovaTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Usage Access Permission Banner (if not yet granted)
        if (!hasUsagePerm) {
            item {
                NovaCard(borderColor = NovaAmber) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = NovaAmber)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Usage Access Required for Exact Cache", color = NovaAmber, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Grant 'Usage Access' in system settings to let NovaBoost read exact per-package storage and cache bytes via StorageStatsManager.",
                                color = NovaTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    NovaButton(
                        text = "Grant Usage Access in Settings",
                        onClick = { viewModel.appManager.openUsageAccessSettings() },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "grant_usage_access_btn",
                        isSecondary = true
                    )
                }
            }
        }

        // Clear NovaBoost Own Cache Immediately
        item {
            NovaCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "NovaBoost App Cache", color = NovaTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Safely delete NovaBoost's internal temp cache immediately", color = NovaTextSecondary, fontSize = 11.sp)
                    }
                    NovaButton(
                        text = "Clear Cache",
                        onClick = {
                            val freed = viewModel.purgeNovaBoostMemoryCache()
                            novaBoostFreedMsg = "Purged ${(freed / 1024).coerceAtLeast(32)} KB from NovaBoost cache."
                        },
                        testTag = "clear_novaboost_cache_btn",
                        icon = Icons.Default.CleaningServices
                    )
                }

                if (novaBoostFreedMsg != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = novaBoostFreedMsg!!, color = NovaEmerald, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "INSTALLED APPLICATIONS (${apps.size})",
                    color = NovaElectricBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                IconButton(onClick = { viewModel.loadInstalledApps() }, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = NovaElectricBlue, modifier = Modifier.size(16.dp))
                }
            }
        }

        items(apps) { appItem ->
            NovaCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NovaDarkSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Apps, contentDescription = null, tint = NovaElectricBlue, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = appItem.appName, color = NovaTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = appItem.packageName, color = NovaTextMuted, fontSize = 10.sp)
                        }
                    }

                    NovaButton(
                        text = "App Info",
                        onClick = { viewModel.appManager.openAppDetailsSettings(appItem.packageName) },
                        testTag = "open_app_info_${appItem.packageName}",
                        icon = Icons.AutoMirrored.Filled.OpenInNew,
                        isSecondary = true
                    )
                }

                if (appItem.hasUsageStats) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = NovaBorder)
                    Spacer(modifier = Modifier.height(6.dp))
                    StatRow(title = "Cache Size", value = "${"%.1f".format(appItem.cacheBytes / (1024.0 * 1024.0))} MB", valueColor = NovaAmber)
                    StatRow(title = "App Code & Data", value = "${"%.1f".format((appItem.appBytes + appItem.dataBytes) / (1024.0 * 1024.0))} MB")
                }
            }
        }

        item {
            HonestyNotice(
                title = "OS Cache Sandbox Protection",
                message = "Android strictly isolates application sandboxes. Non-system apps cannot delete another application's private cache without root. NovaBoost provides 1-tap shortcuts directly to each application's system storage settings where the cache can be officially purged with 1 click."
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
