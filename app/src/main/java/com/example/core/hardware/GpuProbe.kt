package com.example.core.hardware

import android.content.Context
import android.content.pm.PackageManager
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.opengl.EGLDisplay
import android.opengl.EGLSurface
import android.opengl.GLES20
import android.os.Build

data class GpuDiagnostics(
    val renderer: String,
    val vendor: String,
    val openGlVersion: String,
    val maxTextureSize: Int,
    val vulkanSupported: Boolean,
    val vulkanLevel: String,
    val extensionCount: Int,
    val topExtensions: List<String>,
    val astcSupported: Boolean,
    val etc2Supported: Boolean
)

object GpuProbe {

    private var cachedGpuInfo: GpuDiagnostics? = null

    fun probeGpu(context: Context): GpuDiagnostics {
        cachedGpuInfo?.let { return it }

        var renderer = "Generic Android GPU"
        var vendor = "System Graphics"
        var version = "OpenGL ES 2.0+"
        var maxTexture = 2048
        var extensionCount = 0
        val topExts = mutableListOf<String>()
        var astc = false
        var etc2 = false

        try {
            val dpy: EGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
            if (dpy != EGL14.EGL_NO_DISPLAY) {
                val major = IntArray(1)
                val minor = IntArray(1)
                if (EGL14.eglInitialize(dpy, major, 0, minor, 0)) {
                    val configAttribs = intArrayOf(
                        EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                        EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT,
                        EGL14.EGL_NONE
                    )
                    val configs = arrayOfNulls<EGLConfig>(1)
                    val numConfigs = IntArray(1)
                    if (EGL14.eglChooseConfig(dpy, configAttribs, 0, configs, 0, 1, numConfigs, 0) && numConfigs[0] > 0 && configs[0] != null) {
                        val pbufferAttribs = intArrayOf(
                            EGL14.EGL_WIDTH, 1,
                            EGL14.EGL_HEIGHT, 1,
                            EGL14.EGL_NONE
                        )
                        val surface: EGLSurface = EGL14.eglCreatePbufferSurface(dpy, configs[0], pbufferAttribs, 0)
                        val contextAttribs = intArrayOf(
                            EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                            EGL14.EGL_NONE
                        )
                        val ctx: EGLContext = EGL14.eglCreateContext(dpy, configs[0], EGL14.EGL_NO_CONTEXT, contextAttribs, 0)

                        if (EGL14.eglMakeCurrent(dpy, surface, surface, ctx)) {
                            renderer = GLES20.glGetString(GLES20.GL_RENDERER) ?: renderer
                            vendor = GLES20.glGetString(GLES20.GL_VENDOR) ?: vendor
                            version = GLES20.glGetString(GLES20.GL_VERSION) ?: version

                            val maxTex = IntArray(1)
                            GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, maxTex, 0)
                            if (maxTex[0] > 0) maxTexture = maxTex[0]

                            val exts = GLES20.glGetString(GLES20.GL_EXTENSIONS)
                            if (!exts.isNullOrBlank()) {
                                val allExt = exts.split("\\s+".toRegex())
                                extensionCount = allExt.size
                                astc = exts.contains("GL_KHR_texture_compression_astc", ignoreCase = true)
                                etc2 = exts.contains("ETC2", ignoreCase = true) || version.contains("3.")
                                topExts.addAll(allExt.take(6))
                            }
                        }
                        EGL14.eglMakeCurrent(dpy, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
                        EGL14.eglDestroySurface(dpy, surface)
                        EGL14.eglDestroyContext(dpy, ctx)
                    }
                    EGL14.eglTerminate(dpy)
                }
            }
        } catch (_: Throwable) {
            // Safe fallback if offscreen context creation has platform restrictions
        }

        // Check Vulkan Hardware Support
        var hasVulkan = false
        var vulkanLevelStr = "Not Supported"
        try {
            val pm = context.packageManager
            hasVulkan = pm.hasSystemFeature(PackageManager.FEATURE_VULKAN_HARDWARE_LEVEL) ||
                    pm.hasSystemFeature(PackageManager.FEATURE_VULKAN_HARDWARE_VERSION)

            if (hasVulkan) {
                vulkanLevelStr = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (pm.hasSystemFeature("android.hardware.vulkan.level", 1)) "Vulkan Level 1.1+"
                    else "Vulkan Level 1.0 (Supported)"
                } else {
                    "Supported"
                }
            }
        } catch (_: Throwable) {}

        val result = GpuDiagnostics(
            renderer = renderer,
            vendor = vendor,
            openGlVersion = version,
            maxTextureSize = maxTexture,
            vulkanSupported = hasVulkan,
            vulkanLevel = vulkanLevelStr,
            extensionCount = extensionCount,
            topExtensions = topExts,
            astcSupported = astc,
            etc2Supported = etc2
        )
        cachedGpuInfo = result
        return result
    }
}
