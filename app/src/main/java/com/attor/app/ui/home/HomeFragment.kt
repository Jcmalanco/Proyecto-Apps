package com.attor.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.attor.app.R
import com.attor.app.data.SessionManager
import com.attor.app.data.Work
import com.attor.app.data.WorkFormat
import com.attor.app.data.WorkRepository
import com.attor.app.databinding.FragmentHomeBinding
import com.attor.app.databinding.ItemCarouselSectionBinding
import com.attor.app.ui.profile.ProfileActivity
import com.attor.app.ui.seeall.SeeAllActivity
import com.attor.app.ui.workdetails.WorkDetailsActivity
import com.attor.app.util.GridSpacingItemDecoration
import com.attor.app.util.WorkAdapter
import com.attor.app.util.dpToPx
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: WorkRepository
    private lateinit var session: SessionManager

    private lateinit var continueReadingAdapter: WorkAdapter
    private val sectionAdapters = mutableMapOf<WorkFormat, WorkAdapter>()
    private val sectionBindings = mutableMapOf<WorkFormat, ItemCarouselSectionBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = WorkRepository(requireContext())
        session = SessionManager(requireContext())

        binding.txtUserName.text = if (session.isGuest()) "Invitado" else session.getUserName()

        // El avatar ahora abre el Perfil real (antes era un logout temporal).
        binding.imgAvatar.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }

        binding.swipeRefresh.setOnRefreshListener { loadContent() }

        setupContinueReading()
        buildCategorySections()
        loadContent()
    }

    private fun setupContinueReading() {
        continueReadingAdapter = WorkAdapter(::openDetails)
        binding.rvContinueReading.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvContinueReading.addItemDecoration(
            GridSpacingItemDecoration(requireContext().dpToPx(12), horizontalList = true)
        )
        binding.rvContinueReading.adapter = continueReadingAdapter
    }

    /** Crea las 8 secciones (una por WorkFormat) una sola vez; luego solo se actualizan datos. */
    private fun buildCategorySections() {
        binding.layoutCategorySections.removeAllViews()
        val inflater = LayoutInflater.from(requireContext())

        WorkFormat.values().forEach { format ->
            val sectionBinding = ItemCarouselSectionBinding.inflate(
                inflater, binding.layoutCategorySections, false
            )
            sectionBinding.txtSectionTitle.text = format.displayName
            sectionBinding.txtSeeAll.contentDescription =
                getString(R.string.cd_see_all, format.displayName)

            val adapter = WorkAdapter(::openDetails)
            sectionBinding.rvSection.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            sectionBinding.rvSection.addItemDecoration(
                GridSpacingItemDecoration(requireContext().dpToPx(12), horizontalList = true)
            )
            sectionBinding.rvSection.layoutAnimation =
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
            sectionBinding.rvSection.adapter = adapter

            // "Ver todo" -> pantalla dedicada con el grid completo de esa categoría.
            sectionBinding.txtSeeAll.setOnClickListener {
                SeeAllActivity.start(requireContext(), format)
            }

            sectionAdapters[format] = adapter
            sectionBindings[format] = sectionBinding
            binding.layoutCategorySections.addView(sectionBinding.root)
        }
    }

    private fun loadContent() {
        viewLifecycleOwner.lifecycleScope.launch {
            repository.seedIfEmpty()
            val all = repository.getAll()

            val isGuest = session.isGuest()
            binding.sectionContinueReading.visibility = if (isGuest) View.GONE else View.VISIBLE
            if (!isGuest) {
                continueReadingAdapter.submitList(all.take(3))
            }

            WorkFormat.values().forEach { format ->
                val worksInFormat = all.filter { it.format == format }
                val sectionBinding = sectionBindings[format] ?: return@forEach
                sectionAdapters[format]?.submitList(worksInFormat)

                val hasWorks = worksInFormat.isNotEmpty()
                sectionBinding.rvSection.visibility = if (hasWorks) View.VISIBLE else View.GONE
                sectionBinding.txtSectionEmpty.visibility = if (hasWorks) View.GONE else View.VISIBLE
                if (hasWorks) sectionBinding.rvSection.scheduleLayoutAnimation()
            }

            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun openDetails(work: Work) {
        WorkDetailsActivity.start(requireContext(), work.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
