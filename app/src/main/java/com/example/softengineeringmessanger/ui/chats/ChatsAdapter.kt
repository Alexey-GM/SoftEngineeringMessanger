package com.example.softengineeringmessanger.ui.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.softengineeringmessanger.databinding.ItemChatBinding
import com.example.softengineeringmessanger.databinding.ItemContactBinding
import com.example.softengineeringmessanger.domain.model.Chat
import com.example.softengineeringmessanger.domain.model.User
import com.example.softengineeringmessanger.ui.contacts.ContactsAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatsAdapter(private val onClick: (Int) -> Unit) :
    ListAdapter<Chat, ChatsAdapter.ChatsViewHolder>(
        NewsDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatsViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
    }

    inner class ChatsViewHolder(
        private val binding: ItemChatBinding,
        private val onClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.tvUsername.text = chat.participantLogin
            binding.tvLastMessageText.text = chat.lastMessage.message
            val date = Date(chat.lastMessage.date)
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(date)
            binding.root.setOnClickListener {
                onClick(chat.participantId)
            }
        }
    }

    private class NewsDiffCallback :
        DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(
            oldItem: Chat,
            newItem: Chat
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Chat,
            newItem: Chat
        ): Boolean {
            return oldItem == newItem
        }
    }
}