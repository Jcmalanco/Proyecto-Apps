package com.attor.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.attor.app.R
import com.attor.app.data.ChapterRepository
import com.attor.app.data.SessionManager
import com.attor.app.data.Work
import com.attor.app.data.WorkRepository
import com.attor.app.databinding.ActivityProfileBinding
import com.attor.app.databinding.ItemBookCardBinding
import com.attor.app.ui.common.BaseActivity
import com.attor.app.ui.login.LoginActivity
import com.attor.app.ui.settings.SettingsActivity
import com.attor.app.ui.workdetails.WorkDetailsActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var session: SessionManager
    private lateinit var workRepository: WorkRepository
    private lateinit var adapter: WorksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        workRepository = WorkRepository(this)

        binding.txtName.text = session.getUserName()
        binding.txtEmail.text = session.getUserEmail()

        setupRecyclerView()
        loadUserWorks()

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            session.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = WorksAdapter(
            onWorkClick = { work ->
                WorkDetailsActivity.start(this, work.id)
            },
            onDeleteClick = { work ->
                showDeleteConfirmation(work)
            }
        )
        binding.rvMyWorks.layoutManager = GridLayoutManager(this, 2)
        binding.rvMyWorks.adapter = adapter
    }

    private fun loadUserWorks() {
        lifecycleScope.launch {
            val works = workRepository.getWorksByOwner(session.getUserName())
            adapter.submitList(works)
            binding.txtWorksCount.text = getString(R.string.profile_works_count_format, works.size)
        }
    }

    private fun showDeleteConfirmation(work: Work) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Eliminar obra")
            .setMessage("¿Estás seguro de que quieres eliminar \"${work.title}\"? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteWork(work)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteWork(work: Work) {
        lifecycleScope.launch {
            workRepository.deleteById(work.id)
            ChapterRepository(this@ProfileActivity).deleteByWork(work.id)
            Toast.makeText(this@ProfileActivity, "Obra eliminada", Toast.LENGTH_SHORT).show()
            loadUserWorks()
        }
    }

    /** Adaptador para las obras del usuario en el perfil. */
    private inner class WorksAdapter(
        val onWorkClick: (Work) -> Unit,
        val onDeleteClick: (Work) -> Unit
    ) : RecyclerView.Adapter<WorksAdapter.WorkViewHolder>() {

        private val works = mutableListOf<Work>()

        fun submitList(newWorks: List<Work>) {
            works.clear()
            works.addAll(newWorks)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
            val binding = ItemBookCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return WorkViewHolder(binding)
        }

        override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
            holder.bind(works[position])
        }

        override fun getItemCount(): Int = works.size

        inner class WorkViewHolder(private val binding: ItemBookCardBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(work: Work) {
                binding.txtTitle.text = work.title
                binding.txtAuthor.text = work.author
                binding.txtRating.text = getString(R.string.rating_format, work.rating)
                binding.txtFormatBadge.text = work.format.displayName

                binding.root.setOnClickListener { onWorkClick(work) }

                // Agregar botón de eliminar dinámicamente
                val btnDelete = com.google.android.material.button.MaterialButton(binding.root.context).apply {
                    text = "Eliminar"
                    textSize = 11f
                    setBackgroundColor(getColor(R.color.accent_red))
                    setTextColor(getColor(R.color.white))
                    setOnClickListener { onDeleteClick(work) }
                }

                val params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                (binding.root as ViewGroup).addView(btnDelete, params)
            }
        }
    }
}
