package com.example.softengineeringmessanger.domain.model

data class Chat(
    val id: Int,
    val participantId: Int,
    val messages: List<Message>
)
