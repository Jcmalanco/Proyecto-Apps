package com.attor.app.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attor.app.data.NotificationItem
import com.attor.app.databinding.ItemNotificationBinding

class NotificationAdapter(
    private val items: MutableList<NotificationItem>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    /** Reemplaza la lista completa de notificaciones (al cargar desde Room). */
    fun updateItems(messages: List<String>) {
        items.clear()
        items.addAll(messages.map { NotificationItem(id = it.hashCode().toString(), message = it) })
        notifyDataSetChanged()
    }

    /** Elimina una notificación por su posición y notifica al RecyclerView. */
    fun removeItem(position: Int) {
        if (position in items.indices) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    inner class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationItem) {
            binding.txtNotifMessage.text = item.message
        }
    }
}
