package com.example.ui.screens.more

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeonActionButton
import com.example.ui.components.NeonCard
import com.example.ui.components.NeonTopBar
import com.example.ui.theme.NovaBgDark
import com.example.ui.theme.NovaCardBg
import com.example.ui.theme.NovaCardBorder
import com.example.ui.theme.NovaNeonBlue
import com.example.ui.theme.NovaNeonCyan
import com.example.ui.theme.NovaTextGray
import com.example.ui.theme.NovaTextWhite
import com.example.ui.viewmodel.NovaMainViewModel

@Composable
fun DeviceScreen(
    viewModel: NovaMainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val ram by viewModel.ramInfo.collectAsState()
    val storage by viewModel.storageInfo.collectAsState()
    val cpu by viewModel.cpuInfo.collectAsState()
    val gpu by viewModel.gpuDiagnostics.collectAsState()
    val display by viewModel.displayInfo.collectAsState()

    var showDetailsModal by remember { mutableStateOf(false) }

    val modelName = remember {
        val brand = Build.MANUFACTURER.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        val model = Build.MODEL
        if (model.startsWith(brand, ignoreCase = true)) model else "$brand $model"
    }

    val androidVer = remember {
        "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }

    val ramLabel = remember(ram) {
        com.example.core.util.FormatUtils.formatBytes(ram.totalBytes)
    }

    val storageLabel = remember(storage) {
        com.example.core.util.FormatUtils.formatBytes(storage.totalBytes)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBgDark)
            .padding(horizontal = 18.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        NeonTopBar(
            icon = Icons.Default.PhoneAndroid,
            title = "Device Information",
            subtitle = "Know Your Device."
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Device Graphic Card
        NeonCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stylized Phone Illustration Box
                Box(
                    modifier = Modifier
                        .size(width = 54.dp, height = 76.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(NovaNeonBlue.copy(alpha = 0.2f))
                        .border(1.5.dp, NovaNeonCyan, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        tint = NovaNeonCyan,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = modelName,
                        color = NovaTextWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = androidVer,
                        color = NovaTextGray,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Specs Table Card (matching mockup list)
        NeonCard {
            SpecRowItem(icon = Icons.Default.Memory, title = "RAM", value = ramLabel)
            Spacer(modifier = Modifier.height(10.dp))
            SpecRowItem(icon = Icons.Default.Storage, title = "Storage", value = storageLabel)
            Spacer(modifier = Modifier.height(10.dp))
            SpecRowItem(icon = Icons.Default.Speed, title = "CPU", value = if (cpu.processorCount >= 8) "Octa-core" else "${cpu.processorCount}-core")
            Spacer(modifier = Modifier.height(10.dp))
            SpecRowItem(icon = Icons.Default.Tv, title = "GPU", value = gpu.renderer.take(24).ifBlank { "System GPU" })
            Spacer(modifier = Modifier.height(10.dp))
            SpecRowItem(icon = Icons.Default.PhoneAndroid, title = "Display", value = display.resolution.ifBlank { "Default Display" })
            Spacer(modifier = Modifier.height(10.dp))
            SpecRowItem(icon = Icons.Default.Refresh, title = "Refresh", value = "${display.refreshRateHz.toInt()} Hz")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // System Details Button
        NeonActionButton(
            text = if (showDetailsModal) "Hide System Details" else "System Details",
            onClick = { showDetailsModal = !showDetailsModal },
            testTag = "system_details_button"
        )

        if (showDetailsModal) {
            Spacer(modifier = Modifier.height(14.dp))
            NeonCard {
                Text(text = "Security Patch: ${if (Build.VERSION.SDK_INT >= 23) Build.VERSION.SECURITY_PATCH else "Recent"}", color = NovaTextGray, fontSize = 11.sp)
                Text(text = "ABI Architecture: ${cpu.architecture}", color = NovaTextGray, fontSize = 11.sp)
                Text(text = "Board Hardware: ${Build.BOARD}", color = NovaTextGray, fontSize = 11.sp)
                Text(text = "Vulkan Support: ${gpu.vulkanLevel}", color = NovaTextGray, fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SpecRowItem(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = NovaNeonCyan, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = title, color = NovaTextGray, fontSize = 13.sp)
        }
        Text(text = value, color = NovaTextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}
