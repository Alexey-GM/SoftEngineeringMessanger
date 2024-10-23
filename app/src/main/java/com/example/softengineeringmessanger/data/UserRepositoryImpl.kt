package com.example.softengineeringmessanger.data

import com.example.softengineeringmessanger.data.nw.UserApiService
import com.example.softengineeringmessanger.data.nw.UserManager
import com.example.softengineeringmessanger.data.nw.model.AuthRequest
import com.example.softengineeringmessanger.domain.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: UserApiService,
    private val userManager: UserManager
) : UserRepository {

    override suspend fun login(login: String, password: String): Result<String> {
        return try {
            val response = apiService.login(AuthRequest(login, password))

            if (response.isSuccessful) {
                response.body()?.token?.access?.let {
                    userManager.saveUserToken(token = it)
                }
                response.body()?.id?.let {
                    userManager.saveUserId(userId = it.toString())
                }
                Result.success("Вход выполнен успешно")
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Throwable("Ошибка входа: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(Throwable("Ошибка сети: ${e.message}"))
        }
    }


    override suspend fun logout() {
    }

    override suspend fun register(login: String, password: String): Result<String> {
        val response = apiService.register(AuthRequest(login, password))
        return if (response.isSuccessful) {
            response.body()?.token?.access?.let { userManager.saveUserToken(token = it) }
            response.body()?.id.let { userManager.saveUserId(userId = it.toString()) }
            Result.success("Регистрация выполнена успешно")
        } else {
            Result.failure(Throwable("Ошибка регистрации"))
        }
    }

    override suspend fun update(): Result<String> {
        return Result.success("123")
    }
}
