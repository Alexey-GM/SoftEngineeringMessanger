package com.example.softengineeringmessanger.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.softengineeringmessanger.data.local.AppDatabase
import com.example.softengineeringmessanger.data.local.dao.ChatDao
import com.example.softengineeringmessanger.data.local.dao.MessageDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(mApplication: Application): AppDatabase {
        return Room.databaseBuilder(
            mApplication.applicationContext,
            AppDatabase::class.java,
            "messenger_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideChatDao(db: AppDatabase): ChatDao = db.chatDao()

    @Provides
    fun provideMessageDao(db: AppDatabase): MessageDao = db.messageDao()
}
