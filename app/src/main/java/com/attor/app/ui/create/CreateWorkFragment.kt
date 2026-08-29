package com.attor.app.ui.create

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.attor.app.R
import com.attor.app.data.SessionManager
import com.attor.app.data.WorkFormat
import com.attor.app.databinding.FragmentCreateWorkBinding
import com.attor.app.ui.login.LoginActivity
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Pantalla de creación de obra. EXCLUSIVA para el rol USUARIO.
 *
 * El ítem "Crear" ya se oculta del bottom nav para invitados en MainActivity,
 * pero se añade una guarda adicional aquí (defensa en profundidad) por si
 * se llega a este fragment por otro camino.
 *
 * Un mismo formulario cubre los 7 tipos de contenido pedidos: Novela, Manga,
 * Manhua, Libro, Cómic, Música y Podcast, mediante el selector de chips.
 */
class CreateWorkFragment : Fragment() {

    private var _binding: FragmentCreateWorkBinding? = null
    private val binding get() = _binding!!

    private var selectedFormat: WorkFormat = WorkFormat.COMIC
    private val maxGenres = 3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
        if (session.isGuest()) {
            showGuestLockedDialog()
            return
        }

        setupFormatChips()
        setupGenreLimit()
        setupLanguageSpinner()

        binding.btnPickCover.setOnClickListener {
            Toast.makeText(requireContext(), "Selector de imagen (prototipo)", Toast.LENGTH_SHORT).show()
        }

        binding.btnPublish.setOnClickListener { attemptPublish() }
    }

    /** ROL INVITADO: no puede crear obras -> se le pide iniciar sesión. */
    private fun showGuestLockedDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.guest_login_required_title)
            .setMessage(R.string.guest_login_required_message)
            .setCancelable(false)
            .setPositiveButton(R.string.login_button) { _, _ ->
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
            .show()
    }

    private fun setupFormatChips() {
        val chipsToFormat = mapOf(
            binding.formatNovel to WorkFormat.NOVEL,
            binding.formatManga to WorkFormat.MANGA,
            binding.formatManhua to WorkFormat.MANHUA,
            binding.formatComic to WorkFormat.COMIC,
            binding.formatBook to WorkFormat.BOOK,
            binding.formatMusic to WorkFormat.MUSIC,
            binding.formatPodcast to WorkFormat.PODCAST
        )

        fun refreshSelection() {
            chipsToFormat.forEach { (chip, format) ->
                val isSelected = format == selectedFormat
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

        chipsToFormat.keys.forEach { chip: TextView ->
            chip.setOnClickListener {
                selectedFormat = chipsToFormat.getValue(chip)
                refreshSelection()
            }
        }
        refreshSelection()
    }

    private fun setupGenreLimit() {
        val chips = listOf(
            binding.genreAventura, binding.genreFantasia,
            binding.genreArte, binding.genreTerror
        )
        chips.forEach { chip: Chip ->
            chip.setOnCheckedChangeListener { _, isChecked ->
                val checkedCount = chips.count { it.isChecked }
                if (isChecked && checkedCount > maxGenres) {
                    chip.isChecked = false
                    Toast.makeText(
                        requireContext(),
                        "Puedes elegir máximo $maxGenres géneros",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupLanguageSpinner() {
        val languages = resources.getStringArray(R.array.languages_array)
        binding.spinnerLanguage.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, languages
        )
    }

    private fun attemptPublish() {
        val title = binding.etWorkTitle.text.toString().trim()
        val synopsis = binding.etWorkSynopsis.text.toString().trim()

        if (title.isEmpty() || synopsis.isEmpty()) {
            Toast.makeText(requireContext(), "Completa título y sinopsis", Toast.LENGTH_SHORT).show()
            return
        }

        // Prototipo: no hay backend, solo confirmamos la publicación localmente.
        Toast.makeText(
            requireContext(),
            "¡\"$title\" publicado como ${selectedFormat.displayName}! (prototipo)",
            Toast.LENGTH_LONG
        ).show()

        binding.etWorkTitle.text?.clear()
        binding.etWorkSynopsis.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
