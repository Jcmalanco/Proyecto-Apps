package com.attor.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.attor.app.data.SessionManager
import com.attor.app.databinding.ActivityLoginBinding
import com.attor.app.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        // Si ya hay una sesión de usuario activa, saltamos directo a Main.
        if (session.isLoggedIn()) {
            goToMain()
            return
        }

        binding.btnLogin.setOnClickListener { attemptLogin() }
        binding.txtGuestAccess.setOnClickListener { continueAsGuest() }

        // Placeholders de funciones no implementadas en el prototipo.
        binding.txtRegister.setOnClickListener {
            Toast.makeText(this, "Registro próximamente", Toast.LENGTH_SHORT).show()
        }
        binding.btnGoogle.setOnClickListener {
            Toast.makeText(this, "Inicio con Google próximamente", Toast.LENGTH_SHORT).show()
        }
        binding.btnApple.setOnClickListener {
            Toast.makeText(this, "Inicio con Apple próximamente", Toast.LENGTH_SHORT).show()
        }
        binding.txtForgotPassword.setOnClickListener {
            Toast.makeText(this, "Recuperación de contraseña próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun attemptLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Ingresa correo y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        // Prototipo: no hay backend, solo simulamos un login exitoso.
        val userName = email.substringBefore("@").replaceFirstChar { it.uppercase() }
        session.login(userName)
        goToMain()
    }

    private fun continueAsGuest() {
        session.loginAsGuest()
        goToMain()
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
