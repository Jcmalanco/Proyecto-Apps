package com.attor.app.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.attor.app.R
import com.attor.app.data.Work
import com.attor.app.data.WorkFormat
import com.attor.app.data.WorkRepository
import com.attor.app.databinding.FragmentSearchBinding
import com.attor.app.ui.workdetails.WorkDetailsActivity
import com.attor.app.util.GridSpacingItemDecoration
import com.attor.app.util.WorkAdapter
import com.attor.app.util.dpToPx
import kotlinx.coroutines.launch

/** Pantalla de búsqueda/exploración. Accesible sin restricciones para ambos roles. */
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: WorkRepository
    private lateinit var adapter: WorkAdapter
    private var activeFilter: WorkFormat? = null
    private var query: String = ""
    private var allWorks: List<Work> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = WorkRepository(requireContext())
        adapter = WorkAdapter { work -> WorkDetailsActivity.start(requireContext(), work.id) }

        binding.rvSearchResults.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvSearchResults.addItemDecoration(
            GridSpacingItemDecoration(requireContext().dpToPx(12), spanCount = 2)
        )
        binding.rvSearchResults.adapter = adapter

        buildFilterChips()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {
                query = s?.toString().orEmpty()
                applyFilters()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        viewLifecycleOwner.lifecycleScope.launch {
            repository.seedIfEmpty()
            allWorks = repository.getAll()
            applyFilters()
        }
    }

    /** Genera un chip "Todo" + uno por cada WorkFormat, sin repetir XML. */
    private fun buildFilterChips() {
        binding.layoutFilters.removeAllViews()
        val chips = mutableListOf<TextView>()

        fun addChip(label: String, format: WorkFormat?) {
            val textView = TextView(requireContext()).apply {
                text = label
                setPadding(
                    requireContext().dpToPx(16), requireContext().dpToPx(8),
                    requireContext().dpToPx(16), requireContext().dpToPx(8)
                )
                textSize = 13f
                setOnClickListener {
                    activeFilter = format
                    highlightSelectedChip(this, chips)
                    applyFilters()
                }
            }
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.marginEnd = requireContext().dpToPx(8)
            textView.layoutParams = params
            chips.add(textView)
            binding.layoutFilters.addView(textView)
        }

        addChip(getString(R.string.filter_all), null)
        WorkFormat.values().forEach { format -> addChip(format.displayName, format) }

        if (chips.isNotEmpty()) highlightSelectedChip(chips.first(), chips)
    }

    private fun highlightSelectedChip(selected: TextView, allChips: List<TextView>) {
        allChips.forEach { chip ->
            val isSelected = chip == selected
            chip.setBackgroundResource(
                if (isSelected) R.drawable.bg_chip_selected else R.drawable.bg_chip_unselected
            )
            chip.setTextColor(
                ContextCompat.getColor(
                    requireContext(), if (isSelected) R.color.white else R.color.text_secondary
                )
            )
        }
    }

    private fun applyFilters() {
        val results: List<Work> = allWorks.filter { work ->
            val matchesFormat = activeFilter == null || work.format == activeFilter
            val matchesQuery = query.isBlank() ||
                work.title.contains(query, ignoreCase = true) ||
                work.author.contains(query, ignoreCase = true)
            matchesFormat && matchesQuery
        }
        adapter.submitList(results)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
