package com.attor.app.ui.workdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.attor.app.data.Work
import com.attor.app.databinding.ActivityWorkDetailsBinding
import com.attor.app.util.ChapterAdapter
import com.attor.app.util.SampleData

/**
 * Pantalla de detalle de una obra.
 * Accesible tanto por el rol INVITADO como por el rol USUARIO: ambos pueden
 * leer. Las acciones de edición (solo visibles para el autor) no forman
 * parte de este prototipo.
 */
class WorkDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val workId = intent.getStringExtra(EXTRA_WORK_ID)
        val work = SampleData.allWorks().firstOrNull { it.id == workId }
            ?: SampleData.allWorks().first()

        bindWork(work)

        binding.btnBack.setOnClickListener { finish() }

        binding.rvChapters.layoutManager = LinearLayoutManager(this)
        binding.rvChapters.adapter = ChapterAdapter(SampleData.chaptersFor(work.id)) { chapter ->
            Toast.makeText(this, "Abriendo: ${chapter.title}", Toast.LENGTH_SHORT).show()
        }

        binding.btnStartReading.setOnClickListener {
            Toast.makeText(this, "Abriendo lector (prototipo)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindWork(work: Work) {
        binding.txtWorkTitle.text = work.title
        binding.txtWorkStatus.text = "${work.format.displayName} · ${work.statusLabel}"
        binding.txtAuthorName.text = work.author
        binding.txtWorkRating.text = "★ ${work.rating}"
        binding.txtSynopsisBody.text = SampleData.synopsisFor(work)
    }

    companion object {
        private const val EXTRA_WORK_ID = "extra_work_id"

        fun start(context: Context, work: Work) {
            val intent = Intent(context, WorkDetailsActivity::class.java)
            intent.putExtra(EXTRA_WORK_ID, work.id)
            context.startActivity(intent)
        }
    }
}
