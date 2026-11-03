package com.example.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Socket
import java.net.URL

data class PingResult(
    val targetHost: String,
    val latencyMs: Long,
    val isSuccess: Boolean,
    val errorMessage: String? = null
)

data class SpeedTestProgress(
    val currentMbps: Float,
    val bytesDownloaded: Long,
    val percentComplete: Int,
    val isFinished: Boolean,
    val errorMessage: String? = null
)

data class NetworkDetails(
    val connectionType: String,
    val isConnected: Boolean,
    val isInternetValidated: Boolean,
    val isVpnActive: Boolean,
    val wifiLinkSpeedMbps: Int,
    val wifiFrequencyMhz: Int,
    val wifiRssiDbm: Int,
    val downstreamKbps: Int,
    val upstreamKbps: Int
)

class NetworkDiagnostics(private val context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    private val wifiManager = try {
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
    } catch (_: Exception) {
        null
    }

    fun getNetworkDetails(): NetworkDetails {
        val cm = connectivityManager ?: return NetworkDetails(
            connectionType = "No Network Connection",
            isConnected = false,
            isInternetValidated = false,
            isVpnActive = false,
            wifiLinkSpeedMbps = 0,
            wifiFrequencyMhz = 0,
            wifiRssiDbm = -100,
            downstreamKbps = 0,
            upstreamKbps = 0
        )
        val active = cm.activeNetwork
        val caps = cm.getNetworkCapabilities(active)

        if (caps == null) {
            return NetworkDetails(
                connectionType = "No Network Connection",
                isConnected = false,
                isInternetValidated = false,
                isVpnActive = false,
                wifiLinkSpeedMbps = 0,
                wifiFrequencyMhz = 0,
                wifiRssiDbm = -100,
                downstreamKbps = 0,
                upstreamKbps = 0
            )
        }

        val isVpn = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        val isWifi = caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        val isCellular = caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)

        val connType = when {
            isWifi -> "Wi-Fi Network"
            isCellular -> "Cellular Mobile Data"
            isVpn -> "VPN Tunnel"
            else -> "Active Connection"
        }

        var linkSpeed = 0
        var freq = 0
        var rssi = -100

        if (isWifi && wifiManager != null) {
            try {
                @Suppress("DEPRECATION")
                val wifiInfo = wifiManager.connectionInfo
                if (wifiInfo != null) {
                    linkSpeed = wifiInfo.linkSpeed
                    freq = wifiInfo.frequency
                    rssi = wifiInfo.rssi
                }
            } catch (_: Exception) {}
        }

        return NetworkDetails(
            connectionType = connType,
            isConnected = true,
            isInternetValidated = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET),
            isVpnActive = isVpn,
            wifiLinkSpeedMbps = linkSpeed,
            wifiFrequencyMhz = freq,
            wifiRssiDbm = rssi,
            downstreamKbps = caps.linkDownstreamBandwidthKbps,
            upstreamKbps = caps.linkUpstreamBandwidthKbps
        )
    }

    suspend fun measureSocketPing(host: String = "1.1.1.1", port: Int = 53, timeoutMs: Int = 3000): PingResult = withContext(Dispatchers.IO) {
        val socket = Socket()
        val start = System.currentTimeMillis()
        try {
            socket.connect(InetSocketAddress(host, port), timeoutMs)
            val latency = System.currentTimeMillis() - start
            socket.close()
            PingResult(
                targetHost = "$host:$port",
                latencyMs = latency,
                isSuccess = true
            )
        } catch (e: Exception) {
            try { socket.close() } catch (_: Exception) {}
            PingResult(
                targetHost = "$host:$port",
                latencyMs = -1L,
                isSuccess = false,
                errorMessage = e.localizedMessage ?: "Connection timed out"
            )
        }
    }

    suspend fun runDownloadSpeedTest(
        testUrl: String = "https://speed.cloudflare.com/__down?bytes=1500000",
        onProgress: (SpeedTestProgress) -> Unit
    ) = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        try {
            val url = URL(testUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 7000
            connection.requestMethod = "GET"
            connection.setRequestProperty("User-Agent", "NovaBoost-Android-Diagnostic/1.0")

            val startTime = System.currentTimeMillis()
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                onProgress(
                    SpeedTestProgress(
                        currentMbps = 0f,
                        bytesDownloaded = 0L,
                        percentComplete = 100,
                        isFinished = true,
                        errorMessage = "Endpoint returned HTTP $responseCode"
                    )
                )
                return@withContext
            }

            val totalExpected = 1500000L
            inputStream = connection.inputStream
            val buffer = ByteArray(16384)
            var totalRead = 0L
            var read: Int

            while (inputStream.read(buffer).also { read = it } != -1) {
                totalRead += read
                val elapsedSec = (System.currentTimeMillis() - startTime) / 1000.0f
                if (elapsedSec > 0.05f) {
                    val bitsPerSec = (totalRead * 8) / elapsedSec
                    val mbps = bitsPerSec / 1_000_000.0f
                    val pct = ((totalRead.toDouble() / totalExpected) * 100).toInt().coerceIn(0, 99)
                    onProgress(
                        SpeedTestProgress(
                            currentMbps = mbps,
                            bytesDownloaded = totalRead,
                            percentComplete = pct,
                            isFinished = false
                        )
                    )
                }
            }

            val totalElapsedSec = ((System.currentTimeMillis() - startTime) / 1000.0f).coerceAtLeast(0.1f)
            val finalMbps = ((totalRead * 8) / totalElapsedSec) / 1_000_000.0f

            onProgress(
                SpeedTestProgress(
                    currentMbps = finalMbps,
                    bytesDownloaded = totalRead,
                    percentComplete = 100,
                    isFinished = true
                )
            )
        } catch (e: Exception) {
            onProgress(
                SpeedTestProgress(
                    currentMbps = 0f,
                    bytesDownloaded = 0L,
                    percentComplete = 100,
                    isFinished = true,
                    errorMessage = e.localizedMessage ?: "Network test interrupted"
                )
            )
        } finally {
            try { inputStream?.close() } catch (_: Exception) {}
            connection?.disconnect()
        }
    }
}
