package com.attor.app.ui.library

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.attor.app.data.SessionManager
import com.attor.app.data.WorkRepository
import com.attor.app.databinding.FragmentLibraryBinding
import com.attor.app.ui.login.LoginActivity
import com.attor.app.ui.workdetails.WorkDetailsActivity
import com.attor.app.util.GridSpacingItemDecoration
import com.attor.app.util.WorkAdapter
import com.attor.app.util.dpToPx
import kotlinx.coroutines.launch

/**
 * Pantalla de biblioteca.
 * ROL INVITADO: se muestra el bloqueo `layoutGuestLock` invitándolo a iniciar sesión.
 * ROL USUARIO: ve su biblioteca (todo el catálogo disponible para leer) desde Room.
 */
class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
        val isGuest = session.isGuest()

        binding.layoutLibraryContent.visibility = if (isGuest) View.GONE else View.VISIBLE
        binding.layoutGuestLock.visibility = if (isGuest) View.VISIBLE else View.GONE

        if (isGuest) {
            binding.btnGuestGoLogin.setOnClickListener {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
            return
        }

        val adapter = WorkAdapter { work -> WorkDetailsActivity.start(requireContext(), work.id) }
        binding.rvLibrary.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvLibrary.addItemDecoration(
            GridSpacingItemDecoration(requireContext().dpToPx(12), spanCount = 2)
        )
        binding.rvLibrary.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            val repository = WorkRepository(requireContext())
            repository.seedIfEmpty()
            adapter.submitList(repository.getAll())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
