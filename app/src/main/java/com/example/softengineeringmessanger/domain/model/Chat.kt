package com.example.softengineeringmessanger.domain.model

data class Chat(
    val id: Int,
    val participantId: Int,
    val participantLogin: String,
    val lastMessage: Message
)
