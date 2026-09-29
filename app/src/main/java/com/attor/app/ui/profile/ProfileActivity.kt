package com.attor.app.ui.profile

import android.content.Intent
import android.os.Bundle
import com.attor.app.data.SessionManager
import com.attor.app.databinding.ActivityProfileBinding
import com.attor.app.ui.common.BaseActivity
import com.attor.app.ui.login.LoginActivity
import com.attor.app.ui.settings.SettingsActivity

class ProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        binding.txtName.text = session.getUserName()
        binding.txtEmail.text = session.getUserEmail()

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            session.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
