package com.example.softengineeringmessanger.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.softengineeringmessanger.domain.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<Result<String>?>(null)
    val loginState: StateFlow<Result<String>?> = _loginState

    private val _registerState = MutableStateFlow<Result<String>?>(null)
    val registerState: StateFlow<Result<String>?> = _registerState

    fun login(login: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.login(login, password)
            _loginState.value = result
        }
    }

    fun register(login: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.register(login, password)
            _registerState.value = result
        }
    }
}

class LoginViewModelFactory @Inject constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}