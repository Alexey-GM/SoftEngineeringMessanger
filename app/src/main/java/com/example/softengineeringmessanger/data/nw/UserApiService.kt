package com.example.softengineeringmessanger.data.nw

import com.example.softengineeringmessanger.data.nw.model.AuthRequest
import com.example.softengineeringmessanger.data.nw.model.AuthResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

private const val BASE_URL = "http://212.75.210.227:8080/"

interface UserApiService {
    companion object {
        private val logger: HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        private val okHttp =
            OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

        fun createApiService(): UserApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build()
            return retrofit.create(UserApiService::class.java)
        }
    }

    @PUT("user/v1/registration")
    suspend fun register(
        @Body authRequest: AuthRequest
    ): Response<AuthResponse>

    @POST("user/v1/login")
    suspend fun login(
        @Body authData: AuthRequest
    ): Response<AuthResponse>
}