package com.example.softengineeringmessanger.domain

import com.example.softengineeringmessanger.domain.model.User

interface UserRepository {
    suspend fun login(login: String, password: String): Result<String>
    suspend fun logout()
    suspend fun register(login: String, password: String): Result<String>
    suspend fun update(): Result<String>
    suspend fun getUsers(included: String?): Result<List<User>>
}