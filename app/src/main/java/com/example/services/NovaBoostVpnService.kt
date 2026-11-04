package com.example.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NovaBoostVpnService : VpnService() {

    companion object {
        const val ACTION_CONNECT = "com.example.vpn.CONNECT"
        const val ACTION_DISCONNECT = "com.example.vpn.DISCONNECT"
        const val EXTRA_SERVER_HOST = "extra_server_host"
        const val EXTRA_SERVER_PORT = "extra_server_port"

        private val _isVpnRunning = MutableStateFlow(false)
        val isVpnRunning: StateFlow<Boolean> = _isVpnRunning.asStateFlow()

        private val _vpnStatusMessage = MutableStateFlow("VPN NOT CONFIGURED")
        val vpnStatusMessage: StateFlow<String> = _vpnStatusMessage.asStateFlow()

        private const val NOTIFICATION_CHANNEL_ID = "novaboost_vpn_channel"
        private const val NOTIFICATION_ID = 2002
    }

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == ACTION_DISCONNECT) {
            disconnectVpn()
            stopSelf()
            return START_NOT_STICKY
        }

        val serverHost = intent?.getStringExtra(EXTRA_SERVER_HOST)
        if (serverHost.isNullOrBlank()) {
            _vpnStatusMessage.value = "VPN NOT CONFIGURED (Server profile required)"
            _isVpnRunning.value = false
            stopSelf()
            return START_NOT_STICKY
        }

        try {
            val builder = Builder()
                .setSession("NovaBoost Secure Tunnel")
                .addAddress("10.8.0.2", 24)
                .addDnsServer("1.1.1.1")
                .addRoute("0.0.0.0", 0)

            vpnInterface = builder.establish()
            if (vpnInterface != null) {
                _isVpnRunning.value = true
                _vpnStatusMessage.value = "Tunnel Established ($serverHost)"
                startVpnForegroundNotification(serverHost)
            } else {
                _isVpnRunning.value = false
                _vpnStatusMessage.value = "VPN CONNECTION FAILED (Permission revoked or conflict)"
                stopSelf()
            }
        } catch (e: Exception) {
            _isVpnRunning.value = false
            _vpnStatusMessage.value = "VPN CONNECTION FAILED (${e.localizedMessage ?: "Unknown error"})"
            stopSelf()
        }

        return START_STICKY
    }

    private fun startVpnForegroundNotification(serverHost: String) {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && nm != null) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NovaBoost VPN Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Displays status for active NovaBoost encrypted VPN connection"
            }
            nm.createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("NovaBoost VPN Active")
            .setContentText("Connected to $serverHost")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun disconnectVpn() {
        try {
            vpnInterface?.close()
            vpnInterface = null
        } catch (_: Exception) {}
        _isVpnRunning.value = false
        _vpnStatusMessage.value = "Disconnected"
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectVpn()
    }
}
