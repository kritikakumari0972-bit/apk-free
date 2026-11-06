# NOVABOOST — CRITICAL ENGINEERING REPAIR
## EXISTING PROJECT: COMPLETE SOURCE CODE FIXING

Repository:
https://github.com/kritikakumari0972-bit/apk-free

You are working as a senior Android engineering team specializing in Kotlin, Jetpack Compose, Android system APIs, Linux memory management, storage management, VPN architecture, networking, gaming performance and Android application optimization.

Your task is to perform another comprehensive repair of the EXISTING NovaBoost application.

This is a targeted engineering repair, not a project recreation.

The previous implementation introduced improvements, but serious defects remain.

Your objective is to correct every defect identified below, preserve existing working functionality, improve performance and deliver a genuinely functional Android application.

# SECTION 1 — ABSOLUTE RULES

1. Work directly inside the existing NovaBoost project.
2. Inspect the latest GitHub commit before editing.
3. Never assume previous source-code paths are unchanged.
4. Inspect every affected class and its dependencies.
5. Preserve existing working implementations.
6. Do not regenerate the application from scratch.
7. Do not replace native Kotlin code with a website.
8. Do not create decorative replacement screens.
9. Do not fabricate hardware statistics.
10. Do not simulate successful operations.
11. Do not create fake VPN connections.
12. Do not claim ordinary files are system RAM.
13. Do not claim fake FPS improvements.
14. Do not claim fake temperature reductions.
15. Do not delete user files without explicit selection and confirmation.
16. Do not use hidden Android APIs.
17. Do not attempt unauthorized root access.
18. Do not add unrelated functionality.
19. Do not leave unfinished buttons.
20. Do not mark a feature complete until its underlying implementation has been verified.

If a feature cannot work under ordinary Android permissions, implement the closest legitimate alternative and clearly identify the limitation.

# SECTION 2 — FULL REPOSITORY INSPECTION

Before editing, inspect:

- README.md
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
- HomeScreen.kt
- MemoryScreen.kt
- CleanerScreen.kt
- AutoScanScreen.kt
- NetworkVpnScreen.kt
- AndroidManifest.xml
- app/build.gradle.kts
- settings.gradle.kts
- All related models, repositories, services and UI components.
- GitHub Actions workflows.

Find actual paths before modifying anything.

Build a dependency map.

Identify duplicated functions, race conditions, incorrect calculations, incorrect fallbacks, lifecycle issues and missing functionality.

Do not overwrite files blindly.

# SECTION 3 — CRITICAL FIX 1: REAL VPN ENGINE

PRIORITY: CRITICAL.

The existing VPN implementation creates a local TUN interface and reports a connected state, but it does not implement the required remote packet-processing and forwarding functionality.

Files to inspect:

- NovaBoostVpnService.kt
- NetworkVpnScreen.kt
- NovaMainViewModel.kt
- NetworkDiagnostics.kt
- AndroidManifest.xml

## A. Remove the misleading VPN implementation

Remove all connection-state logic that treats a successful local TUN interface creation as proof of an active VPN connection.

Remove misleading messages such as:

"Encrypted Tunnel Active"

unless an actual encrypted remote tunnel has been established and tested.

A local Boolean must never determine actual VPN connection status.

## B. Implement a genuine VPN backend

Use Android's official VpnService architecture and a maintained, compatible VPN protocol implementation.

Prefer integrating a mature, maintained VPN backend instead of inventing a new cryptographic protocol.

The implementation must support the actual data path:

1. User authorizes VPN access.
2. VPN backend receives valid configuration.
3. Remote endpoint is contacted.
4. Protocol handshake is completed.
5. Tunnel is established.
6. Traffic is processed through the backend.
7. Packets are securely transported.
8. Responses are received.
9. Valid traffic is returned to the TUN interface.
10. Connection state is updated from the real backend.
11. Disconnection terminates the tunnel correctly.

Do not implement a custom encryption protocol.

Do not create a fake remote server.

Do not invent VPN credentials.

Do not hardcode a fictional server port.

Do not claim that a VPN works without an actual configured provider or endpoint.

## C. VPN configuration

Support a legitimate compatible configuration supplied by the user or provider.

Validate:

- Endpoint.
- Port.
- Protocol.
- Authentication configuration.
- DNS settings.
- Routing configuration.

Protect credentials.

Do not place secrets in Kotlin source files or GitHub repositories.

## D. Genuine VPN status

Create these states:

- NOT CONFIGURED
- CONFIGURATION INVALID
- PERMISSION REQUIRED
- CONNECTING
- HANDSHAKE IN PROGRESS
- CONNECTED
- RECONNECTING
- DISCONNECTING
- DISCONNECTED
- CONNECTION FAILED

Only display CONNECTED after genuine tunnel establishment.

Display actual:
- Selected endpoint.
- Connection duration.
- Bytes sent.
- Bytes received.
- Handshake status.
- Measured endpoint latency.
- Error details.

## E. Fail-safe behavior

Handle:

- Missing endpoint.
- Invalid configuration.
- Failed authentication.
- Handshake timeout.
- Internet disconnection.
- Wi-Fi to mobile network changes.
- VPN permission revocation.
- Backend crash.
- Service termination.
- Manual disconnection.

Restore normal network behavior safely.

If a genuine backend cannot be integrated or configured, disable the misleading connect button and display:

VPN PROVIDER CONFIGURATION REQUIRED.

Do not pretend the VPN is operational.

# SECTION 4 — CRITICAL FIX 2: RAM AND PHYSICAL MEMORY

PRIORITY: CRITICAL.

Inspect:

- HardwareMonitor.kt
- MemoryScreen.kt
- NovaMainViewModel.kt
- Memory models and repositories.

## A. Remove incorrect physical RAM fallback

The existing implementation can substitute Runtime.maxMemory() for physical RAM.

This is incorrect.

Runtime.maxMemory() is the application heap limit, not the phone's physical RAM.

Remove this fallback from physical RAM reporting.

Use ActivityManager.MemoryInfo and other appropriate Android system sources.

Display separate values:

- Physical RAM total.
- Physical RAM available.
- Physical RAM usage.
- NovaBoost process memory.
- Memory pressure.
- System swap.
- zRAM.

If a metric is unavailable, display UNAVAILABLE.

Never substitute another metric.

## B. Accurate calculations

Implement and test:

Used RAM = Total physical RAM - Available physical RAM.

Memory usage percentage = Used RAM / Total physical RAM × 100.

Handle invalid or zero values safely.

Do not present application heap size as system RAM.

## C. Memory history

Build a genuine monitoring repository.

Record actual measurements with timestamps.

Implement:

- Current RAM.
- Historical RAM.
- Memory pressure.
- High-memory warning.
- Low-memory warning.
- Monitoring interval controls.

Do not generate synthetic chart values.

## D. Smart Boost correction

The current Smart Boost implementation primarily clears NovaBoost's own cache and requests garbage collection.

Correct the implementation and its naming.

Create separate actions:

1. Analyze Memory.
2. Clear NovaBoost Cache.
3. View Memory Pressure.
4. View Memory History.

Show the actual cache size removed.

Remove artificial minimum values such as:

kb.coerceAtLeast(32)

If zero bytes were removed, report zero.

Do not claim that clearing NovaBoost's own cache cleans the entire phone's RAM.

Do not indiscriminately kill other applications.

# SECTION 5 — CRITICAL FIX 3: zRAM AND SWAP DETECTION

PRIORITY: CRITICAL.

Correct the existing memory detection implementation.

Inspect:

- /proc/meminfo
- /proc/swaps
- /sys/block/zram*
- Accessible zRAM statistics.

## A. Correct zRAM identification

The presence of /sys/block/zram0 alone is not sufficient proof that zRAM is active.

Verify actual active swap entries and identify their device type where possible.

Do not classify ordinary system swap as zRAM.

Do not display SwapTotal as zRAM capacity.

Use distinct models for:

- Physical RAM.
- zRAM configured capacity.
- Active zRAM usage.
- System swap total.
- System swap available.
- Storage-backed swap.
- Unknown swap device.

## B. SWAP file capability detection

Build a real compatibility checker.

Detect:

- Android version.
- Kernel capabilities where accessible.
- Existing swap devices.
- Existing active swap.
- Available storage.
- Filesystem information where accessible.
- Permission restrictions.
- Supported system integration.

## C. SWAP file manager

Implement honest support.

For ordinary no-root Android:

- Detect current swap.
- Detect zRAM.
- Report system capability.
- Do not activate unsupported files.
- Do not claim generated files are active RAM.
- Do not create large useless files.

For a genuinely supported privileged system integration:

- Verify authorization.
- Verify filesystem support.
- Create a user-sized file safely.
- Request authorized activation.
- Verify actual activation through system swap information.
- Monitor real utilization.
- Support safe deactivation.
- Remove files only after confirmed deactivation.

A created file must NEVER be treated as activated swap.

# SECTION 6 — CRITICAL FIX 4: CLEANER ENGINE

PRIORITY: CRITICAL.

Inspect StorageCleaner.kt and CleanerScreen.kt.

The existing cleaner has several important defects.

## A. Fix duplicate counting

The same file can currently appear in multiple categories.

Create one canonical result collection keyed by a stable file identifier, preferably its normalized accessible URI.

Track multiple category labels without adding the same file size more than once.

Calculate total recoverable storage from unique selected files.

Prevent duplicate deletion requests.

## B. Fix deletion verification

The current implementation can return isVerified = true even when some files failed to delete.

Correct the implementation.

Verification rules:

- Every selected item gets its own deletion result.
- Every successful deletion is verified.
- Every failed deletion is recorded.
- Overall success requires all requested deletions to succeed.
- Partial success must be reported as PARTIAL SUCCESS.
- Never mark failed deletions as verified.

## C. Remove hidden cache deletion

The cleaner currently removes NovaBoost's own cache as part of another cleaning operation.

Separate it into an explicit action.

The user must be able to review and choose:

- Selected user files.
- NovaBoost internal cache.
- Other supported removable items.

Never silently delete unselected files.

## D. Improve storage coverage

Use legitimate Android APIs:

- Storage Access Framework.
- DocumentFile.
- MediaStore.
- StorageStatsManager where accessible.

Allow users to select additional directories.

Respect scoped storage.

Do not access private directories of unrelated applications.

## E. Duplicate detection

Keep full-file SHA-256 verification.

Improve:

- Streaming.
- Cancellation.
- Memory efficiency.
- File-access error handling.
- Duplicate-result grouping.
- Rechecking metadata before deletion.

Do not load large files completely into memory.

## F. Safe deletion interface

Required workflow:

SCAN → REVIEW → SELECT → CONFIRM → DELETE → VERIFY.

Default selection must remain empty.

Personal files must never be preselected.

Display exact selected count and size.

Refresh storage after deletion.

Report actual recovered storage only.

# SECTION 7 — CPU MONITORING REPAIR

PRIORITY: HIGH.

Inspect HardwareMonitor.kt and all CPU monitoring code.

Preserve the existing separation between device CPU utilization and NovaBoost process utilization.

Correct the remaining measurement issues.

## A. Sampling synchronization

The CPU sampler is shared by different modules.

Implement one synchronized measurement source.

Prevent concurrent sampling from corrupting measurement intervals.

Use timestamped samples.

Handle counter resets and invalid deltas.

## B. CPU information

Display:

- Architecture.
- ABI.
- Core count.
- CPU model where accessible.
- Per-core frequency where accessible.
- CPU utilization where measurable.
- Process CPU usage.
- Thermal status.

Never fabricate CPU values.

## C. Background processing

Do not block the main thread.

Use suitable CoroutineDispatchers.

Cancel sampling when not needed.

Prevent duplicate monitoring jobs.

# SECTION 8 — GPU PROBE REPAIR

PRIORITY: HIGH.

Inspect GpuProbe.kt.

Remove generic hardware fallbacks that are displayed as actual detected information.

Examples of values that must not be presented as confirmed hardware information without verification:

- System Graphics.
- Generic Android GPU.
- OpenGL ES 2.0+.
- 2048 maximum texture size.

Implement explicit result states.

Collect real GPU information using supported graphics APIs.

Use an appropriate graphics context and thread.

Keep real-time GPU utilization separate from graphics capability detection.

If live GPU utilization is unavailable, clearly display:

REAL-TIME GPU UTILIZATION UNAVAILABLE.

Never fabricate a GPU percentage.

# SECTION 9 — GAMING CENTER REPAIR

PRIORITY: HIGH.

Inspect:

- GamingSessionManager.kt
- Gaming screens.
- Game discovery.
- Gaming profile storage.
- FloatingOverlayService.kt.

Preserve real gaming session functionality.

## A. Gaming session

Verify:

- Start.
- Stop.
- Timer.
- Session recovery.
- Local history.
- Duplicate-session prevention.
- Resource cleanup.

## B. Gaming profiles

Do not treat a stored profile name as an applied optimization.

Separate:

- Saved preferences.
- Applied Android settings.
- Monitoring-only configuration.
- Unsupported optimization.

Every profile must show what it actually changes.

## C. Fix network diagnostic classification

The current implementation can classify a failed TCP connection as packet loss.

This is incorrect.

Separate:

- TCP connection failure.
- DNS failure.
- HTTP failure.
- Measured latency.
- Actual packet loss, when measurable by an appropriate method.

Fix the contradiction where the displayed network status can report GOOD while the diagnostic result says LAGGING.

Use consistent measurement thresholds and labels.

## D. FPS monitoring

Implement FPS monitoring only when a reliable measurement source exists.

Do not create timer-based fake FPS.

Do not invent GPU frame rates.

Display FPS UNAVAILABLE when Android does not expose reliable data.

Do not claim arbitrary external-game FPS improvements.

## E. Floating HUD

Move hardware collection off Dispatchers.Main.

Collect information in the background.

Send only small UI updates to the main dispatcher.

Implement:

- Proper overlay permission.
- Start/stop lifecycle.
- Cancellation.
- Repositioning.
- Minimize.
- Close.
- Battery-conscious update intervals.

The monitoring HUD must stop when the session ends.

# SECTION 10 — STARTUP AND LOADING REPAIR

PRIORITY: HIGH.

Inspect MainActivity.kt and NovaMainViewModel.kt.

The application currently performs expensive initialization when the ViewModel is created.

Move hardware probing and GPU probing into proper background initialization.

## Required startup sequence

1. Display native splash.
2. Initialize local settings.
3. Initialize database.
4. Load cached interface preferences.
5. Initialize hardware detection in background.
6. Initialize GPU probing in background.
7. Load current memory information.
8. Load storage information.
9. Load permission states.
10. Display Home.
11. Continue background monitoring only when appropriate.

Do not block the main thread.

Do not introduce artificial fixed delays.

Provide a fast first render.

Show metric-specific loading states.

# SECTION 11 — AUTO SCAN REPAIR

Inspect NovaMainViewModel.kt and AutoScanScreen.kt.

Replace all incomplete scan orchestration.

Create a proper scan state model.

Each stage must expose:

- ID.
- Name.
- Status.
- Progress, when measurable.
- Start time.
- Completion time.
- Result.
- Error information.

Stages:

1. RAM.
2. zRAM.
3. System swap.
4. Storage.
5. Junk files.
6. Duplicate files.
7. CPU.
8. GPU.
9. Battery.
10. Thermal.
11. Network.
12. Gaming readiness.

Use StateFlow.

Use real progress.

For operations without a measurable progress value, display indeterminate loading.

Do not create fake percentages.

Implement:

- Cancel scan.
- Retry failed scan.
- Preserve completed results.
- Continue independent scan stages after one stage fails.
- Guaranteed state cleanup in finally blocks.
- Error reporting.
- Resource cleanup.

Never delete files during Auto Scan.

# SECTION 12 — BACKGROUND SERVICE REPAIR

Inspect FloatingOverlayService.kt and every monitoring service.

Correct:

- Dispatcher selection.
- Coroutine cancellation.
- Service lifecycle.
- Duplicate jobs.
- Monitoring intervals.
- Resource cleanup.
- Foreground notification behavior where required.
- Android background execution compliance.

Do not run continuous monitoring when no feature requires it.

Do not use unnecessary wake locks.

Do not make NovaBoost consume excessive RAM or CPU.

# SECTION 13 — PREMIUM APPLICATION EXPERIENCE

Preserve existing NovaBoost branding.

Improve the application without replacing its visual identity.

Use:

- Deep dark backgrounds.
- Electric blue accents.
- Cyan highlights.
- Purple secondary accents.
- Premium cards.
- Smooth transitions.
- Clean charts.
- Consistent typography.

Add genuine animations to:

- Application opening.
- Home dashboard.
- Cleaner scans.
- Memory monitoring.
- CPU charts.
- GPU information.
- Gaming sessions.
- VPN connection states.
- Battery states.
- Auto Scan.
- Success and failure feedback.

Animations must reflect actual operations.

Never animate fake data.

Respect lower-end hardware.

# SECTION 14 — FIX RELEASE SIGNING

Inspect:

.github/workflows/build-apk.yml

The current workflow generates a new release keystore using a hardcoded password.

Remove hardcoded signing credentials.

Implement secure signing using GitHub Actions secrets.

Requirements:

- Persistent release keystore.
- Protected keystore password.
- Protected key alias.
- Protected key password.
- No secrets in source code.
- No committing keystore files.
- Stable signing identity across releases.

The same signing key must be used for application updates.

If signing secrets are not configured, produce a debug APK separately.

Never generate a different release identity for every build.

# SECTION 15 — AUTOMATED TESTING

Create meaningful tests for actual functionality.

Required tests:

RAM:
- Total memory.
- Available memory.
- Usage calculation.
- Missing system information.
- Invalid readings.

zRAM:
- Active device identification.
- Inactive device.
- Missing interface.
- Ordinary swap distinction.

Cleaner:
- Duplicate grouping.
- Unique file totals.
- SHA-256.
- Empty directories.
- Large files.
- Partial deletion.
- Failed deletion.
- Successful deletion.
- Cancellation.

CPU:
- Counter calculation.
- Zero interval.
- Counter reset.
- Missing files.
- Concurrent sampling.

GPU:
- Available renderer.
- Missing renderer.
- Unsupported extension.
- Unavailable utilization.

VPN:
- Missing configuration.
- Permission denial.
- Backend initialization failure.
- Actual connection state.
- Actual disconnection.
- No false connected status.

Gaming:
- Start.
- Stop.
- Recovery.
- Timer accuracy.
- History persistence.

Auto Scan:
- Stage progression.
- Failure recovery.
- Cancellation.
- Final state cleanup.

UI:
- Navigation.
- Loading.
- Errors.
- Animations.
- Dialogs.
- Permission revocation.

Do not use fake production measurements.

Test doubles are acceptable inside tests only.

# SECTION 16 — BUILD AND GITHUB ACTIONS

Run:

./gradlew clean

./gradlew testDebugUnitTest

./gradlew lintDebug

./gradlew assembleDebug

Resolve compilation errors.

Resolve dependency conflicts.

Resolve missing imports.

Resolve Kotlin errors.

Resolve manifest problems.

Make tests mandatory.

Do not silently ignore failed tests.

Verify the APK exists.

Calculate its SHA-256.

Upload the artifact only after successful compilation.

Do not claim physical-device testing unless it has actually occurred.

# SECTION 17 — STRICT SCOPE LOCK

Only repair NovaBoost.

Do not add:

- AI chatbot.
- Social network.
- Browser.
- Music player.
- Video player.
- News feed.
- Cryptocurrency.
- Unnecessary accounts.
- Advertising.
- Subscription features.
- Payment gateway.
- Unrequested modules.
- Decorative fake benchmark tools.
- Fake widgets.

Do not introduce unrelated text or features.

Do not redesign every screen unnecessarily.

Keep the current navigation and existing visual identity.

Concentrate on the real implementation of existing features.

# SECTION 18 — FINAL ACCEPTANCE CHECKLIST

[ ] VPN no longer reports a false connected state.

[ ] Genuine VPN backend supported or correctly disabled pending configuration.

[ ] Physical RAM never uses application heap fallback.

[ ] zRAM is accurately identified.

[ ] System swap is separated from zRAM.

[ ] SWAP activation is never falsely reported.

[ ] Smart Boost reports actual results.

[ ] Cleaner totals count unique files.

[ ] Deletion verification is correct.

[ ] No hidden cache deletion.

[ ] Duplicate hashing is accurate.

[ ] CPU sampling is synchronized.

[ ] GPU fallback information is removed.

[ ] Gaming profiles distinguish saved settings from applied settings.

[ ] Incorrect packet-loss reporting is fixed.

[ ] FPS is never fabricated.

[ ] Floating HUD runs expensive operations off the main thread.

[ ] Startup hardware probing runs in the background.

[ ] Auto Scan has genuine progress.

[ ] Auto Scan cancellation works.

[ ] Auto Scan always exits its loading state.

[ ] Release signing is secure and persistent.

[ ] Tests are mandatory.

[ ] Existing project compiles.

[ ] APK builds successfully.

# SECTION 19 — EXECUTION REQUIREMENT

Do not return a list of recommendations and stop.

Perform the actual code repairs.

Work in controlled phases.

After each phase:

1. Modify the actual source files.
2. Inspect the changes.
3. Update dependent classes.
4. Compile affected code.
5. Run relevant tests.
6. Fix newly introduced errors.
7. Continue.

Preserve all existing verified functionality.

Do not replace working code unnecessarily.

If an operation is unavailable on standard Android, implement the legitimate alternative and report it honestly.

At completion, provide:

- Modified file list.
- Created file list.
- Defects fixed.
- Functionality completed.
- Functionality still restricted by Android.
- Tests executed.
- Test results.
- Build status.
- APK location.
- Remaining defects.

# FINAL OBJECTIVE

Deliver a repaired and reliable NovaBoost application.

NO FAKE VPN.
NO FAKE SWAP.
NO FAKE RAM.
NO FAKE CPU/GPU VALUES.
NO FAKE FPS.
NO FALSE CLEANING RESULTS.
NO FAKE LOADING PROGRESS.
NO UNNECESSARY FEATURES.

Preserve the existing application and make its real functionality dependable.