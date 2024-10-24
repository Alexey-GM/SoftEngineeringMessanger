package com.example.softengineeringmessanger.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.softengineeringmessanger.data.local.model.ChatDB

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatDB): Long

    @Query("SELECT * FROM chats WHERE participantId = :participantId LIMIT 1")
    suspend fun findChat(participantId: Int): ChatDB?

    @Query("SELECT * FROM chats")
    suspend fun getChats(): List<ChatDB>
}