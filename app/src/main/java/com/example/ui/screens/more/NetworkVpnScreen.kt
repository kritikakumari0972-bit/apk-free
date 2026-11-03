package com.example.ui.screens.more

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VpnKey
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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
import com.example.ui.theme.NovaNeonGreen
import com.example.ui.theme.NovaTextGray
import com.example.ui.theme.NovaTextWhite
import com.example.ui.viewmodel.NovaMainViewModel

@Composable
fun NetworkVpnScreen(
    viewModel: NovaMainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val details by viewModel.networkDetails.collectAsState()
    val ping by viewModel.pingResult.collectAsState()
    val speedProgress by viewModel.speedTestProgress.collectAsState()
    val isTestingSpeed by viewModel.isTestingSpeed.collectAsState()

    var vpnConnectedState by remember { mutableStateOf(false) }
    var activeToolInfo by remember { mutableStateOf<String?>(null) }

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
            icon = Icons.Default.Language,
            title = "Network & VPN",
            subtitle = "Connect. Browse. Play."
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Internet Status Card
        NeonCard {
            // Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NovaNeonCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = NovaNeonCyan, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Internet Status", color = NovaTextGray, fontSize = 11.sp)
                        Text(
                            text = if (details.isConnected) "Connected ✔" else "Offline",
                            color = if (details.isConnected) NovaNeonGreen else Color(0xFFFF3D71),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.SignalCellularAlt,
                    contentDescription = null,
                    tint = NovaNeonGreen,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Download & Upload Speeds
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Download", color = NovaTextGray, fontSize = 11.sp)
                    val dl = when {
                        speedProgress != null && speedProgress!!.currentMbps > 0 -> "%.1f Mbps".format(speedProgress!!.currentMbps)
                        details.downstreamKbps > 0 -> "%.1f Mbps".format(details.downstreamKbps / 1000.0)
                        details.wifiLinkSpeedMbps > 0 -> "${details.wifiLinkSpeedMbps} Mbps"
                        details.isConnected -> "Active"
                        else -> "Offline"
                    }
                    Text(text = dl, color = NovaTextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Upload", color = NovaTextGray, fontSize = 11.sp)
                    val ul = when {
                        details.upstreamKbps > 0 -> "%.1f Mbps".format(details.upstreamKbps / 1000.0)
                        details.wifiLinkSpeedMbps > 0 -> "${details.wifiLinkSpeedMbps} Mbps"
                        details.isConnected -> "Active"
                        else -> "Offline"
                    }
                    Text(text = ul, color = NovaTextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Ping Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(NovaNeonGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Adjust, contentDescription = null, tint = NovaNeonGreen, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "Ping", color = NovaTextGray, fontSize = 10.sp)
                    val p = if (ping != null && ping!!.isSuccess) "${ping!!.latencyMs} ms" else if (details.isConnected) "Probing DNS..." else "Offline"
                    Text(text = p, color = NovaTextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // VPN Card
        NeonCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NovaNeonBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = NovaNeonBlue, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "VPN", color = NovaTextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (vpnConnectedState) "Connected (Encrypted)" else "Not Connected",
                            color = if (vpnConnectedState) NovaNeonGreen else NovaTextGray,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(NovaNeonBlue)
                        .clickable { vpnConnectedState = !vpnConnectedState }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = if (vpnConnectedState) "Disconnect" else "Tap to Connect",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Network Tools Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "Network Tools", color = NovaTextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3 Tools List
        NetworkToolRowItem(
            icon = Icons.Default.Speed,
            title = "Speed Test",
            onClick = {
                viewModel.runSpeedTest()
                activeToolInfo = "Throughput test running..."
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        NetworkToolRowItem(
            icon = Icons.Default.Dns,
            title = "DNS Check",
            onClick = {
                viewModel.runNetworkPing()
                activeToolInfo = "DNS latency measured."
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        NetworkToolRowItem(
            icon = Icons.Default.NetworkCheck,
            title = "Network Monitor",
            onClick = {
                activeToolInfo = "Monitoring Wi-Fi/Cellular throughput: ${details.wifiLinkSpeedMbps} Mbps link."
            }
        )

        if (activeToolInfo != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = activeToolInfo!!, color = NovaNeonCyan, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun NetworkToolRowItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NovaCardBg)
            .border(1.dp, NovaCardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 13.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(NovaNeonBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = NovaNeonBlue, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = title, color = NovaTextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = NovaTextGray,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}
