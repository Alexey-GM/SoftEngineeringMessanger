package com.example.softengineeringmessanger.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.softengineeringmessanger.databinding.ItemContactBinding
import com.example.softengineeringmessanger.domain.model.User

class ContactsAdapter(private val onIconClick: (Int) -> Unit) :
    ListAdapter<User, ContactsAdapter.ContactsViewHolder>(
        NewsDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(binding, onIconClick)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class ContactsViewHolder(
        private val binding: ItemContactBinding,
        private val onIconClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvUsername.text = user.login
            binding.ivStartChatting.setOnClickListener {
                onIconClick(user.id)
            }
        }
    }

    private class NewsDiffCallback :
        DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem == newItem
        }
    }
}