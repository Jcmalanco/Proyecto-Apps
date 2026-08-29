package com.attor.app.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.attor.app.R
import com.attor.app.data.Work
import com.attor.app.data.WorkFormat
import com.attor.app.databinding.FragmentSearchBinding
import com.attor.app.ui.workdetails.WorkDetailsActivity
import com.attor.app.util.SampleData
import com.attor.app.util.WorkAdapter

/** Pantalla de búsqueda/exploración. Accesible sin restricciones para ambos roles. */
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WorkAdapter
    private var activeFilter: WorkFormat? = null
    private var query: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WorkAdapter { work -> WorkDetailsActivity.start(requireContext(), work) }
        binding.rvSearchResults.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvSearchResults.adapter = adapter
        applyFilters()

        setupFilterChip(binding.chipAll, null)
        setupFilterChip(binding.chipNovels, WorkFormat.NOVEL)
        setupFilterChip(binding.chipBooks, WorkFormat.BOOK)
        setupFilterChip(binding.chipComics, WorkFormat.COMIC)
        setupFilterChip(binding.chipMusic, WorkFormat.MUSIC)

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {
                query = s?.toString().orEmpty()
                applyFilters()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupFilterChip(chip: TextView, format: WorkFormat?) {
        chip.setOnClickListener {
            activeFilter = format
            highlightSelectedChip(chip)
            applyFilters()
        }
    }

    private fun highlightSelectedChip(selected: TextView) {
        val allChips = listOf(
            binding.chipAll, binding.chipNovels, binding.chipBooks,
            binding.chipComics, binding.chipMusic
        )
        allChips.forEach { chip ->
            val isSelected = chip == selected
            chip.setBackgroundResource(
                if (isSelected) R.drawable.bg_chip_selected else R.drawable.bg_chip_unselected
            )
            chip.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (isSelected) R.color.white else R.color.text_secondary
                )
            )
        }
    }

    private fun applyFilters() {
        val results: List<Work> = SampleData.allWorks().filter { work ->
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
