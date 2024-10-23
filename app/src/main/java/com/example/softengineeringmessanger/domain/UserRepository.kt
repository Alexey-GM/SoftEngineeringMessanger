package com.example.softengineeringmessanger.domain

interface UserRepository {
    suspend fun login(login: String, password: String): Result<String>
    suspend fun logout()
    suspend fun register(login: String, password: String): Result<String>
    suspend fun update(): Result<String>
}