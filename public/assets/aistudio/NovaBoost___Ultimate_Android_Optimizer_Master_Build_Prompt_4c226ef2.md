# PROJECT: NOVABOOST
## COMPLETE MASTER DEVELOPMENT SPECIFICATION

You are an expert Android application development team consisting of senior Kotlin engineers, Android system engineers, performance optimization specialists, UI/UX designers, storage-management developers, networking engineers and software testers.

Your assignment is to DESIGN, DEVELOP, IMPLEMENT AND TEST a genuine native Android application named:

# NovaBoost
### Ultimate Android Optimizer • Virtual Memory Manager • Gaming Center • Smart Cleaner

This is a real Android software development project, NOT a UI demonstration, website, dashboard mockup, visual prototype or collection of fake buttons.

The application must use actual Android APIs and perform its advertised functions wherever Android permits.

---

# 1. ABSOLUTE DEVELOPMENT RULES

These instructions have the highest priority.

1. Build an actual native Android application.
2. Use Kotlin and Jetpack Compose.
3. Use Android SDK APIs wherever available.
4. Implement real functionality for every feature that can legally and technically work.
5. Never simulate system operations.
6. Never display fabricated hardware statistics.
7. Never pretend that a file is functioning as virtual RAM when it is not activated as system swap.
8. Never display fake CPU or GPU performance improvements.
9. Never create fake VPN connections.
10. Never display invented network speeds, FPS, temperatures, memory values or battery statistics.
11. Never add buttons that do nothing.
12. Never create decorative features that have no actual implementation.
13. Never introduce unrelated features.
14. Never add unnecessary AI assistants, chatbots, social media, accounts, advertisements or subscriptions.
15. Never request unnecessary permissions.
16. Never delete a user's files without explicit authorization.
17. Never execute dangerous or unsupported system commands.
18. Never require root for the Standard Edition.
19. Never use a permanent background service without a genuine reason.
20. Test every implemented module and correct its errors.

If a requested capability is impossible under standard Android permissions, implement the closest legitimate alternative and clearly explain the limitation inside that feature.

Do not silently replace an impossible feature with a fake implementation.

Do not respond with a development plan alone. Start creating the actual application and its source files.

---

# 2. APPLICATION IDENTITY

Application name: NovaBoost

Application category:
- Android phone optimization
- Storage management
- Memory diagnostics
- Hardware monitoring
- Gaming performance monitoring
- Network diagnostics

Design direction:

Create a premium, modern, professional Android utility application.

Visual appearance:
- Deep dark graphite background.
- Midnight navy surfaces.
- Electric blue primary accents.
- Purple secondary accents.
- Subtle cyan highlights.
- Carefully controlled gradients.
- Smooth transitions.
- Premium cards.
- Clean typography.
- Rounded but professional components.
- Consistent spacing.
- Responsive layouts.

Avoid:
- Excessive neon.
- Excessive glow.
- Unnecessary animations.
- Cluttered interfaces.
- Too many cards on one screen.
- Oversized headings.
- Fake 3D controls.
- Excessive decorative icons.

The design should feel like a premium hardware optimization utility.

Create a consistent design system with reusable:
- Buttons.
- Cards.
- Progress indicators.
- Status indicators.
- Charts.
- Dialogs.
- Permission screens.
- Empty states.
- Error states.
- Loading states.

Use Material 3 and custom Compose components.

---

# 3. MAIN APPLICATION STRUCTURE

Create exactly these primary navigation sections:

1. Home
2. Cleaner
3. Memory
4. Gaming
5. More

The More section contains:
- CPU & GPU monitoring.
- Battery.
- Network & VPN.
- Device information.
- Settings.

Do not invent additional navigation sections.

The application must have a proper navigation architecture with working navigation, persistent state and consistent screen layouts.

---

# 4. HOME DASHBOARD

Build a real-time device health dashboard.

Display actual available information:

RAM:
- Total physical RAM.
- Currently available RAM.
- Used RAM.
- Memory usage percentage.
- Swap total, if readable.
- Swap available, if readable.
- zRAM information, if accessible.

Storage:
- Total storage.
- Available storage.
- Used storage.
- Storage percentage.

CPU:
- Processor information.
- Available processor cores.
- CPU utilization, where measurable.
- CPU frequency, if accessible.

GPU:
- Actual GPU renderer.
- GPU vendor.
- Graphics API information.
- Graphics capabilities.

Battery:
- Battery level.
- Charging status.
- Temperature.
- Health status reported by Android.

Display:
- Screen resolution.
- Refresh rate, where available.

Network:
- Current connection type.
- Connection status.
- Validated internet connectivity.

Every statistic must come from a real device source.

Include:
- Refresh button.
- Automatic refresh at reasonable intervals.
- Last-updated timestamp.
- Permission status.
- Unavailable status when a metric cannot be retrieved.

Create a device health summary using transparent calculations. Do not invent a mysterious performance score.

---

# 5. VIRTUAL RAM ENGINE

This is a major module.

Develop a genuine memory-management and virtual-memory diagnostic system.

Required features:

A. Physical RAM monitor
- Read actual system memory statistics.
- Display available and total memory.
- Calculate used memory.
- Show memory usage trends.
- Refresh automatically.

B. zRAM monitor
- Detect zRAM information where the device exposes it.
- Read compressed-memory statistics where accessible.
- Display actual values.
- Display unavailable when restricted.

C. Storage SWAP compatibility checker
- Detect Android version.
- Detect available kernel and system capabilities.
- Check whether system swap information is accessible.
- Check existing active swap information where permitted.
- Report whether actual storage-backed swap activation is supported.

D. SWAP file manager

Important technical requirement:

Creating a .swap file does NOT activate Linux system swap.

A normal non-root Android application cannot independently invoke privileged kernel swap activation.

Therefore:

- Never claim a created file is active swap.
- Never claim that an ordinary file increases system RAM.
- Never create enormous unusable swap files.
- Never waste storage by creating fake swap files.
- Never execute root-only swapon commands.
- Never attempt privilege escalation.

If genuine swap activation is unavailable, provide an honest compatibility report.

E. Memory optimization

Implement safe, genuine functions:
- Memory diagnostics.
- Memory-pressure monitoring.
- Application memory information where publicly available.
- NovaBoost's own memory usage monitoring.
- Safe cleanup of NovaBoost's own temporary cache.
- Recommendations based on actual measurements.

Do not kill unrelated applications to manufacture a larger free-RAM number.

---

# 6. ADVANCED CLEANER ENGINE

Build a working storage-cleaning system.

The cleaner must analyze files before deleting anything.

Supported scanning categories:

1. Temporary files.
2. Duplicate files.
3. Large files.
4. Empty folders.
5. Old downloads.
6. Thumbnail files where accessible.
7. APK installation files stored in user-accessible folders.
8. User-selected cache and temporary directories.
9. Old unnecessary documents.
10. Other user-selected removable files.

For every category, show:
- Number of detected files.
- Total size.
- File names where appropriate.
- File locations.
- Selection controls.
- Selected size.
- Scan status.

Cleaning process:

1. Scan.
2. Analyze.
3. Display results.
4. Allow individual selection.
5. Ask for confirmation.
6. Delete selected files using supported Android document APIs.
7. Verify deletion.
8. Refresh storage statistics.
9. Display the actual recovered size.

Important storage restrictions:

Respect Android scoped-storage rules.

Use:
- Storage Access Framework.
- MediaStore.
- DocumentFile where appropriate.
- StorageStatsManager where accessible.

Do not access other applications' private directories.

Do not delete application databases or important Android files.

Do not delete personal photos, videos or documents automatically.

Never request MANAGE_EXTERNAL_STORAGE simply to bypass normal permissions.

---

# 7. APPLICATION CACHE MANAGER

Create a separate Application Cache Manager.

Capabilities:

- Analyze application storage usage where permission allows.
- Display installed application names.
- Show available storage statistics.
- Show application cache sizes where accessible.
- Sort applications by reported storage consumption.
- Open the selected application's Android App Info screen.
- Direct the user to the appropriate system storage-management interface.
- Clear NovaBoost's own cache directly.

Important:

Standard Android applications cannot freely delete every other application's private cache.

Use genuine Android-supported mechanisms.

If direct cache deletion is unavailable:
- Display the real limitation.
- Provide a button that opens the relevant system settings.
- Never report the cache as deleted unless Android confirms it.

---

# 8. CPU ENGINE

Create a dedicated CPU monitoring module.

Show:

- Processor hardware information.
- Number of logical processors available.
- CPU architecture.
- Supported ABIs.
- CPU utilization when reliably measurable.
- CPU frequency where accessible.
- Core information where available.
- Temperature information exposed by Android.
- Historical monitoring data.

Performance tools:

- CPU usage monitor.
- CPU workload dashboard.
- Background performance analysis.
- Thermal warnings.
- CPU monitoring history.

Do not claim to:
- Add processor cores.
- Convert storage into CPU power.
- Overclock the CPU.
- Change protected CPU governors.
- Force unsupported CPU frequencies.
- Modify another application's processor scheduling without permission.

Performance profiles may control only legitimate settings that Android exposes to this app.

---

# 9. GPU ENGINE

Create a separate GPU diagnostics module.

Display:

- GPU renderer.
- GPU vendor.
- OpenGL ES version.
- Supported graphics information.
- Vulkan support where reliably detectable.
- Device graphics capabilities.
- GPU-related information exposed by Android.

GPU tools:

- Graphics diagnostics.
- Rendering capability information.
- Gaming graphics profiles.
- Graphics compatibility checker.
- Graphics monitoring where accessible.

Do not fabricate GPU utilization.

Do not claim to increase physical GPU cores or VRAM.

Do not implement fake GPU overclocking.

Do not clear graphics-driver data or protected system caches.

Clearly distinguish GPU information from actual real-time GPU utilization.

---

# 10. SEPARATE GAMING CENTER

This is one of NovaBoost's most important features.

Create a dedicated Gaming Center, visually and functionally separate from the ordinary phone-cleaning interface.

The Gaming Center must have its own dashboard.

Main sections:

A. My Games

- Discover launchable installed games within Android package-visibility restrictions.
- Let users manually add supported games.
- Display game names and application icons.
- Store custom profiles.
- Launch selected games.
- Remove games from the NovaBoost launcher without uninstalling them.

B. Gaming Session

When a user launches a game through NovaBoost, provide an optional gaming session.

Track:
- Session duration.
- Selected game.
- Available RAM.
- Total RAM.
- CPU monitoring information.
- Battery level.
- Battery temperature.
- Network connection.
- Latency measurements where configured.
- Thermal warnings.

Save actual session summaries locally.

C. Lag Monitoring

Create a diagnostic engine that monitors:

- Memory pressure.
- CPU load.
- Thermal conditions.
- Network connectivity.
- Network latency.
- Battery temperature.
- Background resource usage where available.

Show possible causes of poor gaming performance.

Do not promise that a monitoring screen itself increases FPS.

D. Game Profiles

Provide individual profiles:

- Balanced.
- Battery-conscious.
- Performance monitoring.

Each profile should only apply settings genuinely available to the application.

Do not silently force unsupported system-level settings.

E. Gaming Overlay

Provide an optional floating monitoring overlay.

The user must manually grant overlay permission.

Display only metrics that can genuinely be collected.

Possible overlay information:
- RAM.
- CPU information.
- Battery temperature.
- Network latency.
- Session duration.

Allow:
- Show/hide.
- Drag and reposition.
- Minimize.
- Close.
- Transparency adjustment.

Use the smallest reasonable resource footprint.

Do not keep the overlay running when the user closes the session.

F. Gaming Notifications

Implement optional Do Not Disturb integration using the official Android notification policy access flow.

Never silently change notification settings.

G. Gaming Reports

After a gaming session, generate a real report containing:
- Game name.
- Session length.
- Highest observed temperature.
- Average available memory, if measured.
- Network measurements, if collected.
- Monitoring limitations.

Do not invent FPS or frame-time statistics.

---

# 11. LAG REDUCTION ENGINE

Build a separate Lag Reduction module.

Its job is to identify and help reduce actual sources of avoidable lag.

Perform:
- Memory-pressure analysis.
- CPU workload analysis.
- Thermal analysis.
- Connectivity checks.
- Storage availability checks.
- Game-specific diagnostic reports.

Possible user-approved actions:
- Close NovaBoost's own unnecessary tasks.
- Enable configured Do Not Disturb access.
- Pause NovaBoost monitoring when not needed.
- Open relevant Android settings.
- Recommend a lower workload when overheating is detected.

Do not indiscriminately force-stop all applications.

Do not use fake animations that claim to clean RAM.

Do not promise higher FPS without measured evidence.

---

# 12. NETWORK & VPN CENTER

Build a separate Network & VPN screen.

NETWORK DIAGNOSTICS:

- Internet connectivity.
- Connection type.
- Network state.
- Link information exposed by Android.
- HTTP latency tests.
- Download speed measurement using a legitimate configured test endpoint.
- Upload speed measurement using a legitimate configured test endpoint.
- Network stability history.

Always identify the measurement endpoint.

Do not use fabricated test results.

VPN REQUIREMENTS:

Build a genuine VPN setup and connection interface.

Use Android's official VpnService architecture if a real compatible VPN implementation can be integrated and tested.

The VPN must use a real VPN endpoint and a real supported protocol implementation.

Include:
- VPN connection status.
- Server configuration.
- Connection duration.
- Reconnection handling.
- Error reporting.
- Disconnect button.
- Clear permission flow.

Never:
- Invent VPN servers.
- Pretend that a button establishes a VPN.
- Display fake server countries.
- Build an insecure custom encryption protocol.
- Claim a connection is protected when no tunnel is active.
- Collect or transmit user traffic secretly.

If no VPN provider, server credentials or compatible protocol engine is available, implement the configuration interface and clearly show that no VPN connection is configured.

Do not pretend the VPN module is complete until an actual end-to-end connection has been tested.

---

# 13. BATTERY & THERMAL CENTER

Create a genuine battery-monitoring section.

Display:
- Battery percentage.
- Charging state.
- Reported health status.
- Battery temperature.
- Voltage if available.
- Power-source information where available.

Thermal monitoring:
- Available thermal status.
- Device thermal warnings.
- Session temperature history where measurable.
- Safe recommendations.

Battery tools:
- Battery usage information where publicly available.
- Charging status.
- Thermal alerts.
- Power-conscious monitoring profile.

Do not claim to repair battery health.

Do not invent battery capacity or cycle counts.

Do not disable Android thermal protection.

Do not claim that cooling software can physically cool a device.

---

# 14. DEVICE INFORMATION

Create a clean hardware information screen.

Display:

- Manufacturer.
- Model.
- Android version.
- API level.
- Build information.
- RAM capacity.
- Available storage.
- Processor information.
- CPU architecture.
- GPU information.
- Display resolution.
- Refresh rate.
- Battery information.
- Supported graphics capabilities.
- Available connectivity information.

Only show information actually obtained from Android APIs or accessible system sources.

---

# 15. AUTO SCAN ENGINE

Create one central Auto Scan function.

It should perform separate scans:

1. Memory diagnostics.
2. Storage analysis.
3. Temporary-file detection.
4. Duplicate-file detection.
5. Large-file detection.
6. CPU diagnostics.
7. GPU information.
8. Battery status.
9. Thermal diagnostics.
10. Network connectivity.

Display progress for every scan.

Each scan must return:
- Actual result.
- Total processing time.
- Detected issues.
- Recommended actions.
- Permission limitations.
- Error state.

The scan must not delete anything.

Deletion always requires separate user confirmation.

---

# 16. SETTINGS

Create a proper settings page with only relevant options.

Include:

- Theme.
- Dark/light mode.
- Refresh frequency.
- Monitoring preferences.
- Temperature warnings.
- Gaming notifications.
- Floating overlay settings.
- Cleaning preferences.
- Scan preferences.
- Automatic scan options.
- Data/history management.
- Permission management.
- Privacy information.
- About NovaBoost.

Every setting must actually affect application behavior.

Persist preferences locally.

Do not create settings that have no associated functionality.

---

# 17. PERMISSION MANAGEMENT

Use minimum necessary permissions.

Request permissions only when a user activates the corresponding feature.

Examples:

- Usage Access: optional gaming-session detection and application usage information.
- Overlay Permission: optional floating gaming monitor.
- Notification Permission: relevant session notifications on supported Android versions.
- Do Not Disturb Access: optional gaming mode.
- Storage Access Framework: user-selected cleaning directories.
- Internet Permission: actual network testing and configured VPN connections.
- VPN Permission: only for a real VPN implementation.

Never request root access in Standard Edition.

Never use Accessibility Service for unrelated system manipulation.

Never request broad storage permissions without a justified and policy-compliant need.

Provide permission explanations before opening system settings.

Handle denied and revoked permissions gracefully.

---

# 18. TECHNICAL ARCHITECTURE

Use:

- Kotlin.
- Jetpack Compose.
- Material 3.
- Android SDK.
- Kotlin Coroutines.
- ViewModel.
- StateFlow.
- Navigation Compose.
- DataStore for preferences.
- Room only if persistent structured history requires it.

Follow a clean, maintainable architecture.

Recommended logical structure:

app/
  MainActivity.kt
  NovaBoostApplication.kt

  core/
    common/
    permissions/
    hardware/
    storage/
    monitoring/

  data/
    local/
    models/
    repositories/

  domain/
    models/
    usecases/

  features/
    home/
    cleaner/
    memory/
    cpu/
    gpu/
    gaming/
    network/
    vpn/
    battery/
    device/
    settings/

  ui/
    components/
    theme/
    navigation/

  services/
    monitoring/
    gaming/
    overlay/

Adapt this logical structure to Google AI Studio's supported single-module Android project architecture.

Avoid unnecessary dependencies.

Do not use NDK-based libraries because the selected Android build environment does not support NDK development.

Use stable, compatible dependencies.

Keep package names, imports, resource names and Gradle configuration consistent.

---

# 19. PERFORMANCE REQUIREMENTS

NovaBoost must run smoothly on lower-end phones.

Initial target device:

Samsung Galaxy M01:
- Android 12.
- Approximately 3 GB RAM.
- Low-end mobile hardware.

Optimize for:
- Low memory consumption.
- Fast application startup.
- Minimal CPU consumption.
- Minimal battery consumption.
- Efficient Compose rendering.
- Limited background activity.
- Efficient local database access.
- Controlled monitoring intervals.

Never run heavy scans continuously.

Use background work only where justified.

Stop monitoring when the relevant screen or session is closed.

Avoid unnecessary network calls.

Do not introduce a server or cloud dependency for ordinary local functionality.

The application must work offline for its local hardware diagnostics, cleaning analysis and memory monitoring.

---

# 20. DATA PRIVACY

NovaBoost should operate primarily on-device.

Requirements:

- No mandatory account.
- No mandatory login.
- No unnecessary personal-data collection.
- No secret file uploads.
- No hidden analytics.
- No unnecessary external tracking.
- No automatic personal-file deletion.
- No hidden background monitoring.

Store user preferences and session records locally.

Explain what each permission is used for.

Provide a way to clear NovaBoost's own stored history.

---

# 21. TESTING REQUIREMENTS

Do not finish development after generating the UI.

Test the application.

A. Compilation:
- Resolve Kotlin errors.
- Resolve Gradle errors.
- Resolve missing imports.
- Resolve dependency conflicts.
- Resolve Compose errors.
- Resolve manifest issues.

B. Functionality:
- Test navigation.
- Test hardware-information retrieval.
- Test memory calculations.
- Test storage calculations.
- Test file scanning.
- Test deletion confirmation.
- Test permission denial.
- Test permission revocation.
- Test gaming launcher.
- Test session tracking.
- Test overlay permission handling.
- Test network disconnection.
- Test empty states.
- Test unavailable hardware metrics.

C. Safety:
- Verify that cleaning does not delete protected files.
- Verify that no fake swap operation is performed.
- Verify that no fake VPN connection is displayed.
- Verify that no fake FPS values are shown.
- Verify that no unsupported CPU/GPU modification is attempted.

D. Performance:
- Check startup speed.
- Check memory consumption.
- Check monitoring overhead.
- Check background behavior.
- Check screen responsiveness.

Fix discovered errors before considering the build complete.

Do not hide errors with dummy data.

---

# 22. UI AND FUNCTIONALITY ACCEPTANCE CRITERIA

The application is not complete unless:

[ ] Main navigation works.

[ ] Dashboard obtains actual device information.

[ ] RAM calculations are correct.

[ ] Storage calculations are correct.

[ ] Memory monitoring refreshes.

[ ] zRAM information is shown when accessible.

[ ] Swap status distinguishes file creation from activation.

[ ] Cleaner scans real user-accessible files.

[ ] Cleaner supports user-selected deletion.

[ ] Cache manager reports genuine limitations.

[ ] CPU diagnostics display real available information.

[ ] GPU information is obtained from the actual graphics environment.

[ ] Gaming launcher works for supported applications.

[ ] Gaming sessions can be started and stopped.

[ ] Gaming session history is saved.

[ ] Temperature warnings use actual readings.

[ ] Network diagnostics work with real connections.

[ ] VPN status never displays a fake connection.

[ ] Overlay permissions are handled correctly.

[ ] Settings are persistent.

[ ] Permissions can be revoked without crashing.

[ ] All buttons perform their advertised actions.

[ ] No unrelated features have been added.

[ ] No fake statistics or simulated results appear in production.

---

# 23. STRICT SCOPE LOCK — DO NOT ADD EXTRA FEATURES

This section is extremely important.

Build ONLY the features explicitly specified in this master specification.

Do not expand the product with unnecessary functionality.

DO NOT ADD:

- AI chatbot.
- AI assistant.
- Social media.
- User community.
- Messaging.
- Music player.
- Video player.
- Browser.
- News feed.
- App store.
- Cryptocurrency.
- Advertising system.
- Subscription system.
- Payment gateway.
- Account registration.
- Unnecessary cloud synchronization.
- Unrequested widgets.
- Fake benchmarking.
- Fake one-click optimization.
- Fake CPU overclocking.
- Fake GPU overclocking.
- Fake RAM expansion.
- Fake VPN servers.
- Decorative nonfunctional modules.

Do not add features simply because they are popular in other apps.

Prioritize completing the requested features correctly.

Quality is more important than feature count.

---

# 24. FINAL DEVELOPMENT INSTRUCTIONS

Start by selecting the native Android platform.

Generate the actual Kotlin and Jetpack Compose project.

Create the real source files.

Build the main navigation and design system.

Implement the functional modules.

Connect each screen to its actual data source.

Handle permissions.

Implement real file scanning and safe cleanup.

Implement genuine hardware monitoring.

Implement the Gaming Center and session tracking.

Implement network diagnostics.

Implement VPN functionality only to the extent that a real compatible VPN backend is available.

Run compilation and functionality checks.

Fix errors.

Do not stop after creating a beautiful interface.

Do not replace implementation with explanations.

Do not claim a feature works merely because its screen exists.

When a feature is technically unavailable, report it honestly and provide the closest safe alternative.

## FINAL PRODUCT

Deliver a functional native Android application:

# NOVABOOST
### One App. Real Optimization. Complete Device Intelligence.

The final result must be a clean, premium, reliable and genuinely useful Android performance-management application.

The application should be ready for real-device testing, particularly on lower-end Android hardware.

Build only what is specified. Do not add unnecessary features or unrelated content.