package com.example.softengineeringmessanger.domain.model

data class Message(
    val id: Int,
    val date: Long,
    val delivered: Boolean,
    val message: String,
    val to: Int
)
