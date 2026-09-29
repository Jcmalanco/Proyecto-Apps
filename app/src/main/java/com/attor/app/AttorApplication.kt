package com.attor.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.attor.app.data.SettingsManager

class AttorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val settings = SettingsManager(this)
        AppCompatDelegate.setDefaultNightMode(settings.getNightMode())
    }
}
