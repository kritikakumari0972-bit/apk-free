package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "novaboost_settings")

class PreferencesManager(private val context: Context) {

    companion object {
        val KEY_TEMP_THRESHOLD = floatPreferencesKey("temp_warning_threshold")
        val KEY_AUTO_REFRESH_SEC = intPreferencesKey("auto_refresh_seconds")
        val KEY_OVERLAY_ENABLED = booleanPreferencesKey("gaming_overlay_enabled")
        val KEY_DND_GAMING = booleanPreferencesKey("gaming_dnd_enabled")
        val KEY_DARK_THEME = booleanPreferencesKey("app_dark_theme")
        val KEY_CUSTOM_GAMES = stringPreferencesKey("custom_games_list")
        val KEY_BATTERY_PROFILE = stringPreferencesKey("battery_profile_mode")
        val KEY_GAMING_PROFILE = stringPreferencesKey("gaming_profile_mode")
    }

    val tempThresholdFlow: Flow<Float> = context.dataStore.data.map { prefs ->
        prefs[KEY_TEMP_THRESHOLD] ?: 42.0f
    }

    val autoRefreshSecFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_AUTO_REFRESH_SEC] ?: 3
    }

    val overlayEnabledFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_OVERLAY_ENABLED] ?: false
    }

    val dndGamingFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_DND_GAMING] ?: false
    }

    val darkThemeFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_DARK_THEME] ?: true
    }

    val customGamesFlow: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        val raw = prefs[KEY_CUSTOM_GAMES] ?: ""
        if (raw.isBlank()) emptySet() else raw.split(",").toSet()
    }

    suspend fun setTempThreshold(value: Float) {
        context.dataStore.edit { it[KEY_TEMP_THRESHOLD] = value }
    }

    suspend fun setAutoRefreshSec(value: Int) {
        context.dataStore.edit { it[KEY_AUTO_REFRESH_SEC] = value }
    }

    suspend fun setOverlayEnabled(value: Boolean) {
        context.dataStore.edit { it[KEY_OVERLAY_ENABLED] = value }
    }

    suspend fun setDndGaming(value: Boolean) {
        context.dataStore.edit { it[KEY_DND_GAMING] = value }
    }

    suspend fun setDarkTheme(value: Boolean) {
        context.dataStore.edit { it[KEY_DARK_THEME] = value }
    }

    val batteryProfileFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_BATTERY_PROFILE] ?: "Balanced"
    }

    val gamingProfileFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_GAMING_PROFILE] ?: "Balanced Gaming"
    }

    suspend fun setBatteryProfile(profile: String) {
        context.dataStore.edit { it[KEY_BATTERY_PROFILE] = profile }
    }

    suspend fun setGamingProfile(profile: String) {
        context.dataStore.edit { it[KEY_GAMING_PROFILE] = profile }
    }

    suspend fun addCustomGame(packageName: String) {
        context.dataStore.edit { prefs ->
            val current = (prefs[KEY_CUSTOM_GAMES] ?: "").split(",").filter { it.isNotBlank() }.toMutableSet()
            current.add(packageName)
            prefs[KEY_CUSTOM_GAMES] = current.joinToString(",")
        }
    }

    suspend fun removeCustomGame(packageName: String) {
        context.dataStore.edit { prefs ->
            val current = (prefs[KEY_CUSTOM_GAMES] ?: "").split(",").filter { it.isNotBlank() }.toMutableSet()
            current.remove(packageName)
            prefs[KEY_CUSTOM_GAMES] = current.joinToString(",")
        }
    }
}
