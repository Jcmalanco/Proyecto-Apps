package com.attor.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.attor.app.R
import com.attor.app.data.SessionManager
import com.attor.app.data.UserRepository
import com.attor.app.databinding.ActivityLoginBinding
import com.attor.app.ui.common.BaseActivity
import com.attor.app.ui.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager
    private lateinit var userRepository: UserRepository

    private var isRegisterMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        userRepository = UserRepository(this)

        if (session.isLoggedIn()) {
            goToMain()
            return
        }

        setupClickListeners()
        updateUIForMode()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            if (isRegisterMode) attemptRegister() else attemptLogin()
        }

        binding.txtRegister.setOnClickListener {
            isRegisterMode = !isRegisterMode
            updateUIForMode()
        }

        binding.txtGuestAccess.setOnClickListener {
            continueAsGuest()
        }

        binding.txtForgotPassword.setOnClickListener {
            Toast.makeText(this, "Recuperación de contraseña próximamente", Toast.LENGTH_SHORT).show()
        }

        binding.btnGoogle.setOnClickListener {
            Toast.makeText(this, "Inicio con Google próximamente", Toast.LENGTH_SHORT).show()
        }

        binding.btnApple.setOnClickListener {
            Toast.makeText(this, "Inicio con Apple próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUIForMode() {
        if (isRegisterMode) {
            binding.btnLogin.text = getString(R.string.login_button_register)
            binding.txtRegister.text = getString(R.string.login_have_account)
            binding.txtForgotPassword.alpha = 0.4f
        } else {
            binding.btnLogin.text = getString(R.string.login_button)
            binding.txtRegister.text = getString(R.string.login_register)
            binding.txtForgotPassword.alpha = 1.0f
        }
    }

    private fun attemptLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Ingresa correo y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        if (!email.contains("@")) {
            Toast.makeText(this, "El correo debe contener al menos una @", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val user = userRepository.login(email, password)
            if (user != null) {
                session.login(user.name, user.email)
                goToMain()
            } else {
                Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun attemptRegister() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Ingresa correo y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        if (!email.contains("@")) {
            Toast.makeText(this, "El correo debe contener al menos una @", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }

        lifecycleScope.launch {
            val success = userRepository.register(name, email, password)
            if (success) {
                session.login(name, email)
                Toast.makeText(this@LoginActivity, "¡Cuenta creada!", Toast.LENGTH_SHORT).show()
                goToMain()
            } else {
                Toast.makeText(this@LoginActivity, "Este correo ya está registrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun continueAsGuest() {
        session.loginAsGuest()
        goToMain()
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
