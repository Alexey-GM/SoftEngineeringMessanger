package com.example.softengineeringmessanger.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.softengineeringmessanger.domain.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatsListViewModel @Inject constructor(private val chatRepository: ChatRepository): ViewModel() {
    private val _uiState = MutableStateFlow<ChatsListUiState>(ChatsListUiState.Loading)
    val uiState: StateFlow<ChatsListUiState> = _uiState

    init {
        getChats()
    }

    private fun getChats() {
        _uiState.value = ChatsListUiState.Loading
        viewModelScope.launch (Dispatchers.IO) {
            val result = chatRepository.getChats()
            if (result.isSuccess) {
                val messages = result.getOrNull() ?: emptyList()
                _uiState.value = ChatsListUiState.Success(messages)
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                _uiState.value = ChatsListUiState.Error(errorMessage)
            }
        }
    }
}

class ChatsListViewModelFactory @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatsListViewModel::class.java)) {
            return ChatsListViewModel(chatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}