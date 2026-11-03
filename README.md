# NovaBoost — Ultimate Android Optimizer

NovaBoost is a production-grade, zero-mock Android performance suite built with **Kotlin** and **Jetpack Compose (Material 3)**.

Every metric, scan, and optimization across NovaBoost binds directly to genuine Android OS APIs and Linux kernel interfaces with zero hardcoded or fake mock data.

---

## ⚡ Key Features

- **🛡️ Real Dynamic Health Score**: Computed live from real RAM headroom, internal storage capacity, battery temperature, and system thermal state.
- **🧹 Storage & Cache Cleaner**: Scans local cache, app temporary files, duplicate files (via SHA-256 chunk hashing), empty directories, and obsolete APKs without simulated values.
- **🧠 Virtual RAM & Swap Monitor**: Reads `/proc/meminfo` and Linux `zRAM` statistics; safely trims background process caches via `ActivityManager`.
- **⚙️ CPU & GPU Telemetry**: Live multi-core CPU usage, scaling clock frequencies (`/sys/devices/system/cpu`), OpenGL ES / Vulkan GPU vendor & renderer inspection.
- **🎮 Gaming Center & Floating HUD**: Real gaming session tracking, DND profile triggers, and movable floating HUD overlay displaying live RAM, temp, and network latency.
- **🔋 Battery & Thermal Guard**: Direct reading of `BatteryManager` (voltage, Celsius temperature, charging status, health) and `PowerManager` thermal status.
- **🌐 Network & VPN Diagnostics**: Live ICMP/Socket latency ping probing, network interface transport detection, and link bandwidth.
- **📱 Universal Android Compatibility**: Built for Android 8.0+ (API 24+) up to Android 15/16 with graceful fallbacks for SELinux restricted OEM ROMs (Samsung One UI, Xiaomi HyperOS, Oppo/Realme ColorOS, Google Pixel).

---

## 🚀 Automated GitHub Actions APK Builds

This repository includes a continuous integration workflow at `.github/workflows/build-apk.yml`.

### How it works:
1. **Triggers**: Runs automatically on every `push` to `main`/`master`, on pull requests, or manually via **Workflow Dispatch** (the "Run workflow" button in GitHub Actions tab).
2. **Build Matrix**: Uses JDK 21 on `ubuntu-latest`, configures the Android SDK, restores keystore credentials, runs tests, and assembles the APK.
3. **Artifacts**: Uploads `NovaBoost-debug-apk` directly to the GitHub Actions Run page with SHA-256 integrity checksums.

### Downloading the APK:
1. Push your repository to GitHub.
2. Navigate to the **Actions** tab on your GitHub repository.
3. Select the latest **Build NovaBoost Android APK** workflow run.
4. Scroll to the **Artifacts** section at the bottom.
5. Download `NovaBoost-debug-apk.zip`, extract `NovaBoost-debug.apk`, and install it directly on any Android device.
