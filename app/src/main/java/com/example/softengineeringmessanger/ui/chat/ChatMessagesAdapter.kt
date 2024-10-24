package com.example.softengineeringmessanger.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.softengineeringmessanger.databinding.ItemCompanionMessageBinding
import com.example.softengineeringmessanger.databinding.ItemMyMessageBinding
import com.example.softengineeringmessanger.domain.model.Message

private const val MY_MESSAGE = 0
private const val COMPANION_MESSAGE = 1

class WeatherItemDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(
        oldItem: Message, newItem: Message
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Message, newItem: Message
    ): Boolean = oldItem == newItem

}

class ChatMessagesAdapter(private val companionId: Int) : ListAdapter<Message, RecyclerView.ViewHolder>(WeatherItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == COMPANION_MESSAGE) {
            val binding =
                ItemCompanionMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CompanionMessageViewHolder(binding)
        } else {
            val binding =
                ItemMyMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MyMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == COMPANION_MESSAGE) {
            (holder as CompanionMessageViewHolder).bind(getItem(position))
        } else {
            (holder as MyMessageViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).to != companionId) COMPANION_MESSAGE else MY_MESSAGE
    }
}

class CompanionMessageViewHolder(
    private val binding: ItemCompanionMessageBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(message: Message) {
        binding.tvText.text = message.message
        binding.tvDate.text = message.date.toString()
    }
}

class MyMessageViewHolder(
    private val binding: ItemMyMessageBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(message: Message) {
        binding.tvText.text = message.message
        binding.tvDate.text = message.date.toString()
    }
}