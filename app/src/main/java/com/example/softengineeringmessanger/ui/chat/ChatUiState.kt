package com.example.softengineeringmessanger.ui.chat

import com.example.softengineeringmessanger.domain.model.Message

sealed class ChatUiState {
        object Loading : ChatUiState()
        data class Success(val messages: List<Message>) : ChatUiState()
        data class Error(val message: String) : ChatUiState()
}