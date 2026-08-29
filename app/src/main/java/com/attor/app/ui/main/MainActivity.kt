package com.attor.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.attor.app.R
import com.attor.app.data.SessionManager
import com.attor.app.databinding.ActivityMainBinding
import com.attor.app.ui.create.CreateWorkFragment
import com.attor.app.ui.home.HomeFragment
import com.attor.app.ui.library.LibraryFragment
import com.attor.app.ui.notifications.NotificationsFragment
import com.attor.app.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var session: SessionManager
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        val isGuest = session.isGuest()

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

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}
