package com.example.softengineeringmessanger.di

import com.example.softengineeringmessanger.data.ChatRepositoryImpl
import com.example.softengineeringmessanger.data.nw.MessengerApiService
import com.example.softengineeringmessanger.data.nw.UserManager
import com.example.softengineeringmessanger.domain.ChatRepository
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class ChatRepositoryModule {

    @Provides
    @Singleton
    fun provideChatRepository(
        messengerApiService: MessengerApiService,
        realm: Realm,
        userManager: UserManager
    ): ChatRepository {
        return ChatRepositoryImpl(messengerApiService, realm, userManager)
    }
}