# PROJECT: NOVABOOST
# COMPLETE ANDROID APPLICATION REPAIR & RECONSTRUCTION

## 1. YOUR ROLE

Act as a senior Android development team consisting of:

- Senior Kotlin developers.
- Android OS and framework engineers.
- Linux memory-management specialists.
- Android performance engineers.
- Storage-management engineers.
- Networking and VPN engineers.
- Jetpack Compose specialists.
- Gaming-performance engineers.
- Software testing and quality-assurance engineers.

Your assignment is to completely inspect, repair, optimize and finish the EXISTING NovaBoost Android project.

Repository:

https://github.com/kritikakumari0972-bit/apk-free

This project already exists.

DO NOT create another project.
DO NOT replace the current application with a new template.
DO NOT regenerate a decorative UI prototype.
DO NOT remove existing working functionality.
DO NOT create a new application with different branding.
DO NOT discard existing source files unnecessarily.

Your objective is to transform the existing incomplete NovaBoost project into a genuinely functional, stable, attractive and thoroughly tested native Android application.

The application name must remain:

# NOVABOOST

Tagline:

ONE APP. REAL OPTIMIZATION. COMPLETE DEVICE INTELLIGENCE.

---

# 2. MOST IMPORTANT DEVELOPMENT RULE

The existing project contains several features whose interface suggests more functionality than the underlying implementation actually provides.

Your first responsibility is to examine the actual source code and establish what works.

For every feature, distinguish:

1. Fully functional.
2. Partially functional.
3. Incorrectly implemented.
4. UI-only.
5. Simulated or fabricated.
6. Inaccessible because of Android restrictions.
7. Completely missing.

Then repair each category.

Never mark something as completed merely because its screen exists.

Every button must execute its advertised operation.

Every displayed statistic must have a genuine data source.

Every loading animation must represent an actual operation.

Every success message must be based on an actual successful result.

If an operation is unavailable under standard Android permissions, report the limitation honestly and provide the closest legitimate alternative.

NEVER fake an operation to make the application look complete.

---

# 3. EXISTING PROJECT PRESERVATION

Before changing anything:

1. Inspect the entire repository.
2. Read README.md.
3. Inspect settings.gradle.kts.
4. Inspect root build.gradle.kts.
5. Inspect app/build.gradle.kts.
6. Inspect AndroidManifest.xml.
7. Inspect every Kotlin file.
8. Inspect all Compose screens.
9. Inspect all ViewModels.
10. Inspect services.
11. Inspect repositories and data models.
12. Inspect dependencies.
13. Inspect GitHub Actions.
14. Inspect existing resources and assets.
15. Identify duplicated and unused code.
16. Identify existing working functions.

Create an internal source-file dependency map.

Do not blindly overwrite files.

Preserve the existing package structure where practical.

If a file must be rewritten, preserve its existing public interfaces where possible and update all dependent code consistently.

Use the actual repository paths rather than assuming paths that do not exist.

Candidate files identified during the previous audit include:

- MainActivity.kt
- HardwareMonitor.kt
- GpuProbe.kt
- StorageCleaner.kt
- GamingSessionManager.kt
- NetworkDiagnostics.kt
- NovaMainViewModel.kt
- HomeScreen.kt
- MemoryScreen.kt
- CleanerScreen.kt
- AutoScanScreen.kt
- NetworkVpnScreen.kt
- FloatingOverlayService.kt
- AndroidManifest.xml
- app/build.gradle.kts
- build-apk.yml

Locate each actual file before modifying it.

---

# 4. STRICT TECHNOLOGY REQUIREMENTS

Maintain the existing native Android architecture.

Use:

- Kotlin.
- Jetpack Compose.
- Material 3.
- Android SDK.
- Kotlin Coroutines.
- StateFlow.
- ViewModel.
- Navigation Compose.
- DataStore.
- Room when persistent structured data requires it.
- Android official APIs.
- Android-compatible maintained dependencies.

Preserve the existing minimum SDK unless there is a technically justified reason to change it.

Preserve the current target SDK unless compatibility or policy requirements require an update.

Avoid unnecessary dependencies.

Do not introduce:
- A web-based application.
- React Native.
- Flutter migration.
- An unnecessary backend.
- Unnecessary cloud services.
- Fake native-code libraries.
- Incompatible NDK dependencies.
- Experimental libraries without justification.

The application must remain an Android application.

---

# 5. PHASE ONE: REMOVE ALL FAKE AND FABRICATED DATA

This phase has the highest priority.

Search the entire application for:

- Hardcoded RAM values.
- Hardcoded CPU percentages.
- Hardcoded GPU percentages.
- Hardcoded temperatures.
- Hardcoded battery percentages.
- Hardcoded storage values.
- Fake swap compatibility.
- Fake VPN status.
- Simulated ping.
- Simulated download speeds.
- Simulated upload speeds.
- Fake FPS counters.
- Fake scan results.
- Artificial cleaning results.
- Artificial memory improvement percentages.
- Placeholder success messages.
- Default values presented as real hardware measurements.

Remove these implementations from production code.

Examples of unacceptable production fallbacks:

CPU = 42%

GPU = 38%

Battery = 100%

Temperature = 25°C

VPN = Connected

Swap = Compatible

These values must never appear unless they were actually obtained from the relevant source.

Implement a consistent result-state model:

- LOADING
- AVAILABLE
- UNAVAILABLE
- PERMISSION_REQUIRED
- UNSUPPORTED
- ERROR

If a system file cannot be read, display unavailable.

If the device does not expose a metric, display unavailable.

If access is denied, explain which permission is required.

If hardware does not support something, explain why.

Never substitute a random or generic number.

Create reusable status components for every monitoring module.

---

# 6. PHASE TWO: REPAIR NOVABOOST LOADING SYSTEM

The application currently needs a proper startup experience and genuine loading states.

Implement a polished native Android splash experience.

Requirements:

- Native Android splash-screen integration.
- NovaBoost logo.
- Smooth logo animation.
- Subtle background gradients.
- Proper application initialization.
- Controlled transition to the Home screen.
- No artificial long waiting periods.

The application must not remain on the splash screen unnecessarily.

Do not introduce fake loading delays.

Only display loading while real initialization work is happening.

For each important screen, implement appropriate loading states.

Examples:

Home:
- Hardware data loading.
- Memory information loading.
- Battery information loading.

Cleaner:
- Directory scanning.
- File indexing.
- Duplicate verification.

Memory:
- RAM statistics collection.
- Swap detection.
- zRAM detection.

Gaming:
- Game discovery.
- Session initialization.

Network:
- Connectivity checking.
- Latency testing.
- VPN state verification.

Use actual coroutine states.

Provide retry functionality for failed operations.

---

# 7. PHASE THREE: COMPLETE RAM ENGINE

This module must be rewritten carefully.

Inspect:

- HardwareMonitor.kt
- MemoryScreen.kt
- NovaMainViewModel.kt
- Associated memory data models.

Implement accurate memory information using official Android APIs and accessible Linux information.

## A. Physical RAM monitoring

Use ActivityManager.MemoryInfo where available.

Collect:

- Total physical memory.
- Available memory.
- Memory threshold.
- Low-memory status.
- Current memory-pressure information.
- Usage percentage.

Use consistent calculations.

Do not confuse total physical RAM with available memory.

Refresh measurements using a reasonable interval.

Avoid excessive polling.

## B. CPU and memory reporting

Clearly separate:

- Device-wide memory.
- NovaBoost process memory.
- Available memory.
- Cached memory where identifiable.
- System swap.
- zRAM.

Do not combine these categories.

## C. zRAM detection

Inspect accessible kernel interfaces, including:

- /proc/meminfo
- /proc/swaps
- /sys/block/zram*
- Exposed zRAM statistics.

Check access carefully.

Use SDK-compatible error handling.

Never assume that SwapTotal means zRAM.

Never classify ordinary storage-backed swap as zRAM without identifying its source.

When unavailable, display:

zRAM information unavailable on this device.

When available, show only genuine readings.

## D. CPU memory-pressure monitoring

Create a memory-pressure diagnostic.

Detect:

- Low available memory.
- Android low-memory status.
- High memory consumption.
- Changes during gaming sessions.

Do not claim that the application creates additional physical RAM.

## E. Repair Smart Boost

The existing Smart Boost behavior primarily clears NovaBoost's own cache and requests garbage collection.

Replace misleading messaging.

Create a genuine Smart Memory Diagnostics operation.

It should:

1. Read current system memory.
2. Read NovaBoost process memory.
3. Check memory-pressure indicators.
4. Identify relevant issues.
5. Record results.
6. Show recommendations.
7. Refresh statistics.

Provide a separate option to clear NovaBoost's own temporary cache.

Never claim that this operation clears all Android RAM.

Never force-stop unrelated applications.

---

# 8. PHASE FOUR: REPAIR VIRTUAL SWAP ENGINE

This is an essential module.

Implement accurate swap diagnostics.

Required functionality:

1. Detect Linux swap information.
2. Detect active swap entries.
3. Identify swap device information where accessible.
4. Identify zRAM devices separately.
5. Detect accessible kernel memory information.
6. Display detection errors.
7. Display kernel restrictions.
8. Display actual swap totals.
9. Display actual available swap where accessible.
10. Refresh swap statistics.

IMPORTANT TECHNICAL RESTRICTION:

An ordinary non-root Android app cannot activate arbitrary system-wide Linux swap files because swap activation requires privileged kernel access.

Therefore:

- Never claim that file creation activates swap.
- Never pretend ordinary app-private storage is system swap.
- Never display fabricated SWAP capacity.
- Never execute root commands.
- Never attempt privilege escalation.
- Never generate enormous unused files.
- Never promise that every Android kernel supports external swap.

If genuine storage-backed swap activation cannot be supported, display:

STORAGE SWAP ACTIVATION: NOT AVAILABLE

Explain why.

If existing device swap can be detected, report it accurately.

Create a distinction between:

- Physical RAM.
- Compressed RAM.
- Total kernel swap.
- zRAM.
- Existing storage-backed swap.
- Ordinary generated files.

If a future privileged edition is considered, isolate that implementation from the ordinary no-root edition. Do not weaken the Standard Edition's security.

---

# 9. PHASE FIVE: COMPLETELY REPAIR CLEANER ENGINE

Inspect:

- StorageCleaner.kt
- CleanerScreen.kt
- Related ViewModels.
- Storage models.
- Android manifest permissions.

The current cleaner requires important safety improvements.

## A. File scanning

Implement reliable scanning for user-accessible directories.

Support detection of:

- Temporary files.
- Duplicate files.
- Large files.
- Empty folders.
- Old downloads.
- User-selected APK files.
- Old archives.
- User-selected unnecessary files.

Each result must include:

- Name.
- Location.
- Size.
- File type.
- Last modified date.
- Detection category.
- Deletion eligibility.
- Verification state.

## B. SAF integration

Use Android Storage Access Framework.

Use suitable Android document APIs.

Respect scoped-storage restrictions.

Do not attempt to bypass protected application directories.

Do not access private data belonging to other applications.

Do not request broad storage access unnecessarily.

## C. Fix automatic file selection

This is critical.

The existing implementation can automatically select detected files.

Remove this behavior.

Default selection must be EMPTY.

Users must manually select files.

Every detected file must be reviewable.

Never assume a large file is junk.

Never assume an APK is unwanted.

Never assume an archive is unnecessary.

Never automatically select photos, videos, documents or personal files.

## D. Duplicate detection

The previous implementation uses an incomplete file-sampling approach.

Replace it with reliable duplicate verification.

Process:

1. Compare file sizes.
2. Group candidates with equal sizes.
3. Compute a full-file SHA-256 digest using streaming reads.
4. Verify candidate contents.
5. Exclude files that cannot be read.
6. Handle large files safely.
7. Cancel scanning when requested.
8. Keep memory consumption low.

Do not load entire files into RAM.

Never classify two files as duplicates using only their first 4 KB.

## E. Safe deletion

Implement the following workflow:

SCAN → ANALYZE → REVIEW → SELECT → CONFIRM → DELETE → VERIFY

Do not combine scanning and deletion.

Before deletion:

- Confirm the selected URI still exists.
- Recheck the file metadata.
- Ensure the user selected the item.
- Display the exact count and total selected size.
- Request confirmation.

After deletion:

- Check the deletion result.
- Record successes and failures separately.
- Refresh storage information.
- Report the size of successfully processed items.
- Do not claim a file was deleted if the operation failed.

Never delete Android system files.

Never delete files outside the user's granted scope.

---

# 10. PHASE SIX: REPAIR APPLICATION CACHE MANAGER

This module must not advertise capabilities Android does not provide to an ordinary application.

Implement:

- Installed application listing within supported package-visibility restrictions.
- Application name.
- Application icon.
- Reported storage information where accessible.
- Cache-related information where available.
- Application details navigation.
- Storage usage sorting.
- App Info shortcuts.

For clearing another application's private cache:

Use the official Android App Info/settings workflow.

The user can then use Android's own cache-management controls.

Do not use hidden APIs.

Do not access other applications' private directories.

Do not report successful cache cleaning until Android confirms it.

Create an accurate label:

DIRECT CACHE CLEANING: LIMITED BY ANDROID

For NovaBoost itself, implement genuine internal cache deletion.

---

# 11. PHASE SEVEN: REPAIR CPU ENGINE

Inspect HardwareMonitor.kt and all CPU-related files.

Implement reliable device CPU monitoring.

## Required features

- Processor information.
- CPU core count.
- Supported ABI.
- CPU architecture.
- Available frequency data.
- Per-core frequency information where accessible.
- Device-wide utilization where measurable.
- NovaBoost process CPU usage.
- CPU monitoring history.
- Reading availability status.

## CPU utilization calculation

Where /proc/stat is accessible:

1. Read a CPU statistics sample.
2. Wait for a measured sampling interval.
3. Read another sample.
4. Calculate the difference between idle and total CPU time.
5. Calculate utilization from the delta.
6. Handle counter resets.
7. Handle zero or invalid deltas.
8. Detect read failures.

Never calculate device-wide CPU usage using only the application's thread count.

If system-level readings are unavailable, display:

DEVICE CPU UTILIZATION UNAVAILABLE

Separate this from NovaBoost's own process CPU usage.

## CPU frequency

Read accessible frequency interfaces carefully.

Handle:
- Missing files.
- Access denial.
- Unsupported cores.
- Dynamic frequency changes.

Never display assumed frequencies.

Do not overclock.

Do not change protected CPU governors.

Do not manipulate the kernel.

---

# 12. PHASE EIGHT: REPAIR GPU ENGINE

Inspect GpuProbe.kt and related interfaces.

Preserve valid hardware detection.

Implement:

- GPU renderer.
- GPU vendor.
- OpenGL ES version.
- Graphics extensions.
- Maximum supported texture information where applicable.
- Vulkan availability where reliably detectable.
- Graphics capability reporting.

Run graphics queries on an appropriate thread and context.

Do not block the Compose UI thread.

## Real-time GPU monitoring

Do not manufacture utilization percentages.

If the device provides no accessible GPU utilization interface, report:

REAL-TIME GPU UTILIZATION UNAVAILABLE

Keep these features separate:

1. GPU hardware information.
2. Graphics API compatibility.
3. Rendering capability.
4. Real-time GPU utilization.

Do not claim GPU overclocking.

Do not claim additional GPU cores.

Do not claim additional VRAM.

---

# 13. PHASE NINE: REPAIR GAMING CENTER

Inspect:

- GamingSessionManager.kt
- Game discovery.
- Gaming screens.
- FloatingOverlayService.kt.
- Related ViewModels.

Preserve the genuine session tracking code.

Improve its functionality.

## A. Game discovery

Implement reliable installed-game discovery within Android package-visibility restrictions.

Include:

- Game name.
- Application icon.
- Launch availability.
- Manually added games.
- Remove-from-library action.

Removing a game from NovaBoost must NOT uninstall it.

Do not request QUERY_ALL_PACKAGES without a strong, policy-compliant justification.

## B. Gaming sessions

Implement:

- Start session.
- Stop session.
- Session timer.
- Selected game.
- Session duration.
- Memory measurements.
- CPU measurements.
- Battery information.
- Thermal information.
- Network measurements where available.
- Session history.

Save data locally.

Handle application interruption correctly.

Prevent duplicate active sessions.

Recover from service or process termination gracefully.

## C. Lag diagnostics

Implement a real diagnostic engine.

Evaluate:

- Memory pressure.
- Thermal status.
- CPU load.
- Storage availability.
- Network latency.
- Connectivity.

Generate recommendations from measured results.

Do not use random lag scores.

Do not promise guaranteed FPS increases.

## D. Game profiles

Create real, persistent profiles.

Supported profile settings may include:

- Monitoring frequency.
- Overlay visibility.
- Do Not Disturb preference.
- Performance report generation.
- Battery-conscious monitoring.
- Notification behavior.

Only apply settings supported by Android.

Do not claim to change inaccessible CPU/GPU settings.

## E. Gaming overlay

Repair FloatingOverlayService.kt.

Requirements:

- Appropriate permission flow.
- Movable overlay.
- Accurate data.
- Minimize control.
- Close control.
- Transparency settings.
- Proper service lifecycle.
- No blocking work on the main thread.
- No excessive monitoring.
- No overlay when inactive.

Use Coroutines and lifecycle-safe state management.

Do not run blocking file operations in the UI thread.

Stop monitoring when the gaming session ends.

## F. Do Not Disturb

Use official Android notification policy access.

Check access before applying the mode.

Never silently change user notification settings.

Provide an option to restore the original setting when appropriate.

---

# 14. PHASE TEN: REPAIR VPN SYSTEM COMPLETELY

Inspect:

- NetworkVpnScreen.kt
- AndroidManifest.xml
- NetworkDiagnostics.kt
- All networking services.

The current VPN interface changes a local Boolean to simulate connection state.

REMOVE THAT IMPLEMENTATION.

A local Boolean must never represent a real VPN tunnel.

## Genuine VPN requirements

Use Android's official VpnService architecture.

Requirements:

1. Real VPN service.
2. Proper service declaration.
3. Official permission and user-consent flow.
4. Real tunnel establishment.
5. Genuine configured VPN endpoint.
6. Genuine compatible VPN protocol implementation.
7. Secure protocol handling.
8. Real connection status.
9. Real disconnect handling.
10. Error handling.
11. Reconnection handling.
12. Lifecycle management.

Do not implement custom encryption.

Do not invent servers.

Do not claim VPN protection without an established tunnel.

If a maintained VPN backend is unavailable or incompatible with the project, do not pretend the VPN is complete.

Keep the feature in a clearly labelled configuration-required state until the user supplies a legitimate provider/server configuration.

Display:

VPN NOT CONFIGURED

or

VPN CONNECTION FAILED

when appropriate.

The VPN connection state must be derived from the actual service/tunnel lifecycle, never from a decorative interface toggle.

---

# 15. PHASE ELEVEN: REPAIR NETWORK DIAGNOSTICS

Inspect NetworkDiagnostics.kt.

Replace misleading network tests.

## Latency

Distinguish between:

- DNS resolution time.
- TCP connection time.
- HTTP response time.
- Game-server latency, when a real game-server endpoint is available.

Do not label all measurements as ping.

## Speed testing

Implement genuine download and upload tests.

Use a legitimate, configurable test endpoint.

Measure transferred bytes and elapsed time.

Account for HTTP errors.

Show:
- Measurement endpoint.
- Actual download speed.
- Actual upload speed.
- Test duration.
- Connection type.
- Failed-test status.

Never invent upload speed.

## DNS testing

Implement genuine DNS lookup testing.

Measure the lookup operation.

Distinguish DNS failure from general internet disconnection.

## Network stability

Where measurable, record:

- Successful requests.
- Failed requests.
- Latency variation.
- Connection changes.
- Packet loss only when using a measurement method that actually supports it.

Do not fabricate ICMP ping results from TCP connections.

---

# 16. PHASE TWELVE: REPAIR AUTO SCAN

Inspect AutoScanScreen.kt and NovaMainViewModel.kt.

The existing scan architecture needs proper progressive updates.

Replace the current all-at-once completion behavior.

Create a structured scanning engine.

Each scan stage must have its own state.

Required stages:

1. RAM diagnostics.
2. Storage analysis.
3. Temporary-file scan.
4. Duplicate detection.
5. Large-file scan.
6. CPU information.
7. GPU information.
8. Battery information.
9. Thermal status.
10. Network status.

For each stage:

- Set LOADING.
- Perform the real operation.
- Update progress based on actual work.
- Store the result.
- Set SUCCESS, WARNING, UNAVAILABLE or ERROR.
- Display completion time.

Use a StateFlow-backed progress model.

Do not use fixed delays to simulate scanning.

If a scan is cancelled:
- Stop eligible scanning operations.
- Preserve completed results.
- Mark the remaining stages cancelled.
- Release resources.

If one scan stage fails, continue other independent stages.

At completion display:

- Successfully completed checks.
- Failed checks.
- Unavailable checks.
- Detected issues.
- Actual recommended actions.
- Total scan duration.

The scan must never delete files automatically.

---

# 17. PHASE THIRTEEN: REPAIR BACKGROUND SERVICES

Inspect FloatingOverlayService.kt and all services.

Current monitoring must be more resource-efficient.

Requirements:

- Correct service lifecycle.
- No main-thread blocking.
- Proper coroutine cancellation.
- No duplicated sampling jobs.
- No unnecessary wake locks.
- No permanent service without justification.
- No continuous monitoring when inactive.
- Proper cleanup when sessions end.
- Handle Android process recreation.
- Handle permission revocation.
- Respect Android background execution restrictions.

Use foreground services only when genuinely required and permitted.

On Android versions that enforce foreground-service types and permissions, implement the correct declarations.

Do not add unnecessary background services simply to display a permanent notification.

---

# 18. PHASE FOURTEEN: REPAIR BATTERY AND THERMAL MONITORING

Preserve actual BatteryManager and PowerManager integrations.

Implement:

- Battery percentage.
- Charging state.
- Battery temperature.
- Voltage where exposed.
- Battery health information where exposed.
- Thermal status.
- Monitoring history.

Use SDK-version checks.

Distinguish battery temperature from thermal-zone temperature.

Handle unavailable readings.

Do not invent battery health percentages.

Do not claim software can physically cool the battery.

Do not interfere with Android thermal protection.

When the system reports a serious thermal status, provide appropriate warnings and pause unnecessary NovaBoost monitoring.

---

# 19. PHASE FIFTEEN: COMPLETE LOADING ANIMATIONS AND UI POLISH

Preserve NovaBoost's existing premium identity.

Do not unnecessarily redesign the entire interface.

Improve visual quality while keeping the existing screens, branding and navigation recognizable.

Use the established visual direction:

- Deep midnight background.
- Graphite surfaces.
- Electric blue accents.
- Cyan highlights.
- Purple secondary gradients.
- Premium cards.
- Clear typography.
- Restrained glowing effects.
- Smooth micro-interactions.

## Implement real animations

Home:
- Smooth dashboard entry.
- Animated health indicator.
- Live statistic transitions.
- Animated chart updates.

Cleaner:
- Genuine scanning animation.
- File-discovery progress.
- Animated category completion.
- Successful deletion feedback.

Memory:
- Smooth memory graphs.
- Animated metric transitions.
- Real-time data updates.

CPU/GPU:
- Animated charts connected to actual measurements.
- Smooth tab switching.
- Loading and unavailable states.

Gaming:
- Smooth gaming session entry.
- Session timer.
- Overlay appearance and disappearance.
- Session completion animation.

Network:
- Real connection testing animation.
- Progress indication.
- Clear failure and reconnection states.

Settings:
- Smooth selection transitions.
- Proper switch feedback.

## Animation requirements

Use appropriate Compose animation APIs.

Examples:
- AnimatedVisibility.
- AnimatedContent.
- animate*AsState.
- updateTransition.
- Canvas animations where justified.

Animations must:
- Be smooth.
- Avoid excessive CPU use.
- Avoid constant unnecessary recomposition.
- Respect reduced-motion preferences where possible.
- Stop when no longer visible.
- Never block actual operations.

Do not animate fake statistics.

Do not make loading indicators continue after an operation has finished.

---

# 20. PHASE SIXTEEN: CLEAN ARCHITECTURE AND STATE MANAGEMENT

Review the entire source architecture.

Avoid placing all business logic inside composables.

Use:

- ViewModels for screen state.
- Repositories for data access.
- Dedicated monitoring services where necessary.
- Data classes for results.
- Coroutines for background work.
- StateFlow for observable state.
- DataStore for settings.
- Room for structured history where required.

Separate UI from business logic.

Create reusable result types.

Example conceptual model:

HardwareResult<T>:

- Loading
- Success(value)
- Unavailable(reason)
- PermissionRequired(permission)
- Error(message)

Do not use fake default values to satisfy non-null UI fields.

Make state transitions explicit.

Prevent duplicated requests.

Prevent race conditions.

Handle cancellation properly.

Use structured concurrency.

Avoid GlobalScope.

Avoid unnecessary singleton state.

---

# 21. PHASE SEVENTEEN: PERMISSION AND PRIVACY AUDIT

Inspect AndroidManifest.xml and every permission request.

Remove unused permissions.

Request permissions only when the user opens the associated feature.

Examples:

- Usage access for optional app-usage/game detection.
- Overlay access for the optional floating HUD.
- Notification permission when required.
- Do Not Disturb access when the user enables the corresponding feature.
- Storage Access Framework access for user-selected folders.
- Internet permission for actual network features.
- VPN permission through the official VPN authorization flow.

Do not silently request all permissions during startup.

Do not access private data of other applications.

Do not upload personal files.

Do not create hidden analytics.

Do not request root access in the Standard Edition.

Provide a proper permission explanation screen.

Handle permanently denied or revoked permissions.

---

# 22. PHASE EIGHTEEN: DATABASE AND PERSISTENCE

Inspect existing database code.

Implement reliable persistence for:

- User settings.
- Gaming session history.
- Scan history.
- Cleaning history.
- Monitoring preferences.
- Game profiles.

Use migrations when the database schema changes.

Avoid deleting existing user records during upgrades.

Do not store unnecessary sensitive information.

Handle database errors.

Prevent duplicate history records.

Allow users to clear NovaBoost's own history.

---

# 23. PHASE NINETEEN: PERFORMANCE OPTIMIZATION

The application must work efficiently on low-end Android devices.

Primary test target:

Samsung Galaxy M01:
- Android 12.
- Approximately 3 GB RAM.
- Entry-level hardware.

Optimize:

- Startup time.
- Compose recomposition.
- Memory usage.
- CPU usage.
- Battery consumption.
- Monitoring frequency.
- Scanning operations.
- Database queries.
- File hashing.
- Image assets.
- Overlay service.

Use background dispatchers for expensive operations.

Use streaming file operations.

Avoid large in-memory lists when scanning directories.

Paginate or limit large file results.

Cancel scans when the user exits or requests cancellation.

Do not continuously poll inaccessible system files.

Avoid unnecessary animations on lower-end hardware.

The booster itself must not become a major source of device lag.

---

# 24. PHASE TWENTY: ERROR HANDLING

Every module must handle:

- Missing permissions.
- Unsupported Android versions.
- Restricted kernel interfaces.
- Missing files.
- File access failures.
- Network disconnection.
- Storage permission revocation.
- Empty results.
- Cancelled operations.
- Background service termination.
- Unexpected exceptions.
- Invalid user input.

Create friendly error states.

Never silently swallow important errors.

Never display success when the underlying operation failed.

Provide retry buttons where appropriate.

Keep technical diagnostic details available without overwhelming normal users.

---

# 25. PHASE TWENTY-ONE: COMPLETE TESTING

Testing is mandatory.

Do not stop after writing code.

## A. Compilation tests

Run:

./gradlew clean

./gradlew testDebugUnitTest

./gradlew lintDebug

./gradlew assembleDebug

Resolve all compilation errors.

Resolve missing dependencies.

Resolve manifest errors.

Resolve Kotlin errors.

Resolve Compose errors.

Resolve resource errors.

Resolve Gradle configuration problems.

## B. RAM tests

Verify:

- Correct total-memory parsing.
- Correct available-memory parsing.
- Correct percentage calculation.
- Swap detection.
- zRAM identification.
- Restricted-file handling.
- Correct unavailable states.

## C. Cleaner tests

Verify:

- Empty directory scan.
- Large file handling.
- Duplicate detection.
- Full SHA-256 verification.
- Permission denial.
- Cancelled scan.
- Manual file selection.
- No automatic personal-file deletion.
- Successful deletion.
- Failed deletion.
- Storage refresh.

## D. CPU/GPU tests

Verify:
- Missing sysfs paths.
- Permission-denied paths.
- Invalid counters.
- CPU counter changes.
- GPU information availability.
- Missing Vulkan support.
- Missing utilization data.

## E. Gaming tests

Verify:
- Game discovery.
- Game launch.
- Session start.
- Session stop.
- Session recovery.
- Timer accuracy.
- Session persistence.
- Monitoring cancellation.
- Overlay permission denial.
- Overlay termination.

## F. VPN tests

Verify:
- Missing configuration.
- Permission request.
- Connection failure.
- Real connection state.
- Disconnection.
- Service termination.

A simulated connection is never an acceptable test result.

## G. Network tests

Verify:
- Working internet.
- No internet.
- DNS failure.
- TCP latency timeout.
- HTTP errors.
- Download failure.
- Upload failure.
- Connection changes.

## H. UI tests

Verify:
- Navigation.
- Screen loading.
- Loading completion.
- Animation cancellation.
- Button functionality.
- Empty states.
- Error states.
- Dialogs.
- Responsive layouts.

Use test doubles only in unit or instrumentation tests.

Never ship test fixtures as production hardware measurements.

---

# 26. PHASE TWENTY-TWO: FIX GITHUB ACTIONS

Inspect:

.github/workflows/build-apk.yml

Repair the build workflow.

Requirements:

1. Correct JDK version.
2. Correct Android SDK setup.
3. Reliable Gradle caching.
4. Dependency resolution.
5. Unit tests.
6. Lint.
7. Debug APK build.
8. APK verification.
9. Artifact upload.
10. Build failure reporting.

CRITICAL:

Remove any configuration that silently ignores test failures.

Remove unnecessary:

continue-on-error: true

Remove:

|| true

from mandatory test/build commands.

A failed required test must produce a failed workflow.

Do not report a successful build when compilation failed.

Upload the APK only after successful compilation.

Generate an actual SHA-256 checksum for the resulting APK.

Do not include signing credentials in source files.

Do not publish an improperly signed release.

---

# 27. PHASE TWENTY-THREE: RELEASE QUALITY

Before delivery:

- Check package ID.
- Check app name.
- Check icons.
- Check Android manifest.
- Check application theme.
- Check version code.
- Check version name.
- Check dependency versions.
- Check crash behavior.
- Check permissions.
- Check startup.
- Check storage access.
- Check battery consumption.
- Check low-memory behavior.

Ensure the application can install and launch on the target Android version.

Do not claim physical-device testing unless it has actually been performed.

If only emulator testing is possible, clearly identify it as emulator testing.

If a build cannot be executed in the current environment, report that honestly.

---

# 28. STRICT SCOPE LOCK

You MUST repair NovaBoost's existing functionality.

Do not introduce unrelated modules.

Do not expand the application beyond its intended scope.

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
- Ads.
- Subscription system.
- Payment gateway.
- Mandatory login.
- Unnecessary cloud services.
- Unrequested widgets.
- Unnecessary onboarding.
- Fake benchmarking.
- Fake system optimization.

Do not add unnecessary text.

Do not generate excessive documentation instead of implementing fixes.

Do not replace source code with descriptions.

Do not create placeholder buttons.

Do not create fake APIs.

Do not add features simply to make the application appear larger.

Use the existing application screens and repair their functionality.

---

# 29. FINAL ACCEPTANCE CHECKLIST

The project cannot be considered complete until the following are verified:

[ ] Existing project preserved.

[ ] No unnecessary project recreation.

[ ] No fabricated hardware data.

[ ] Proper native splash screen.

[ ] Proper loading states.

[ ] Real incremental scan progress.

[ ] Physical RAM monitoring corrected.

[ ] zRAM identification corrected.

[ ] Swap diagnostics corrected.

[ ] No fake swap activation.

[ ] Smart Boost accurately renamed or implemented.

[ ] Cleaner scanning corrected.

[ ] Automatic file selection removed.

[ ] Full-file duplicate verification implemented.

[ ] Safe deletion implemented.

[ ] Deletion results verified.

[ ] Application cache restrictions communicated.

[ ] CPU utilization calculation corrected.

[ ] CPU frequency detection handled safely.

[ ] GPU capability detection corrected.

[ ] Fake GPU utilization removed.

[ ] Gaming library functional.

[ ] Gaming sessions functional.

[ ] Session history persistent.

[ ] Gaming profiles functional within Android permissions.

[ ] Lag diagnostics based on actual measurements.

[ ] Floating overlay lifecycle corrected.

[ ] Fake VPN state removed.

[ ] Genuine VPN architecture implemented where a real backend exists.

[ ] Network latency labels corrected.

[ ] Genuine DNS diagnostics implemented.

[ ] Download testing functional.

[ ] Upload testing functional.

[ ] Battery and thermal reporting corrected.

[ ] Background tasks optimized.

[ ] Permissions reviewed.

[ ] Animations implemented.

[ ] Error handling improved.

[ ] Database migrations checked.

[ ] GitHub Actions repaired.

[ ] Tests mandatory.

[ ] Debug APK builds successfully.

[ ] No critical lint or compilation errors.

---

# 30. FINAL EXECUTION INSTRUCTIONS

Do not stop after analyzing the repository.

Do not return only a list of suggested changes.

Do not generate a new mock application.

Do not ask the user to rewrite the project from scratch.

Perform the actual source-code modifications.

Execute the work in controlled phases.

After each phase:

1. Save the changes.
2. Inspect modified files.
3. Check affected imports.
4. Check dependent functions.
5. Run relevant tests.
6. Resolve errors.
7. Continue to the next phase.

Do not leave incomplete functions or unexplained TODOs.

Do not mark unavailable capabilities as completed.

If an Android restriction prevents a requested operation, implement the correct supported alternative and document the limitation within the relevant feature.

Preserve all valid functionality that already works.

Avoid unnecessary changes to unrelated modules.

At the end, provide a concise engineering report containing:

- Files modified.
- Files created.
- Major defects fixed.
- Actual features completed.
- Features limited by Android.
- Tests executed.
- Test results.
- Build status.
- APK artifact location, if successfully generated.
- Remaining issues, if any.

Never claim that work was tested when it was not.

# FINAL OBJECTIVE

Transform the existing project into:

# NOVABOOST

A premium, stable and genuinely functional Android phone optimization application.

The application must provide real:

- RAM monitoring.
- zRAM diagnostics.
- Swap diagnostics.
- Safe storage cleaning.
- Application storage analysis.
- CPU monitoring.
- GPU diagnostics.
- Gaming session management.
- Gaming performance diagnostics.
- Floating performance monitoring.
- Network diagnostics.
- Genuine VPN functionality when properly configured.
- Battery monitoring.
- Thermal monitoring.
- Animated loading and scanning.
- Reliable background processing.

The application must be lightweight enough for lower-end Android phones.

Every feature must be implemented, tested and honestly reported.

NO FAKE STATISTICS.
NO FAKE OPERATIONS.
NO DECORATIVE FUNCTIONALITY.
NO UNRELATED FEATURES.

BUILD AND REPAIR THE EXISTING NOVABOOST PROJECT.