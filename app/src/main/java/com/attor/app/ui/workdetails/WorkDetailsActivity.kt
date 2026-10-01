package com.attor.app.ui.workdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.attor.app.data.Work
import com.attor.app.data.WorkRepository
import com.attor.app.databinding.ActivityWorkDetailsBinding
import com.attor.app.ui.common.BaseActivity
import com.attor.app.util.ChapterAdapter
import com.attor.app.util.SampleData
import kotlinx.coroutines.launch

/**
 * Pantalla de detalle de una obra.
 * Accesible tanto por el rol INVITADO como por el rol USUARIO: ambos pueden leer.
 */
class WorkDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityWorkDetailsBinding
    private var currentRating = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val workId = intent.getStringExtra(EXTRA_WORK_ID)
        binding.btnBack.setOnClickListener { finish() }

        val prefs = getSharedPreferences("attor_likes", MODE_PRIVATE)
        var isLiked = (workId != null) && prefs.getBoolean("liked_$workId", false)

        fun updateLikeIcon() {
            binding.btnLike.setImageResource(
                if (isLiked) com.attor.app.R.drawable.ic_star_filled else com.attor.app.R.drawable.ic_star_outline
            )
            androidx.core.widget.ImageViewCompat.setImageTintList(binding.btnLike, null)
        }

        fun updateRatingDisplay() {
            binding.txtWorkRating.text = getString(
                com.attor.app.R.string.rating_format, currentRating
            )
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

        lifecycleScope.launch {
            val repository = WorkRepository(this@WorkDetailsActivity)
            val work = workId?.let { repository.getById(it) } ?: repository.getAll().firstOrNull()

            if (work == null) {
                Toast.makeText(this@WorkDetailsActivity, "Obra no encontrada", Toast.LENGTH_SHORT).show()
                finish()
                return@launch
            }

            bindWork(work)

            binding.rvChapters.layoutManager = LinearLayoutManager(this@WorkDetailsActivity)
            binding.rvChapters.adapter = ChapterAdapter(SampleData.chaptersFor(work.id)) { chapter ->
                Toast.makeText(
                    this@WorkDetailsActivity, "Abriendo: ${chapter.title}", Toast.LENGTH_SHORT
                ).show()
            }

            binding.btnStartReading.setOnClickListener {
                Toast.makeText(this@WorkDetailsActivity, "Abriendo lector (prototipo)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindWork(work: Work) {
        binding.txtWorkTitle.text = work.title
        binding.txtWorkStatus.text = getString(
            com.attor.app.R.string.work_status_format, work.format.displayName, work.statusLabel
        )
        binding.txtAuthorName.text = work.author
        currentRating = work.rating
        binding.txtWorkRating.text = getString(com.attor.app.R.string.rating_format, currentRating)
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
