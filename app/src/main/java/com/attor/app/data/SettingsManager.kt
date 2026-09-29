package com.attor.app.data

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("attor_settings", Context.MODE_PRIVATE)

    fun getNightMode(): Int {
        return prefs.getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    fun setNightMode(mode: Int) {
        prefs.edit().putInt(KEY_NIGHT_MODE, mode).apply()
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun getFontSizeScale(): Float {
        return prefs.getFloat(KEY_FONT_SCALE, 1.0f)
    }

    fun setFontSizeScale(scale: Float) {
        prefs.edit().putFloat(KEY_FONT_SCALE, scale).apply()
    }

    companion object {
        private const val KEY_NIGHT_MODE = "night_mode"
        private const val KEY_FONT_SCALE = "font_scale"
    }
}
