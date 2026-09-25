package com.attor.app.ui.workdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val workId = intent.getStringExtra(EXTRA_WORK_ID)
        binding.btnBack.setOnClickListener { finish() }

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
        binding.txtWorkRating.text = getString(com.attor.app.R.string.rating_format, work.rating)
        binding.txtSynopsisBody.text = SampleData.synopsisFor(work)
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
