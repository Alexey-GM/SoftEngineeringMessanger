package com.example.softengineeringmessanger.domain

import com.example.softengineeringmessanger.domain.model.Chat
import com.example.softengineeringmessanger.domain.model.Message

interface ChatRepository {
    suspend fun sendMessage(to: Int, message: String): Result<String>
    suspend fun getMessages(from: Int): Result<List<Message>>
    suspend fun getChats(): Result<List<Chat>>
}