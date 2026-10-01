package com.attor.app.ui.create

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.attor.app.R
import com.attor.app.data.NotificationRepository
import com.attor.app.data.SessionManager
import com.attor.app.data.Work
import com.attor.app.data.WorkFormat
import com.attor.app.data.WorkRepository
import com.attor.app.databinding.FragmentCreateWorkBinding
import com.attor.app.ui.login.LoginActivity
import com.attor.app.util.dpToPx
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Pantalla de creación de obra. EXCLUSIVA para el rol USUARIO.
 * Ahora guarda de verdad en la base de datos local (Room) a través de
 * WorkRepository, así que lo publicado aparece luego en Home/Búsqueda/Perfil.
 * Incluye selector de imagen para la portada.
 */
class CreateWorkFragment : Fragment() {

    private var _binding: FragmentCreateWorkBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: WorkRepository
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var session: SessionManager

    private var selectedFormat: WorkFormat = WorkFormat.NOVEL
    private val maxGenres = 3
    private val formatChips = mutableMapOf<TextView, WorkFormat>()
    private var selectedCoverUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            selectedCoverUri = it
            // Tomar permiso persistente para la URI
            requireContext().contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            // Mostrar la imagen seleccionada
            binding.imgCoverPreview.setImageURI(it)
            binding.imgCoverPreview.scaleType = ImageView.ScaleType.CENTER_CROP
            // Ocultar el ícono de galería
            binding.imgGalleryIcon.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session = SessionManager(requireContext())
        repository = WorkRepository(requireContext())
        notificationRepository = NotificationRepository(requireContext())

        if (session.isGuest()) {
            showGuestLockedDialog()
            return
        }

        buildFormatChips()
        setupGenreLimit()
        setupLanguageSpinner()

        binding.btnPickCover.setOnClickListener {
            pickImageLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.btnPublish.setOnClickListener { attemptPublish() }
    }

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

    /** Genera un chip por cada uno de los 8 formatos (Novela, Manga... Arte). */
    private fun buildFormatChips() {
        binding.layoutFormats.removeAllViews()
        formatChips.clear()

        WorkFormat.entries.forEach { format ->
            val chip = TextView(requireContext()).apply {
                text = format.displayName
                val hPad = requireContext().dpToPx(16)
                val vPad = requireContext().dpToPx(8)
                setPadding(hPad, vPad, hPad, vPad)
                textSize = 13f
                setOnClickListener {
                    selectedFormat = format
                    refreshFormatSelection()
                }
            }
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.marginEnd = requireContext().dpToPx(8)
            chip.layoutParams = params
            formatChips[chip] = format
            binding.layoutFormats.addView(chip)
        }
        refreshFormatSelection()
    }

    private fun refreshFormatSelection() {
        formatChips.forEach { (chip, format) ->
            val isSelected = format == selectedFormat
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

    private fun updateChipStyle(chip: Chip, isChecked: Boolean) {
        chip.setChipBackgroundColorResource(
            if (isChecked) R.color.accent_red else R.color.chip_unselected
        )
        chip.setTextColor(
            ContextCompat.getColor(
                requireContext(), if (isChecked) R.color.white else R.color.text_secondary
            )
        )
    }

    private fun setupGenreLimit() {
        val chips = listOf(
            binding.genreAventura, binding.genreFantasia, binding.genreArte, binding.genreTerror
        )
        chips.forEach { chip ->
            updateChipStyle(chip, chip.isChecked)
            chip.setOnCheckedChangeListener { _, isChecked ->
                val checkedCount = chips.count { it.isChecked }
                if (isChecked && (checkedCount > maxGenres)) {
                    chip.isChecked = false
                    Toast.makeText(
                        requireContext(), "Puedes elegir máximo $maxGenres géneros", Toast.LENGTH_SHORT
                    ).show()
                    return@setOnCheckedChangeListener
                }
                updateChipStyle(chip, isChecked)
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

        val work = Work(
            id = UUID.randomUUID().toString(),
            title = title,
            author = session.getUserName(),
            rating = 0f,
            format = selectedFormat,
            synopsis = synopsis,
            statusLabel = "Recién publicado",
            coverUrl = selectedCoverUri?.toString(),
            isUserCreated = true,
            ownerName = session.getUserName()
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repository.publish(work)
            notificationRepository.add("${session.getUserName()} publicó una nueva obra: \"$title\"")
            showPublishSuccess(title)
            clearForm()
        }
    }

    /** Feedback visual: aparece la tarjeta de éxito con una animación de caída + fade. */
    private fun showPublishSuccess(title: String) {
        binding.layoutPublishSuccess.visibility = View.VISIBLE
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up_in)
        binding.layoutPublishSuccess.startAnimation(anim)

        Toast.makeText(
            requireContext(), "\"$title\" publicado como ${selectedFormat.displayName}", Toast.LENGTH_LONG
        ).show()

        binding.layoutPublishSuccess.postDelayed({
            _binding?.layoutPublishSuccess?.visibility = View.GONE
        }, 2600)
    }

    private fun clearForm() {
        binding.etWorkTitle.text?.clear()
        binding.etWorkSynopsis.text?.clear()
        selectedCoverUri = null
        binding.imgCoverPreview.setImageResource(R.drawable.placeholder_cover)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
