package com.attor.app.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SettingsManagerTest {

    private lateinit var context: Context
    private lateinit var settingsManager: SettingsManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        settingsManager = SettingsManager(context)
    }

    @Test
    fun getNightMode_defaultValue_returnsFollowSystem() {
        val mode = settingsManager.getNightMode()
        assertEquals(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, mode)
    }

    @Test
    fun setNightMode_darkMode_persistsValue() {
        settingsManager.setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        val mode = settingsManager.getNightMode()
        assertEquals(AppCompatDelegate.MODE_NIGHT_YES, mode)
    }

    @Test
    fun setNightMode_lightMode_persistsValue() {
        settingsManager.setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val mode = settingsManager.getNightMode()
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, mode)
    }

    @Test
    fun getFontSizeScale_defaultValue_returnsOne() {
        val scale = settingsManager.getFontSizeScale()
        assertEquals(1.0f, scale, 0.01f)
    }

    @Test
    fun setFontSizeScale_large_persistsValue() {
        settingsManager.setFontSizeScale(1.5f)
        val scale = settingsManager.getFontSizeScale()
        assertEquals(1.5f, scale, 0.01f)
    }

    @Test
    fun setFontSizeScale_small_persistsValue() {
        settingsManager.setFontSizeScale(0.8f)
        val scale = settingsManager.getFontSizeScale()
        assertEquals(0.8f, scale, 0.01f)
    }
}
