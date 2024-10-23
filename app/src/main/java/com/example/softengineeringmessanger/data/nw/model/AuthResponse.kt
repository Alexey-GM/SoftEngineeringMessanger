package com.example.softengineeringmessanger.data.nw.model


import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val login: String,
    @SerializedName("token")
    val token: Token
)