package com.example.softengineeringmessanger.domain

import com.example.softengineeringmessanger.domain.model.User

interface UserRepository {
    suspend fun login(user: User): Result<String>
    suspend fun logout()
    suspend fun register(newUser: User): Result<String>
    suspend fun update(): Result<String>
}