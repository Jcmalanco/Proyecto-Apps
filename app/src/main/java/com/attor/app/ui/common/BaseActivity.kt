package com.attor.app.ui.common

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.attor.app.data.SettingsManager

open class BaseActivity : AppCompatActivity() {
    override fun getResources() = super.getResources().also { res ->
        val settings = SettingsManager(this)
        val config = Configuration(res.configuration)
        config.fontScale = settings.getFontSizeScale()
        res.updateConfiguration(config, res.displayMetrics)
    }
}
