package com.attor.app.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.attor.app.R
import com.attor.app.data.SessionManager
import com.attor.app.data.WorkRepository
import com.attor.app.databinding.ActivityMainBinding
import com.attor.app.ui.common.BaseActivity
import com.attor.app.ui.create.CreateWorkFragment
import com.attor.app.ui.home.HomeFragment
import com.attor.app.ui.library.LibraryFragment
import com.attor.app.ui.notifications.NotificationsFragment
import com.attor.app.ui.search.SearchFragment
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var session: SessionManager
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        val isGuest = session.isGuest()

        // Primera carga: siembra la base de datos local con el catálogo de ejemplo.
        lifecycleScope.launch { WorkRepository(this@MainActivity).seedIfEmpty() }

        // ROL INVITADO: no puede crear contenido ni ver notificaciones personales.
        binding.bottomNav.menu.findItem(R.id.nav_create).isVisible = !isGuest
        binding.bottomNav.menu.findItem(R.id.nav_notifications).isVisible = !isGuest

        binding.bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_search -> SearchFragment()
                R.id.nav_create -> CreateWorkFragment()
                R.id.nav_library -> LibraryFragment()
                R.id.nav_notifications -> NotificationsFragment()
                else -> HomeFragment()
            }
            showFragment(fragment)
            true
        }

        if (savedInstanceState == null) {
            showFragment(HomeFragment())
        }
    }

    /** Cambia de pantalla con una pequeña transición (feedback visual al navegar). */
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}
