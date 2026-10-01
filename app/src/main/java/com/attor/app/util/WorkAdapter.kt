package com.attor.app.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.attor.app.R
import com.attor.app.data.Work
import com.attor.app.databinding.ItemBookCardBinding

/**
 * Adaptador reutilizable para grids/carruseles de obras (home, search, library, ver todo).
 * Al extender ListAdapter con DiffUtil, cada vez que llamas a submitList() con una
 * lista distinta, RecyclerView anima automáticamente lo que se agregó/quitó/movió
 * (animación de agregar/eliminar sin escribir código extra).
 */
class WorkAdapter(
    private val onClick: (Work) -> Unit
) : ListAdapter<Work, WorkAdapter.WorkViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        val binding = ItemBookCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WorkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WorkViewHolder(private val binding: ItemBookCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(work: Work) {
            binding.txtTitle.text = work.title
            binding.txtAuthor.text = work.author
            binding.txtRating.text = binding.root.context.getString(R.string.rating_format, work.rating)
            binding.txtFormatBadge.text = work.format.displayName

            binding.imgCover.contentDescription =
                binding.root.context.getString(R.string.cd_cover, work.title)

            if (work.coverUrl.isNullOrBlank()) {
                binding.imgCover.setImageResource(R.drawable.placeholder_cover)
            } else {
                binding.imgCover.setImageResource(R.drawable.placeholder_cover)
            }

            binding.root.contentDescription =
                binding.root.context.getString(R.string.cd_book_card, work.title, work.author)
            binding.root.setOnClickListener { onClick(work) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Work>() {
            override fun areItemsTheSame(oldItem: Work, newItem: Work) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Work, newItem: Work) = oldItem == newItem
        }
    }
}
