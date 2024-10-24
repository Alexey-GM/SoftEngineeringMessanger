package com.example.softengineeringmessanger.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chats")
data class ChatDB(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val participantId: Int
)