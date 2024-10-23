package com.example.softengineeringmessanger.data.nw.model


import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("access")
    val access: String,
    @SerializedName("refresh")
    val refresh: String
)