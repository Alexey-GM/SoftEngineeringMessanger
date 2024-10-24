package com.example.softengineeringmessanger.ui.chats

import com.example.softengineeringmessanger.domain.model.Chat

sealed class ChatsListUiState {
    object Loading : ChatsListUiState()
    data class Success(val chats: List<Chat>) : ChatsListUiState()
    data class Error(val message: String) : ChatsListUiState()
}