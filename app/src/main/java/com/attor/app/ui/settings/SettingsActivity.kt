package com.attor.app.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.attor.app.data.SettingsManager
import com.attor.app.databinding.ActivitySettingsBinding
import com.attor.app.ui.common.BaseActivity

class SettingsActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settings: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settings = SettingsManager(this)

        binding.switchDarkMode.isChecked = settings.getNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            settings.setNightMode(mode)
        }
    }
}
