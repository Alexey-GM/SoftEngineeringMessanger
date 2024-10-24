package com.example.softengineeringmessanger.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.softengineeringmessanger.domain.ChatRepository
import com.example.softengineeringmessanger.ui.contacts.ContactsUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(private val chatRepository: ChatRepository): ViewModel() {
    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun fetchMessages(from: Int) {
        _uiState.value = ChatUiState.Loading
        viewModelScope.launch (Dispatchers.IO) {
            val result = chatRepository.getMessages(from)
            if (result.isSuccess) {
                val messages = result.getOrNull() ?: emptyList()
                _uiState.value = ChatUiState.Success(messages)
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                _uiState.value = ChatUiState.Error(errorMessage)
            }
        }
    }
}

class ChatViewModelFactory @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(chatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}