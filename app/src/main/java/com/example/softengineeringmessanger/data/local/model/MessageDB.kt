package com.example.softengineeringmessanger.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    foreignKeys = [ForeignKey(
        entity = ChatDB::class,
        parentColumns = ["id"],
        childColumns = ["chatId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MessageDB(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val delivered: Boolean,
    val message: String,
    val to: Int,
    val chatId: Int
)