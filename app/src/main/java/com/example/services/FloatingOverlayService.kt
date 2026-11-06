package com.example.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.core.hardware.HardwareMonitor
import com.example.core.network.NetworkDiagnostics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FloatingOverlayService : Service() {

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var params: WindowManager.LayoutParams? = null

    private val serviceScope = CoroutineScope(Dispatchers.Main)
    private var updateJob: Job? = null

    private lateinit var hardwareMonitor: HardwareMonitor
    private lateinit var networkDiagnostics: NetworkDiagnostics

    private var isMinimized = false
    private var sessionSeconds = 0L

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        hardwareMonitor = HardwareMonitor(this)
        networkDiagnostics = NetworkDiagnostics(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            stopSelf()
            return
        }

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        initOverlayView()
        startMetricsLoop()
    }

    private fun initOverlayView() {
        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 60
            y = 200
        }

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 16, 24, 16)
            background = GradientDrawable().apply {
                setColor(0xEE090D16.toInt())
                cornerRadius = 24f
                setStroke(2, 0xFF00E5FF.toInt())
            }
        }

        // Header Row: Drag handle & Close
        val headerRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val titleTv = TextView(this).apply {
            text = "NOVABOOST HUD"
            setTextColor(0xFF00E5FF.toInt())
            textSize = 11f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val minBtn = TextView(this).apply {
            text = "– "
            setTextColor(0xFF94A3B8.toInt())
            textSize = 16f
            setPadding(12, 0, 12, 0)
            setOnClickListener {
                toggleMinimize()
            }
        }

        val closeBtn = TextView(this).apply {
            text = "✕"
            setTextColor(0xFFEF4444.toInt())
            textSize = 14f
            setPadding(8, 0, 8, 0)
            setOnClickListener {
                stopSelf()
            }
        }

        headerRow.addView(titleTv)
        headerRow.addView(minBtn)
        headerRow.addView(closeBtn)
        rootLayout.addView(headerRow)

        // Metrics Text container
        val metricsLayout = LinearLayout(this).apply {
            id = View.generateViewId()
            orientation = LinearLayout.VERTICAL
            setPadding(0, 10, 0, 0)
        }

        val ramTv = TextView(this).apply {
            id = View.generateViewId()
            text = "RAM: --%"
            setTextColor(0xFFF1F5F9.toInt())
            textSize = 12f
        }

        val tempTv = TextView(this).apply {
            id = View.generateViewId()
            text = "Temp: --°C"
            setTextColor(0xFFF1F5F9.toInt())
            textSize = 12f
        }

        val pingTv = TextView(this).apply {
            id = View.generateViewId()
            text = "Ping: -- ms"
            setTextColor(0xFFF1F5F9.toInt())
            textSize = 12f
        }

        val timeTv = TextView(this).apply {
            id = View.generateViewId()
            text = "Time: 00:00"
            setTextColor(0xFF94A3B8.toInt())
            textSize = 11f
        }

        metricsLayout.addView(ramTv)
        metricsLayout.addView(tempTv)
        metricsLayout.addView(pingTv)
        metricsLayout.addView(timeTv)
        rootLayout.addView(metricsLayout)

        // Drag to reposition
        rootLayout.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f

            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params?.x ?: 0
                        initialY = params?.y ?: 0
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params?.let { p ->
                            p.x = initialX + (event.rawX - initialTouchX).toInt()
                            p.y = initialY + (event.rawY - initialTouchY).toInt()
                            windowManager?.updateViewLayout(overlayView, p)
                        }
                        return true
                    }
                }
                return false
            }
        })

        overlayView = rootLayout
        try {
            windowManager?.addView(overlayView, params)
        } catch (_: Exception) {}
    }

    private fun toggleMinimize() {
        isMinimized = !isMinimized
        val root = overlayView as? LinearLayout ?: return
        if (root.childCount >= 2) {
            root.getChildAt(1).visibility = if (isMinimized) View.GONE else View.VISIBLE
        }
    }

    private fun startMetricsLoop() {
        updateJob = serviceScope.launch {
            while (isActive) {
                sessionSeconds += 2
                val (ram, bat, ping) = kotlinx.coroutines.withContext(Dispatchers.IO) {
                    Triple(
                        hardwareMonitor.getRamInfo(),
                        hardwareMonitor.getBatteryInfo(),
                        networkDiagnostics.measureSocketPing()
                    )
                }

                val root = overlayView as? LinearLayout
                if (root != null && root.childCount >= 2) {
                    val metricsBox = root.getChildAt(1) as? LinearLayout
                    if (metricsBox != null && metricsBox.childCount >= 4) {
                        (metricsBox.getChildAt(0) as? TextView)?.text = "RAM: ${ram.usagePercentage}% (${ram.availableBytes / (1024 * 1024)}MB free)"
                        val tempColor = if (bat.isOverheating) 0xFFEF4444.toInt() else 0xFFF1F5F9.toInt()
                        (metricsBox.getChildAt(1) as? TextView)?.apply {
                            text = "Temp: ${"%.1f".format(bat.temperatureCelsius)}°C (${bat.thermalStatus})"
                            setTextColor(tempColor)
                        }
                        val pingStr = if (ping.isSuccess) "${ping.latencyMs} ms" else "Timeout"
                        (metricsBox.getChildAt(2) as? TextView)?.text = "Ping: $pingStr"

                        val mins = sessionSeconds / 60
                        val secs = sessionSeconds % 60
                        (metricsBox.getChildAt(3) as? TextView)?.text = "Time: %02d:%02d".format(mins, secs)
                    }
                }
                delay(2000L)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateJob?.cancel()
        overlayView?.let {
            try {
                windowManager?.removeView(it)
            } catch (_: Exception) {}
        }
        overlayView = null
    }
}
