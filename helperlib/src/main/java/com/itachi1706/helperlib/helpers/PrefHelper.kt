@file:Suppress("unused")

package com.itachi1706.helperlib.helpers

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

/**
 * Wrapper to [PreferenceManager.getDefaultSharedPreferences] without invoking StrictMode Disk Read Policy Violation
 * @receiver Application Context
 * @return SharedPreference singleton object
 */
fun Context.getDefaultSharedPreferencesThreadSafe(): SharedPreferences {
    val old = StrictMode.getThreadPolicy()
    StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder(old).permitDiskReads().build())
    val sp = PreferenceManager.getDefaultSharedPreferences(this)
    StrictMode.setThreadPolicy(old)
    return sp
}

/**
 * Wrapper to [Context.getSharedPreferences] without invoking StrictMode Disk Read Policy Violation
 * @receiver Application Context
 * @param name Name of Shared Preference File
 * @param mode How to open the file in. Either [Context.MODE_PRIVATE], [Context.MODE_WORLD_READABLE], [Context.MODE_WORLD_WRITEABLE] or [Context.MODE_MULTI_PROCESS]
 * @return SharedPreference singleton object
 */
@JvmOverloads
fun Context.getSharedPreferencesThreadSafe(name: String, mode: Int = Context.MODE_PRIVATE): SharedPreferences {
    val old = StrictMode.getThreadPolicy()
    StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder(old).permitDiskReads().build())
    val sp = this.getSharedPreferences(name, mode)
    StrictMode.setThreadPolicy(old)
    return sp
}

/**
 * Check if night mode is enabled
 * @receiver Application Context
 * @return false if Night mode is disabled, true otherwise
 */
fun Context.isNightModeEnabled(): Boolean {
    val currentNightMode =
        this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode != Configuration.UI_MODE_NIGHT_NO
}

object PrefHelper {
    /**
     * Set Night Mode Theme. Options include:
     * [AppCompatDelegate.MODE_NIGHT_NO]
     * [AppCompatDelegate.MODE_NIGHT_YES]
     * [AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM]
     * [AppCompatDelegate.MODE_NIGHT_AUTO]
     * [AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY]
     * @param newTheme New Theme setting
     * @param themeName THeme name for logging purposes
     */
    @JvmStatic
    fun changeDarkModeTheme(newTheme: Int, themeName: String) {
        Log.i("AppThemeChanger", "Switching over to $themeName mode")
        AppCompatDelegate.setDefaultNightMode(newTheme)
    }

    /**
     * Does the handling of theme changes natively
     * @param switchedTheme Either of the values "light", "dark", "battery" or "default"
     */
    @JvmStatic
    fun handleDefaultThemeSwitch(switchedTheme: String) {
        when (switchedTheme) {
            "light" -> changeDarkModeTheme(AppCompatDelegate.MODE_NIGHT_NO, "Light")
            "dark" -> changeDarkModeTheme(AppCompatDelegate.MODE_NIGHT_YES, "Dark")
            "battery" -> changeDarkModeTheme(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY, "Battery Saver")
            "default" -> changeDarkModeTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, "System Default")
            else ->  // Set as battery saver default if P and below
                changeDarkModeTheme(if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                else AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, "Unknown mode, falling back to default")
        }
    }
}