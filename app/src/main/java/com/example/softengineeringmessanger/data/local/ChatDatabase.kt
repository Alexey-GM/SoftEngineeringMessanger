package com.example.softengineeringmessanger.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.softengineeringmessanger.data.local.dao.ChatDao
import com.example.softengineeringmessanger.data.local.dao.MessageDao
import com.example.softengineeringmessanger.data.local.model.ChatDB
import com.example.softengineeringmessanger.data.local.model.MessageDB

@Database(entities = [ChatDB::class, MessageDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
}