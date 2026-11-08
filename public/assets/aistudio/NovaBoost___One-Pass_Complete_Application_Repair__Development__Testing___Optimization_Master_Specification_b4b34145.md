# NOVABOOST
## ONE-PASS COMPLETE ENGINEERING AND FUNCTIONALITY REPAIR

PROJECT: NovaBoost

REPOSITORY:
https://github.com/kritikakumari0972-bit/apk-free

APPLICATION TYPE:
Native Android application.

TECHNOLOGY:
Kotlin + Jetpack Compose + Android SDK.

PROJECT OBJECTIVE:

Transform the existing NovaBoost project into a complete, professional, lightweight and genuinely functional Android phone optimization application.

This is a comprehensive development and repair assignment.

You must inspect, repair, implement, integrate, test and optimize the complete application in one coordinated engineering pass.

DO NOT create another project.

DO NOT generate another prototype.

DO NOT replace the application with a decorative interface.

DO NOT merely provide recommendations.

WORK DIRECTLY ON THE EXISTING NOVABOOST SOURCE CODE.

==================================================
1. FINAL DEVELOPMENT MISSION
==================================================

NovaBoost must become a complete Android optimization suite containing:

01. Smart Phone Cleaner
02. Deep Storage Cleaner
03. Duplicate File Finder
04. Application Cache Manager
05. Physical RAM Monitor
06. RAM Management Engine
07. Virtual Memory Diagnostics
08. zRAM Monitor
09. SWAP File Compatibility and Management
10. CPU Monitor
11. GPU Diagnostics
12. Gaming Center
13. Gaming Session Manager
14. FPS Diagnostics
15. Lag Detection and Reduction
16. Gaming Performance Profiles
17. Genuine VPN Center
18. Network Optimizer
19. DNS Diagnostics
20. Download and Upload Speed Testing
21. Battery Monitor
22. Battery Saver
23. Thermal Guard
24. Background Activity Monitor
25. Device Information
26. Smart Device Scanner
27. Performance History
28. Animated Application Startup
29. Floating Gaming HUD
30. Complete Settings

Every listed module must either perform its advertised operation or clearly identify its technical limitation.

No fabricated functionality is acceptable.

==================================================
2. MOST IMPORTANT RULE: COMPLETE THE PROJECT, NOT JUST THE UI
==================================================

You are an expert Android engineering team.

Use your expertise in:

- Native Android development.
- Kotlin.
- Jetpack Compose.
- Android framework APIs.
- Linux memory management.
- Storage systems.
- CPU/GPU monitoring.
- Gaming performance.
- Networking.
- VPN protocols.
- Battery management.
- Thermal management.
- Background processing.
- Software architecture.
- Automated testing.
- Android security.

Every operation must have a real implementation.

For each feature, identify:

1. What the user sees.
2. What Android functionality is required.
3. Which API or system interface supplies the data.
4. Which permissions are required.
5. Which operations are actually possible.
6. How the operation is verified.
7. What happens when the operation fails.

NEVER confuse an attractive UI with a completed feature.

NEVER confuse saving a preference with applying a setting.

NEVER confuse creating a file with activating system swap.

NEVER confuse establishing a TUN interface with establishing a working VPN.

NEVER confuse monitoring with optimization.

NEVER claim performance improvements without reliable measurements.

If something is impossible without additional system privileges, provider configuration or manufacturer support, explain that precisely and implement the best legitimate alternative.

==================================================
3. FIRST: FULL REPOSITORY AND DEPENDENCY AUDIT
==================================================

Before editing, retrieve the latest repository revision.

Inspect the complete source tree.

Previously audited files include:

- MainActivity.kt
- NovaBoostApplication.kt
- NovaMainViewModel.kt
- HardwareMonitor.kt
- GpuProbe.kt
- StorageCleaner.kt
- GamingSessionManager.kt
- NetworkDiagnostics.kt
- NovaBoostVpnService.kt
- FloatingOverlayService.kt
- MemoryScreen.kt
- CleanerScreen.kt
- NetworkVpnScreen.kt
- AutoScanScreen.kt
- StartupSplashScreen.kt
- NovaDatabase.kt
- build-apk.yml

Inspect all actual current paths.

Also inspect:

- AndroidManifest.xml
- Gradle files.
- Room entities.
- DAOs.
- Database migrations.
- Navigation.
- Repositories.
- Data models.
- Permission handlers.
- UI state management.
- All services.
- Resources.
- Icons.
- Dependencies.
- GitHub Actions.

Do not rely only on the README.

Do not assume previous defects remain unchanged.

For every previously reported defect:

- Verify whether it is still present.
- Identify its actual cause.
- Identify all dependent files.
- Repair it.
- Test it.
- Verify the fix.

Create a comprehensive dependency map.

Avoid duplicate implementations and unnecessary file rewrites.

==================================================
4. CRITICAL BUG REPAIR: VPN
==================================================

PRIORITY: CRITICAL.

PREVIOUSLY IDENTIFIED FILE:

NovaBoostVpnService.kt

PREVIOUS IMPLEMENTATION PROBLEMS:

- TCP endpoint connection used as a fake handshake.
- TCP socket immediately closed.
- TUN interface established.
- IPv4 default route configured.
- VPN status reported as CONNECTED.
- No genuine packet-processing engine.
- No real remote packet forwarding.
- No actual encryption protocol.
- Traffic counters remain at zero.
- StateFlow properties return snapshots instead of shared observable state.

Completely repair this architecture.

A. Remove false VPN status.

A successful local TUN interface creation does not establish a genuine VPN.

A successful TCP connection does not establish an encrypted VPN tunnel.

A Boolean variable does not establish a VPN connection.

Remove all misleading connection reporting.

B. Integrate an actual VPN backend.

Use a maintained and compatible implementation of a recognized VPN protocol.

Do not invent a cryptographic protocol.

Do not build an insecure custom encryption implementation.

Implement the actual data path:

USER AUTHORIZATION
→ CONFIGURATION VALIDATION
→ REMOTE ENDPOINT
→ AUTHENTICATION
→ PROTOCOL HANDSHAKE
→ ENCRYPTED TUNNEL
→ PACKET PROCESSING
→ REMOTE TRANSPORT
→ RESPONSE RECEPTION
→ TUN INTEGRATION
→ VERIFIED CONNECTION.

Support real endpoint configuration.

Do not invent servers or credentials.

C. VPN connection states:

- NOT CONFIGURED
- INVALID CONFIGURATION
- PERMISSION REQUIRED
- CONNECTING
- HANDSHAKE
- CONNECTED
- RECONNECTING
- DISCONNECTING
- DISCONNECTED
- FAILED

Only the genuine backend may establish the connected state.

D. Actual VPN measurements:

- Endpoint.
- Connection duration.
- Handshake duration.
- Bytes sent.
- Bytes received.
- Connection status.
- DNS functionality.
- Actual endpoint responsiveness.

Counters must increase based on actual traffic.

E. Fail-safe behavior:

Handle:

- Missing configuration.
- Failed authentication.
- Invalid endpoint.
- Handshake failure.
- DNS failure.
- Network switching.
- Internet interruption.
- Permission revocation.
- Service termination.
- Manual disconnection.

Never leave a default route pointing into a nonfunctional TUN interface.

F. Low-latency diagnostics:

Measure actual DNS resolution, endpoint connection time, handshake duration and response time.

Compare direct and VPN connectivity where possible.

Never promise lower ping.

Never claim that a nearby server is faster without measurements.

G. Missing backend:

If no real VPN protocol backend or provider configuration is available, display:

VPN PROVIDER CONFIGURATION REQUIRED.

Keep the feature safely disabled.

Do not mark an incomplete VPN as finished.

H. Observable state:

Use a shared StateFlow or another lifecycle-safe observable state source.

Do not return newly constructed StateFlow snapshots from property getters.

Ensure the UI receives actual service-state updates.

==================================================
5. CRITICAL BUG REPAIR: PHYSICAL RAM
==================================================

PREVIOUS FILE:

HardwareMonitor.kt

The previous JVM heap fallback must remain removed.

Never use:

Runtime.maxMemory()

as physical device RAM.

It represents an application heap limit.

Use genuine Android memory information.

Required:

- Physical RAM total.
- Physical RAM available.
- Physical RAM used.
- Memory threshold.
- Low-memory status.
- NovaBoost process memory.
- Device-wide memory information.
- System swap.

Keep each metric separate.

Use:

Used RAM = Total RAM - Available RAM.

Usage percentage = Used RAM / Total RAM × 100.

Validate zero values, negative values and invalid readings.

Do not substitute arbitrary defaults.

If a value cannot be collected, display UNAVAILABLE.

Create unit tests for the calculations.

==================================================
6. CRITICAL BUG REPAIR: zRAM
==================================================

Previously identified files:

HardwareMonitor.kt
MemoryScreen.kt

Completely correct the zRAM identification system.

Inspect accessible:

- /proc/meminfo
- /proc/swaps
- /sys/block/zram*
- Available zRAM statistics.

Create four distinct conditions:

1. DETECTED.
2. CONFIGURED.
3. ACTIVE.
4. CURRENTLY IN USE.

Do not classify zRAM as active simply because its device directory exists.

Do not classify ordinary system swap as zRAM.

Do not use total system swap as zRAM capacity without identifying its source.

The Memory screen must not use an expression equivalent to:

swapTotalBytes > 0 || zRamAccessible

as proof of active zRAM.

Display only supported measurements:

- Device status.
- Configured capacity.
- Active capacity attributable to zRAM.
- Swap usage where identifiable.
- Compression information where accessible.

If a measurement is inaccessible, clearly report it.

==================================================
7. VIRTUAL RAM AND SWAP FILE MANAGEMENT
==================================================

Implement a real memory capability and swap-management engine.

A normal no-root Android application cannot activate arbitrary Linux system swap merely by creating a storage file.

Respect this limitation.

STANDARD EDITION:

- Detect existing swap.
- Detect zRAM.
- Read accessible kernel information.
- Check available storage.
- Check exposed filesystem information.
- Report compatibility.
- Display real activation limitations.
- Never claim a generated file is active system swap.

SUPPORTED SYSTEM-INTEGRATION EDITION:

Only implement activation through a legitimate authorized system integration.

When genuine support exists:

- Check kernel support.
- Check storage compatibility.
- Verify authorization.
- Allow suitable size selection.
- Create the file.
- Request authorized activation.
- Verify actual activation.
- Monitor actual use.
- Support proper deactivation.
- Remove the file only after confirmed deactivation.

Implement accurate states:

- NOT CONFIGURED.
- FILE CREATED.
- ACTIVATION UNSUPPORTED.
- PERMISSION REQUIRED.
- ACTIVATED.
- ACTIVE AND USED.
- INACTIVE.
- ERROR.

Do not create useless huge files.

Do not waste storage.

Do not use unauthorized privilege escalation.

==================================================
8. COMPLETE PHONE CLEANER
==================================================

PREVIOUS FILE:

StorageCleaner.kt

Preserve previous improvements:

- Full-file SHA-256.
- Streaming verification.
- Empty default selection.
- Unique deletion paths.
- Actual deletion verification.
- Partial-success tracking.

Now finish the complete cleaner.

A. Scanning:

Detect accessible:

- Temporary files.
- Duplicate files.
- Large files.
- Old downloads.
- Empty folders.
- APK installation files.
- Old archives.
- User-selected unnecessary files.
- Accessible application caches.
- Identifiable removable thumbnails.

B. Storage Access Framework:

Implement genuine user-selected directory access.

Allow the user to grant directory permissions.

Persist permission when Android permits.

Handle revoked access.

Support user-selected folders without bypassing scoped storage.

C. Unique file registry:

A file can belong to several categories but must appear once in the canonical file list.

Calculate total recoverable storage using unique files only.

Avoid duplicate deletion.

D. Duplicate detection:

Use complete SHA-256 verification.

Use streaming reads.

Handle large files without loading them entirely into RAM.

Support cancellation.

E. Safe deletion:

SCAN
→ REVIEW
→ SELECT
→ CONFIRM
→ DELETE
→ VERIFY.

Default selection must be EMPTY.

Never automatically select personal files.

Never automatically delete:

- Photos.
- Videos.
- Personal documents.
- Archives.
- Downloads.
- Unknown files.

F. Verification:

For every selected file:

- Verify access.
- Recheck metadata.
- Delete.
- Check whether it disappeared.
- Record success or failure.

Statuses:

SUCCESS.
FAILED.
PARTIAL SUCCESS.
UNAVAILABLE.
CANCELLED.

Do not report complete success when some deletions fail.

G. Accurate category totals:

Prevent overlapping category counts from inflating total storage recovery.

H. Internal cache:

Keep NovaBoost's own cache deletion as a separate operation.

Never silently clean it when another category is processed.

I. Other applications' cache:

Use only legitimate Android-supported mechanisms.

Open App Info or supported system storage-management interfaces where necessary.

Never claim unrestricted private-cache deletion.

==================================================
9. COMPLETE APPLICATION CACHE MANAGER
==================================================

Implement:

- Installed application discovery within package-visibility limits.
- App name.
- App icon.
- Accessible storage statistics.
- Cache information where available.
- Storage sorting.
- Application details.
- Open App Info.
- Supported system cache-cleaning flow.

Use the official Android permission and user-consent mechanisms available on the target version.

Do not access protected private directories.

Do not use hidden APIs.

Do not display deleted amounts without verification.

==================================================
10. CPU PERFORMANCE ENGINE
==================================================

Inspect HardwareMonitor.kt.

Preserve synchronized sampling improvements.

Implement:

- Processor information.
- Architecture.
- ABI.
- Core count.
- CPU frequency where accessible.
- Per-core information where accessible.
- Device-wide CPU usage where measurable.
- NovaBoost process CPU usage.
- CPU history.
- Thermal information.

Fix:

- Concurrent sampling.
- Incorrect intervals.
- Invalid deltas.
- Counter resets.
- Missing interfaces.
- Access errors.

Do not confuse device CPU utilization with application process utilization.

Use real Android performance integrations when available.

Do not advertise unsupported overclocking.

Do not pretend to add processor cores.

==================================================
11. GPU DIAGNOSTICS ENGINE
==================================================

Inspect GpuProbe.kt.

Collect actual:

- GPU renderer.
- GPU vendor.
- OpenGL ES version.
- Supported extensions.
- Vulkan availability where reliably detectable.
- Graphics capabilities.
- Texture information where accessible.

Remove generic fallback information presented as confirmed hardware specifications.

Do not use:

- Fake GPU vendors.
- Fake renderer names.
- Fake OpenGL versions.
- Invented texture limits.
- Fake GPU usage percentages.

If GPU utilization cannot be measured, display:

REAL-TIME GPU UTILIZATION UNAVAILABLE.

Use appropriate graphics contexts and threads.

Never block the UI thread.

==================================================
12. COMPLETE GAMING CENTER
==================================================

Build a separate Gaming Center.

A. Game Library:

- Discover supported launchable applications.
- Display game names.
- Display icons.
- Add games manually.
- Launch games.
- Remove games from the NovaBoost library without uninstalling them.
- Save individual profiles.

Respect package-visibility restrictions.

B. Gaming sessions:

Record:

- Game.
- Start time.
- End time.
- Duration.
- RAM readings.
- CPU readings.
- Battery status.
- Thermal status.
- Network latency.
- FPS only when reliably available.

Save session history.

Prevent duplicate sessions.

Handle application interruptions.

C. Gaming profiles:

Create:

- Performance Monitoring.
- Balanced.
- Battery-Conscious.
- Thermal-Conscious.

Do not treat a stored profile name as proof of applied system optimization.

Clearly distinguish:

- Saved preference.
- Applied setting.
- Monitoring configuration.
- Unsupported capability.

Use supported Android Game Mode or performance integrations when available.

D. Floating Gaming HUD:

Display available real-time information.

Support:

- Move.
- Minimize.
- Close.
- Transparency.
- Monitoring interval.
- Permission management.

Collect measurements using background dispatchers.

Update the UI on the main thread.

Cancel monitoring when the session ends.

Do not create duplicate jobs.

==================================================
13. FPS MONITOR AND LAG REDUCER
==================================================

Implement reliable FPS diagnostics where supported.

Do not create timer-based fake FPS values.

Do not pretend that the refresh rate is equal to the game's actual FPS.

Separate:

- Display refresh rate.
- Measured FPS.
- Frame timing.
- Graphics information.
- Thermal status.

When accurate external-game FPS is inaccessible, show:

FPS DATA UNAVAILABLE.

Lag diagnostics must identify:

- MEMORY RELATED.
- CPU RELATED.
- THERMAL RELATED.
- NETWORK RELATED.
- STORAGE RELATED.
- UNKNOWN.

Do not classify every issue as RAM-related.

Recommendations must depend on actual readings.

Do not indiscriminately force-stop apps.

Do not guarantee zero lag.

Do not guarantee arbitrary FPS increases.

==================================================
14. THERMAL GUARD
==================================================

Correct the previous gaming diagnosis.

Battery temperature exceeding 41°C does NOT independently prove CPU/GPU thermal throttling.

Use Android PowerManager thermal status where available.

Separate:

- Battery temperature.
- Device thermal status.
- CPU workload.
- GPU information.
- Actual throttling evidence where available.

Implement:

- NORMAL.
- MODERATE.
- SEVERE.
- CRITICAL.
- UNAVAILABLE.

Safe actions:

- Reduce NovaBoost monitoring.
- Pause unnecessary scans.
- Reduce HUD updates.
- Warn about excessive workload.
- Recommend reducing gaming intensity.

Do not disable Android thermal protection.

Do not claim software physically cools hardware.

==================================================
15. BATTERY SAVER
==================================================

Fix the current mismatch between stored battery profiles and the actual telemetry refresh loop.

Previously:

The selected preferences changed autoRefreshSec, but startTelemetryLoop() continued using a fixed delay of 3000 milliseconds.

Repair this.

The telemetry loop must observe the actual saved monitoring preference.

Implement:

- Dynamic refresh interval.
- Proper coroutine cancellation.
- No duplicated telemetry loops.
- Correct profile application.
- Battery-conscious monitoring.
- Gaming monitoring profile.
- Balanced profile.
- Power-saving profile.

Profiles must actually change NovaBoost's own resource usage.

Do not claim to control protected system settings.

Display the active refresh interval accurately.

==================================================
16. BACKGROUND ACTIVITY MONITOR
==================================================

Implement genuine application diagnostics.

Show supported:

- Installed apps.
- Application names.
- Icons.
- Usage statistics.
- Last-used information where accessible.
- Storage information.
- Battery information where available.
- Background activity indicators where measurable.

Provide:

- Open App Info.
- Open battery settings.
- Open storage settings.
- Supported app-management actions.

Do not call historical usage data real-time activity.

Do not force-stop all applications.

Do not disable essential Android components.

==================================================
17. NETWORK OPTIMIZER
==================================================

Implement genuine:

- Internet connectivity.
- Connection type.
- DNS testing.
- TCP latency.
- HTTP response time.
- Download testing.
- Upload testing.
- Network stability.
- Connection changes.

Use real test endpoints.

Display the endpoint.

Measure actual transferred bytes and elapsed time.

Distinguish TCP latency from ICMP ping.

Distinguish failed connections from packet loss.

Never invent network speeds.

==================================================
18. COMPLETE VPN CENTER
==================================================

This section must integrate with the real VPN backend implemented earlier.

Do not create a second fake VPN architecture.

Use one shared VPN state source.

Display:

- Configuration status.
- Endpoint.
- Protocol.
- Authorization status.
- Handshake progress.
- Actual connection status.
- Connection duration.
- Actual traffic counters.
- Error messages.

Support:

- User-provided configurations.
- Legitimate provider configuration.
- Secure credential handling.
- Reconnection.
- Disconnection.
- Network changes.

The actual tunnel must process and forward traffic.

If a real backend is unavailable, keep the feature in CONFIGURATION REQUIRED state.

==================================================
19. BATTERY AND THERMAL HISTORY
==================================================

Use Room database records.

Store genuine timestamped measurements.

Track:

- Battery level.
- Charging state.
- Temperature.
- Thermal status.
- Monitoring events.

Do not fabricate missing data.

Do not invent battery health percentages.

Use proper database migrations.

==================================================
20. COMPLETE DEVICE INFORMATION
==================================================

Display:

- Manufacturer.
- Model.
- Android version.
- API level.
- Build information.
- CPU architecture.
- CPU cores.
- Physical RAM.
- GPU renderer.
- Graphics APIs.
- Storage.
- Display resolution.
- Refresh rate.
- Battery.
- Thermal capability.
- Network capability.

Only show confirmed information.

Use UNAVAILABLE when detection fails.

==================================================
21. SMART AUTO SCAN
==================================================

Fix the previously discovered mismatch between the 14-stage ViewModel and the 8-point screen description.

Use one shared scan-stage definition.

Do not maintain two independent scan-stage counts.

Implement:

1. Hardware compatibility.
2. Physical RAM.
3. zRAM.
4. System swap.
5. Storage.
6. Temporary files.
7. Duplicate files.
8. CPU.
9. GPU.
10. Battery.
11. Thermal.
12. Network.
13. Background activity.
14. Gaming readiness.

For every stage, display:

- Name.
- Status.
- Start time.
- Completion time.
- Result.
- Error.
- Measurable progress.

Implement:

- File-level progress where possible.
- Indeterminate progress where totals are unknown.
- Cancellation.
- Retry.
- Error recovery.
- Partial-result preservation.
- Guaranteed loading-state cleanup.

Do not use fixed delays.

Do not generate fake progress percentages.

Do not delete files during Smart Scan.

Gaming readiness must not claim high-FPS capability using only available RAM and battery temperature.

==================================================
22. ANIMATED APPLICATION EXPERIENCE
==================================================

Preserve the new StartupSplashScreen.kt.

Improve the complete app-opening experience.

Startup:

- Animated NovaBoost logo.
- Smooth entrance.
- Premium background.
- Real initialization status.
- Fast first render.

Screens:

- Animated card entry.
- Live data transitions.
- Loading skeletons.
- Real scanning feedback.
- Error animations.
- Success animations.
- Smooth navigation.
- Animated charts based on actual measurements.

Use appropriate Jetpack Compose animation APIs.

Keep animations lightweight.

Do not block rendering.

Respect low-end devices.

==================================================
23. PERFORMANCE HISTORY AND DATABASE
==================================================

Preserve the new Room database architecture.

CRITICAL:

The current version has a previous database version 1 and a newer version 2, with fallbackToDestructiveMigration().

Remove destructive migration as the normal upgrade strategy.

Implement an explicit Migration(1, 2).

Preserve existing history data.

Inspect every entity and DAO.

Test migrations.

Add future schema migrations when needed.

Persist:

- Performance records.
- Scan history.
- Gaming sessions.
- Battery profiles.
- Gaming preferences.
- User settings.
- Cleaning history.

Avoid unnecessary duplicate rows.

Allow users to delete NovaBoost's own history.

==================================================
24. STARTUP AND VIEWMODEL PERFORMANCE
==================================================

Fix synchronous hardware reads in NovaMainViewModel property initializers.

Previously identified initial reads include:

- RAM.
- Storage.
- Battery.
- Display.

Move all hardware probing into asynchronous initialization.

The main screen must render immediately.

Use loading states for individual metrics.

Do not wait for the GPU probe before rendering Home.

Do not execute expensive filesystem operations on the UI thread.

Use structured concurrency.

Handle cancellation.

Prevent duplicate initialization.

==================================================
25. PERMISSION MANAGEMENT
==================================================

Use only necessary Android permissions.

Request permissions when a relevant feature is opened.

Examples:

- Internet.
- Network state.
- Usage access.
- Overlay.
- Notifications.
- Do Not Disturb.
- SAF document access.
- VPN authorization.
- Required foreground-service permissions.

Respect package visibility.

Do not request QUERY_ALL_PACKAGES without a genuine policy-compliant requirement.

Never request root in Standard Edition.

Do not use Accessibility Service for unrelated manipulation.

Handle permission denial and revocation.

Explain each permission.

==================================================
26. COMPLETE SETTINGS
==================================================

Provide:

- Dark/light appearance.
- Accent settings.
- Animation preferences.
- Monitoring intervals.
- Battery profiles.
- Gaming profiles.
- Overlay settings.
- Scan settings.
- Cleaner settings.
- Thermal warnings.
- Notification preferences.
- Data history.
- Permission status.
- Privacy controls.

Every setting must influence actual application behavior.

Do not create unused switches.

==================================================
27. APPLICATION ARCHITECTURE
==================================================

Use:

- Kotlin.
- Jetpack Compose.
- Material 3.
- Android SDK.
- ViewModel.
- StateFlow.
- Coroutines.
- Navigation Compose.
- DataStore.
- Room.

Maintain proper separation between:

- UI.
- Domain logic.
- Hardware monitoring.
- Storage.
- Memory.
- VPN.
- Networking.
- Gaming.
- Thermal.
- Database.
- Permissions.

Avoid:

- GlobalScope.
- Main-thread blocking.
- Duplicate services.
- Duplicated telemetry jobs.
- Uncontrolled polling.
- Large in-memory file operations.
- Unnecessary dependencies.

==================================================
28. LOW-END DEVICE OPTIMIZATION
==================================================

Primary test target:

Samsung Galaxy M01.
Android 12.
Approximately 3 GB RAM.

Also support the widest practical Android device range through runtime capability detection.

Optimize:

- Startup.
- Memory usage.
- CPU consumption.
- Battery consumption.
- File scanning.
- Charts.
- Animations.
- Overlay.
- Database.
- Background tasks.

NovaBoost itself must not become a source of lag.

Do not introduce heavy monitoring loops.

Use appropriate refresh intervals.

Stop monitoring when features are no longer active.

==================================================
29. SECURITY AND PRIVACY
==================================================

No mandatory registration.

No unnecessary cloud server.

No hidden analytics.

No hidden uploads.

No unnecessary tracking.

No collection of personal files.

No secret credential logging.

Protect VPN configurations.

Use secure communication.

Allow users to clear local history.

Follow Android security and privacy requirements.

==================================================
30. GITHUB ACTIONS AND RELEASE SIGNING
==================================================

Inspect:

.github/workflows/build-apk.yml

Fix the build pipeline completely.

Requirements:

1. Correct JDK.
2. Compatible Gradle.
3. Android SDK setup.
4. Dependency resolution.
5. Gradle caching.
6. Unit tests.
7. Lint.
8. Debug APK.
9. Release APK when credentials exist.
10. APK verification.
11. SHA-256.
12. Artifact upload.

CRITICAL RELEASE SIGNING PROBLEM:

The previous workflow used a hardcoded password:

novaboostreleasepass

and generated a new release keystore when persistent signing credentials were missing.

Remove this behavior.

Implement persistent release signing using GitHub Actions secrets.

Use:

- Persistent encoded keystore.
- Keystore password.
- Key alias.
- Key password.

Never hardcode signing credentials.

Never print credentials.

Never generate a new release signing identity for every build.

If release credentials are missing:

- Build the debug APK.
- Mark release signing unavailable.
- Do not silently create a replacement release identity.

Preserve update compatibility.

==================================================
31. FULL AUTOMATED TESTING
==================================================

Do not stop at compilation.

Run:

./gradlew clean

./gradlew testDebugUnitTest

./gradlew lintDebug

./gradlew assembleDebug

Create unit tests for:

RAM:
- Total memory.
- Available memory.
- Usage percentage.
- Invalid values.

zRAM:
- Detection.
- Configuration.
- Activation.
- Ordinary swap distinction.

Cleaner:
- SHA-256.
- Unique paths.
- Duplicate categories.
- File deletion.
- Partial failure.
- SAF access.
- Cancellation.

VPN:
- Missing configuration.
- Invalid configuration.
- Authorization.
- Real protocol handshake.
- Packet forwarding.
- Connection state.
- Disconnect.

CPU:
- Sampling.
- Concurrent calls.
- Counter resets.
- Invalid data.

GPU:
- Renderer.
- Vendor.
- Unsupported readings.
- Failed probe.

Gaming:
- Sessions.
- Profiles.
- Thermal classification.
- Network classification.
- History.

Battery:
- Refresh interval changes.
- Profile persistence.
- Monitoring cancellation.

Database:
- Version 1 to version 2 migration.
- Existing history preservation.

Auto Scan:
- Shared stage definitions.
- Progress.
- Cancellation.
- Error recovery.
- Final state cleanup.

UI:
- Navigation.
- Startup.
- Loading.
- Permissions.
- Animations.
- Screen-size compatibility.

Testing must never silently ignore failure.

Do not ship test fixtures as production hardware data.

==================================================
32. REAL DEVICE COMPATIBILITY TESTING
==================================================

Test the Samsung Galaxy M01 Android 12 where physical access is available.

Also use Android emulators for additional API levels.

Record:

- Device.
- Android version.
- Supported APIs.
- Successfully tested features.
- Restricted features.
- Unsupported hardware metrics.
- Runtime errors.

Never claim testing on hardware that was not actually tested.

Never claim universal compatibility solely because compilation succeeded.

==================================================
33. STRICT SCOPE LOCK
==================================================

Do not introduce unrelated features.

DO NOT ADD:

- AI chatbot.
- AI assistant.
- Social media.
- Browser.
- Music player.
- Video player.
- News feed.
- Cryptocurrency.
- Advertising platform.
- Subscription system.
- Payment gateway.
- Mandatory login.
- Unrequested widgets.
- Fake benchmark screens.
- Unrelated utilities.

Preserve NovaBoost's purpose.

Do not clutter the interface.

Do not generate excessive explanatory text inside the application.

==================================================
34. ONE-PASS EXECUTION REQUIREMENT
==================================================

This is a complete coordinated engineering assignment.

Do not stop after the initial inspection.

Do not return only a plan.

Do not fix only the most visible screen.

Do not implement one module while leaving its dependent modules inconsistent.

Do not ask the user to repeat the same requirements.

Perform the full repair in one continuous development pass.

Follow this sequence:

PHASE 1:
Repository inspection and dependency analysis.

PHASE 2:
Core state management and architecture.

PHASE 3:
Physical RAM, zRAM and SWAP.

PHASE 4:
Cleaner, storage and cache management.

PHASE 5:
CPU and GPU monitoring.

PHASE 6:
VPN backend and network diagnostics.

PHASE 7:
Gaming, FPS diagnostics and lag reduction.

PHASE 8:
Battery, thermal and background management.

PHASE 9:
Database, preferences and migrations.

PHASE 10:
Startup, loading, animations and interface.

PHASE 11:
Performance and compatibility.

PHASE 12:
Automated testing and bug fixing.

PHASE 13:
GitHub Actions, APK and signing.

PHASE 14:
Final source audit and acceptance verification.

After each phase:

1. Inspect modified source.
2. Check dependent classes.
3. Compile affected code.
4. Run relevant tests.
5. Correct new errors.
6. Continue.

Do not abandon later phases because one optional hardware feature is unsupported.

Implement safe alternatives for unsupported capabilities.

Do not report unimplemented external-provider integrations as completed.

==================================================
35. FINAL ACCEPTANCE CHECKLIST
==================================================

APPLICATION:
[ ] Existing repository preserved.
[ ] No unnecessary recreation.
[ ] Native Android application.
[ ] Premium interface.
[ ] Complete navigation.

STARTUP:
[ ] Animated splash.
[ ] Fast first render.
[ ] Asynchronous initialization.
[ ] Correct loading states.

CLEANER:
[ ] Real scanning.
[ ] SAF folder selection.
[ ] Full SHA-256 verification.
[ ] Unique file accounting.
[ ] Safe deletion.
[ ] Actual deletion verification.
[ ] Correct recovered storage.

MEMORY:
[ ] Physical RAM.
[ ] Correct calculations.
[ ] Accurate zRAM detection.
[ ] Correct system swap reporting.
[ ] SWAP capability detection.
[ ] No fake memory expansion.

HARDWARE:
[ ] CPU diagnostics.
[ ] GPU diagnostics.
[ ] No fabricated statistics.
[ ] Battery monitoring.
[ ] Thermal monitoring.

GAMING:
[ ] Game library.
[ ] Sessions.
[ ] Persistent profiles.
[ ] Lag detection.
[ ] FPS capability detection.
[ ] Floating HUD.
[ ] Gaming history.

VPN:
[ ] Real provider/backend.
[ ] Real protocol handshake.
[ ] Real encrypted tunnel.
[ ] Packet forwarding.
[ ] Genuine counters.
[ ] Safe disconnection.
[ ] No false connected state.

NETWORK:
[ ] DNS test.
[ ] Latency test.
[ ] Download test.
[ ] Upload test.
[ ] Network stability.

BATTERY:
[ ] Working monitoring profiles.
[ ] Dynamic refresh interval.
[ ] Battery history.
[ ] Safe resource reduction.

THERMAL:
[ ] Actual Android thermal status.
[ ] Battery temperature separated.
[ ] Correct throttling diagnosis.
[ ] Thermal warnings.

BACKGROUND:
[ ] Activity diagnostics.
[ ] Proper lifecycle.
[ ] No unnecessary services.

DATABASE:
[ ] Room migration.
[ ] Existing history preserved.
[ ] Profiles persisted.

AUTO SCAN:
[ ] Consistent stage count.
[ ] Genuine progress.
[ ] Cancellation.
[ ] Error recovery.
[ ] No automatic deletion.

RELEASE:
[ ] Secure signing.
[ ] Persistent signing identity.
[ ] Mandatory tests.
[ ] Lint.
[ ] APK build.
[ ] SHA-256.
[ ] Artifact upload.

==================================================
36. FINAL DELIVERY
==================================================

When every feasible task is implemented and tested, provide an engineering report.

Include:

1. Latest base commit.
2. Files modified.
3. Files created.
4. Defects fixed.
5. Features fully implemented.
6. Features partially implemented.
7. Features requiring external configuration.
8. Features restricted by Android.
9. Tests actually executed.
10. Test results.
11. Compilation results.
12. Lint results.
13. APK build status.
14. APK artifact location.
15. Release signing status.
16. Physical-device testing status.
17. Remaining issues.

Never invent successful test results.

Never claim the physical APK was tested when it was not.

Never report a feature complete merely because its screen exists.

==================================================
FINAL PRODUCT
==================================================

# NOVABOOST

ULTIMATE ANDROID OPTIMIZATION SUITE.

One application combining:

- Smart Cleaner.
- Deep Storage Analyzer.
- RAM Management.
- zRAM Diagnostics.
- SWAP Management.
- CPU Engine.
- GPU Diagnostics.
- Gaming Center.
- FPS Diagnostics.
- Lag Reducer.
- Genuine VPN.
- Network Optimizer.
- Battery Saver.
- Thermal Guard.
- Background Activity Manager.
- Device Information.
- Smart Diagnostics.
- Performance History.
- Floating Gaming HUD.
- Premium Animated UI.

THE FINAL PRODUCT MUST BE:

GENUINELY FUNCTIONAL.
ACCURATE.
SECURE.
LIGHTWEIGHT.
DEVICE-AWARE.
PROPERLY TESTED.
EASY TO USE.

NO FAKE STATISTICS.
NO FAKE OPERATIONS.
NO FAKE VPN.
NO FAKE RAM.
NO FAKE FPS.
NO FALSE CLEANER RESULTS.
NO FALSE THERMAL WARNINGS.
NO USELESS FEATURES.
NO NONFUNCTIONAL BUTTONS.

PRESERVE THE EXISTING NOVABOOST PROJECT.

FIX THE ENTIRE APPLICATION.

TEST THE ENTIRE APPLICATION.

DELIVER THE BEST VERIFIED BUILD POSSIBLE IN THIS DEVELOPMENT PASS.