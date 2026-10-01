package com.attor.app.ui.workdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.attor.app.R
import com.attor.app.data.Chapter
import com.attor.app.data.ChapterRepository
import com.attor.app.data.Work
import com.attor.app.data.WorkRepository
import com.attor.app.databinding.ActivityWorkDetailsBinding
import com.attor.app.databinding.DialogAddChapterBinding
import com.attor.app.ui.common.BaseActivity
import com.attor.app.ui.reader.ReaderActivity
import com.attor.app.util.ChapterAdapter
import com.attor.app.util.SampleData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

/**
 * Pantalla de detalle de una obra.
 * Accesible tanto por el rol INVITADO como por el rol USUARIO: ambos pueden leer.
 * El usuario puede agregar capítulos a sus propias obras.
 */
class WorkDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityWorkDetailsBinding
    private lateinit var chapterBinding: DialogAddChapterBinding
    private var currentRating = 0f
    private var workId: String = ""
    private var isOwner = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workId = intent.getStringExtra(EXTRA_WORK_ID) ?: ""
        binding.btnBack.setOnClickListener { finish() }

        val prefs = getSharedPreferences("attor_likes", MODE_PRIVATE)
        var isLiked = (workId != null) && prefs.getBoolean("liked_$workId", false)

        fun updateLikeIcon() {
            binding.btnLike.setImageResource(
                if (isLiked) R.drawable.ic_star_filled else R.drawable.ic_star_outline
            )
            androidx.core.widget.ImageViewCompat.setImageTintList(binding.btnLike, null)
        }

        fun updateRatingDisplay() {
            binding.txtWorkRating.text = getString(R.string.rating_format, currentRating)
        }

        binding.btnLike.setOnClickListener {
            if (workId != null) {
                isLiked = !isLiked
                prefs.edit { putBoolean("liked_$workId", isLiked) }
                currentRating += if (isLiked) 1f else -1f
                updateLikeIcon()
                updateRatingDisplay()
                val msg = if (isLiked) "¡Obra marcada con estrella!" else "Estrella removida"
                Toast.makeText(this@WorkDetailsActivity, msg, Toast.LENGTH_SHORT).show()
            }
        }

        loadWork()
    }

    private fun loadWork() {
        lifecycleScope.launch {
            val repository = WorkRepository(this@WorkDetailsActivity)
            val work = workId.let { repository.getById(it) } ?: repository.getAll().firstOrNull()

            if (work == null) {
                Toast.makeText(this@WorkDetailsActivity, "Obra no encontrada", Toast.LENGTH_SHORT).show()
                finish()
                return@launch
            }

            bindWork(work)

            // Verificar si el usuario es el dueño de la obra
            val session = com.attor.app.data.SessionManager(this@WorkDetailsActivity)
            isOwner = work.ownerName == session.getUserName()

            // Mostrar botón de agregar capítulo solo si es el dueño
            binding.btnAddChapter.visibility = if (isOwner) android.view.View.VISIBLE else android.view.View.GONE

            loadChapters(work.id)

            binding.btnAddChapter.setOnClickListener {
                showAddChapterDialog(work.id)
            }

            binding.btnStartReading.setOnClickListener {
                val chapters = SampleData.chaptersFor(work.id)
                val firstChapter = chapters.firstOrNull()
                if (firstChapter != null) {
                    ReaderActivity.start(this@WorkDetailsActivity, work.id, firstChapter.id)
                }
            }
        }
    }

    private fun loadChapters(workId: String) {
        lifecycleScope.launch {
            val chapterRepository = ChapterRepository(this@WorkDetailsActivity)
            val chapters = chapterRepository.getChaptersByWork(workId)

            val chapterList = chapters.map { entity ->
                Chapter(
                    id = entity.id.toString(),
                    title = entity.title,
                    dateLabel = "Capítulo"
                )
            }

            binding.rvChapters.layoutManager = LinearLayoutManager(this@WorkDetailsActivity)
            binding.rvChapters.adapter = ChapterAdapter(chapterList) { chapter ->
                ReaderActivity.start(this@WorkDetailsActivity, workId, chapter.id)
            }
        }
    }

    private fun showAddChapterDialog(workId: String) {
        chapterBinding = DialogAddChapterBinding.inflate(layoutInflater)

        MaterialAlertDialogBuilder(this)
            .setView(chapterBinding.root)
            .setPositiveButton("Agregar") { _, _ ->
                val title = chapterBinding.etChapterTitle.text.toString().trim()
                val content = chapterBinding.etChapterContent.text.toString().trim()

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(this, "Completa título y contenido", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    val chapterRepository = ChapterRepository(this@WorkDetailsActivity)
                    chapterRepository.add(workId, title, content)
                    Toast.makeText(this@WorkDetailsActivity, "Capítulo agregado", Toast.LENGTH_SHORT).show()
                    loadChapters(workId)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun bindWork(work: Work) {
        binding.txtWorkTitle.text = work.title
        binding.txtWorkStatus.text = getString(
            R.string.work_status_format, work.format.displayName, work.statusLabel
        )
        binding.txtAuthorName.text = work.author
        currentRating = work.rating
        binding.txtWorkRating.text = getString(R.string.rating_format, currentRating)
        binding.txtSynopsisBody.text = SampleData.synopsisFor(work)
        binding.imgCoverLarge.contentDescription =
            getString(com.attor.app.R.string.cd_cover, work.title)
    }

    companion object {
        private const val EXTRA_WORK_ID = "extra_work_id"

        fun start(context: Context, workId: String) {
            val intent = Intent(context, WorkDetailsActivity::class.java)
            intent.putExtra(EXTRA_WORK_ID, workId)
            context.startActivity(intent)
        }
    }
}
