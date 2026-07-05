package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val PREFERRED_LANGUAGE = stringPreferencesKey("preferred_language")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val STUDY_GOAL_HOURS = floatPreferencesKey("study_goal_hours")
        val LIFETIME_AD_SKIPS = intPreferencesKey("lifetime_ad_skips")
    }

    val userNameFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_NAME] ?: ""
        }

    val preferredLanguageFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.PREFERRED_LANGUAGE] ?: "en"
        }

    val darkModeFlow: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DARK_MODE]
        }

    val onboardingCompletedFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
        }

    val studyGoalHoursFlow: Flow<Float> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.STUDY_GOAL_HOURS] ?: 2.0f
        }

    val lifetimeAdSkipsFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LIFETIME_AD_SKIPS] ?: 0
        }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }

    suspend fun savePreferredLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PREFERRED_LANGUAGE] = language
        }
    }

    suspend fun saveDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = enabled
        }
    }

    suspend fun saveOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun saveStudyGoalHours(hours: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.STUDY_GOAL_HOURS] = hours
        }
    }

    suspend fun incrementAdSkips(): Boolean {
        var success = false
        context.dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.LIFETIME_AD_SKIPS] ?: 0
            if (current < 2) {
                preferences[PreferencesKeys.LIFETIME_AD_SKIPS] = current + 1
                success = true
            }
        }
        return success
    }

    suspend fun resetAllProgress() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
