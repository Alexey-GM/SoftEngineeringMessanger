package com.example.softengineeringmessanger.data.nw.model


import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("date")
    val date: Long,
    @SerializedName("delivered")
    val delivered: Boolean,
    @SerializedName("from")
    val from: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("to")
    val to: Int
)