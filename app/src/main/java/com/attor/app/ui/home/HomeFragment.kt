package com.attor.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.attor.app.data.SessionManager
import com.attor.app.databinding.FragmentHomeBinding
import com.attor.app.ui.workdetails.WorkDetailsActivity
import com.attor.app.util.SampleData
import com.attor.app.util.WorkAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
        val isGuest = session.isGuest()

        // ROL INVITADO: sin historial de lectura -> ocultamos "Sigue leyendo".
        binding.sectionContinueReading.visibility = if (isGuest) View.GONE else View.VISIBLE
        binding.txtUserName.text = if (isGuest) "Invitado" else session.getUserName()

        val openDetails = { work: com.attor.app.data.Work ->
            WorkDetailsActivity.start(requireContext(), work)
        }

        if (!isGuest) {
            binding.rvContinueReading.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvContinueReading.adapter = WorkAdapter(openDetails).apply {
                submitList(SampleData.continueReading)
            }
        }

        binding.rvTrending.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvTrending.adapter = WorkAdapter(openDetails).apply {
            submitList(SampleData.trending)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
