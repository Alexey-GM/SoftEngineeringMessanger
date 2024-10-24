package com.example.softengineeringmessanger.di

import com.example.softengineeringmessanger.data.ChatRepositoryImpl
import com.example.softengineeringmessanger.data.local.dao.ChatDao
import com.example.softengineeringmessanger.data.local.dao.MessageDao
import com.example.softengineeringmessanger.data.nw.MessengerApiService
import com.example.softengineeringmessanger.data.nw.UserManager
import com.example.softengineeringmessanger.domain.ChatRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ChatRepositoryModule {

    @Provides
    @Singleton
    fun provideChatRepository(
        messengerApiService: MessengerApiService,
        chatDao: ChatDao,
        messageDao: MessageDao,
        userManager: UserManager
    ): ChatRepository {
        return ChatRepositoryImpl(messengerApiService, chatDao, messageDao, userManager)
    }
}