package com.example.softengineeringmessanger.ui.contacts

import com.example.softengineeringmessanger.domain.model.User

sealed class ContactsUiState {
    object Loading : ContactsUiState()
    data class Success(val users: List<User>) : ContactsUiState()
    data class Error(val message: String) : ContactsUiState()
}
