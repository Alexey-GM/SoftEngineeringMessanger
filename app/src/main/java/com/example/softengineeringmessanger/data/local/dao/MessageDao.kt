package com.example.softengineeringmessanger.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.softengineeringmessanger.data.local.model.MessageDB

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageDB): Long

    @Query("SELECT * FROM messages WHERE chatId = :chatId")
    suspend fun getMessagesByChatId(chatId: Int): List<MessageDB>

    @Query("SELECT * FROM messages WHERE id = :id LIMIT 1")
    suspend fun findMessageById(id: Int): MessageDB?
}
