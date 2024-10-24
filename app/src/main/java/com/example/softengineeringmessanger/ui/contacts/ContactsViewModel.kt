package com.example.softengineeringmessanger.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.softengineeringmessanger.domain.UserRepository
import com.example.softengineeringmessanger.ui.auth.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactsUiState>(ContactsUiState.Loading)
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    init {
        fetchUsers(null)
    }

    fun fetchUsers(included: String?) {
        _uiState.value = ContactsUiState.Loading
        viewModelScope.launch (Dispatchers.IO) {
            val result = userRepository.getUsers(included)
            if (result.isSuccess) {
                val users = result.getOrNull() ?: emptyList()
                _uiState.value = ContactsUiState.Success(users)
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                _uiState.value = ContactsUiState.Error(errorMessage)
            }
        }
    }
}

class ContactsViewModelFactory @Inject constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            return ContactsViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


