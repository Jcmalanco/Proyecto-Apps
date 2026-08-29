package com.attor.app.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.attor.app.data.SessionManager
import com.attor.app.databinding.FragmentNotificationsBinding
import com.attor.app.ui.login.LoginActivity
import com.attor.app.util.NotificationAdapter
import com.attor.app.util.SampleData

/**
 * Pantalla de notificaciones. Exclusiva para el rol USUARIO.
 * El ítem del bottom nav ya está oculto para invitados en MainActivity,
 * pero se añade esta guarda extra por si se llega aquí por otra vía
 * (deep link, back stack, etc.).
 */
class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
        if (session.isGuest()) {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
            return
        }

        binding.rvNotifications.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotifications.adapter = NotificationAdapter(SampleData.notifications)

        binding.txtMarkAllRead.setOnClickListener {
            Toast.makeText(requireContext(), "Todas marcadas como leídas", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
